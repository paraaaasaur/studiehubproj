package com.group5.springboot.controller.product;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import com.group5.springboot.dto.product.CreateProductForm;
import com.group5.springboot.dto.product.CreateProductView;
import com.group5.springboot.dto.product.UpdateProductForm;
import com.group5.springboot.dto.product.UpdateProductView;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.product.ProductService;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.validate.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import java.io.File;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@Controller
public class ProductController {
	final ProductService productService;
	final ProductValidator productValidator;
	final EntityManager em;

	private final String IMAGE_STORAGE_DIR;
	private final String VIDEO_STORAGE_DIR;
	private final String IMAGE_URL_BASE;
	private final String VIDEO_URL_BASE;


	@Autowired
	public ProductController(ProductService productService, ProductValidator productValidator, EntityManager em, StorageConfigProperties props) {
		this.productService = productService;
		this.productValidator = productValidator;
		this.em = em;
		this.IMAGE_STORAGE_DIR = props.getProductImageUploadStorageDir();
		this.VIDEO_STORAGE_DIR = props.getProductVideoUploadStorageDir();
		this.IMAGE_URL_BASE = StorageConfigProperties.storagePathToViewAndDbUrl(IMAGE_STORAGE_DIR);
		this.VIDEO_URL_BASE = StorageConfigProperties.storagePathToViewAndDbUrl(VIDEO_STORAGE_DIR);
	}


	@GetMapping("/takeClass/{p_ID}")
	public String takeClass(@PathVariable Integer p_ID, Model model) {
		var product = productService.findByProductID(p_ID);
		var detail = productService.mapToProductDetail(product);
		model.addAttribute("product", detail);
		return "products/detail";
	}

	@RequiresAdmin
	@GetMapping("/updateProduct/{p_ID}")
	public String updateProduct(@PathVariable Integer p_ID, Model model) {
		model.addAttribute("updateProductView", getUpdateProductView(p_ID));

		return "products/admin/edit";
	}

	@GetMapping("/queryProductForUser")
	public String queryProductForUser() {
		return "products/list";
	}

	@RequiresAdmin
	@GetMapping("/queryProduct")
	public String sendQueryProduct() {
		return "products/admin/list";
	}

	@RequiresAdmin
	@GetMapping("/findAllProductPending")
	public String findAllProductPending() {
		return "products/admin/pending-list";
	}

	@RequiresAdmin
	@GetMapping("/accessResult/{p_ID}")
	public String accessResult(@PathVariable Integer p_ID) {
		ProductInfo productInfo = productService.findByProductID(p_ID);
		productInfo.setP_Status(1);
		productService.update(productInfo);
		return "products/admin/pending-list";
	}

	@RequiresUser
	@GetMapping("/insertProduct")
	public String addProduct(Model model) {
		model.addAttribute("createProductView", CreateProductView.newInstance());
		return "products/add";
	}

	@RequiresAdmin
	@PostMapping("/updateProduct/{p_ID}")
	public String updateProduct(UpdateProductForm form, RedirectAttributes ra, Model model) {
		var result = productValidator.validate(form);
		if (result.hasErrors()) {
			var view = productService.mapToUpdateProductView(form);
			model.addAttribute("updateProductView", view);
			model.addAttribute(MODEL_KEY_PREFIX + "updateProductView", result);
			return "products/admin/edit";
		}

		var productInfo = productService.applyToEntity(form);

		MultipartFile img = productInfo.getImgFile();
		MultipartFile video = productInfo.getVideoFile();
		if (img != null && img.getSize() > 0) {
			try {
				String imgext = StringUtils.getFilenameExtension(img.getOriginalFilename());
				File imageFolder = new File(IMAGE_STORAGE_DIR);
				if (!imageFolder.exists()) {
					imageFolder.mkdirs();
				}
				String imgFilename = StringUtils.stripFilenameExtension(img.getOriginalFilename()) + "_" + productInfo.getP_ID() + "." + imgext;
				File imgFile = new File(imageFolder, imgFilename);
				img.transferTo(imgFile);
				productInfo.setP_Img(IMAGE_URL_BASE + "/" + imgFilename);


			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}

		}

		if (video != null && video.getSize() > 0) {
			try {


				String videoext = StringUtils.getFilenameExtension(video.getOriginalFilename());
				File videoFolder = new File(VIDEO_STORAGE_DIR);
				if (!videoFolder.exists()) {
					videoFolder.mkdirs();
				}
				String videoFilename = StringUtils.stripFilenameExtension(video.getOriginalFilename()) + "_" + productInfo.getP_ID() + "." + videoext;
				File videoFile = new File(videoFolder, videoFilename);
				video.transferTo(videoFile);
				productInfo.setP_Video(VIDEO_URL_BASE + "/" + videoFilename);


			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		productInfo.setP_DESC(SystemUtils.stringToClob(productInfo.getDescString()));
		productInfo.setP_Status(0);
		productService.update(productInfo);
		ra.addFlashAttribute("successMessage", productInfo.getP_Name() + "更新成功");
		return "redirect:/queryProduct";
	}

	@RequiresUser
	@PostMapping("/insertProduct")
	public String saveProduct(CreateProductForm form, RedirectAttributes ra, Model model) {
		var result = productValidator.validate(form);
		if (result.hasErrors()) {
			var view = productService.mapToCreateProductView(form);
			model.addAttribute("createProductForm", view);
			model.addAttribute(MODEL_KEY_PREFIX + "createProductView", result);
			return "products/add";
		}

		var productInfo = productService.applyToEntity(form);

		MultipartFile img = form.getImgFile();
		MultipartFile video = form.getVideoFile();
		User_Info user_Info = em.find(User_Info.class, form.getU_ID());
		productInfo.setUser_Info(user_Info);

		productInfo.setP_createDate(new Date());
		Clob clob = SystemUtils.stringToClob(form.getDescString());
		productInfo.setP_DESC(clob);
		productService.save(productInfo, form.getU_ID());
		try {
			String imgext = StringUtils.getFilenameExtension(img.getOriginalFilename());
			String videoext = StringUtils.getFilenameExtension(video.getOriginalFilename());
			File imageFolder = new File(IMAGE_STORAGE_DIR);
			File videoFolder = new File(VIDEO_STORAGE_DIR);
			if (!imageFolder.exists()) {
				imageFolder.mkdirs();
			}
			if (!videoFolder.exists()) {
				videoFolder.mkdirs();
			}
			String imgFilename = StringUtils.stripFilenameExtension(img.getOriginalFilename()) + "_" + productInfo.getP_ID() + "." + imgext;
			File imgFile = new File(IMAGE_STORAGE_DIR + "/" + imgFilename);
			img.transferTo(imgFile);
			productInfo.setP_Img(IMAGE_URL_BASE + "/" + imgFilename);
			String videoFilename = StringUtils.stripFilenameExtension(video.getOriginalFilename()) + "_" + productInfo.getP_ID() + "." + videoext;
			File videoFile = new File(VIDEO_STORAGE_DIR + "/" + videoFilename);
			video.transferTo(videoFile);
			productInfo.setP_Video(VIDEO_URL_BASE + "/" + videoFilename);
			productInfo.setP_Status(0);
			productService.update(productInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ra.addFlashAttribute("successMessage", form.getP_Name() + "新增成功");

		return "redirect:/queryProductForUser";
	}

	@RequiresAdmin
	@GetMapping("/deleteProduct/{p_ID}")
	public String deleteProduct(@PathVariable("p_ID") Integer p_ID) {

		productService.deleteProduct(p_ID);

		return "redirect:/queryProduct";
	}


	// facades
	private UpdateProductView getUpdateProductView(Integer p_ID) {
		var productInfo = productService.findByProductID(p_ID);
		return productService.mapToUpdateProductView(productInfo);
	}
}