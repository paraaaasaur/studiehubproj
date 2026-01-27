package com.group5.springboot.controller.product;

import java.io.File;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import com.group5.springboot.annotation.dev.DeprecatedDetail;
import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.service.product.ProductServiceImpl;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.validate.ProductValidator;

@Controller
public class ProductController {
	
	@Autowired
	ProductServiceImpl productService;
	
	@Autowired
	ProductValidator prodcutValidator;
	@Autowired
	ServletContext context;
	@Autowired
	CartItemService cartItemService;
	@Autowired
	EntityManager em;
	private final String IMAGE_STORAGE_DIR;
	private final String VIDEO_STORAGE_DIR;
	private final String IMAGE_URL_BASE;
	private final String VIDEO_URL_BASE;


	@Autowired
	public ProductController(StorageConfigProperties props) {
		this.IMAGE_STORAGE_DIR = props.getProductImageUploadStorageDir();
		this.VIDEO_STORAGE_DIR = props.getProductVideoUploadStorageDir();
		this.IMAGE_URL_BASE = StorageConfigProperties.storagePathToViewAndDbUrl(IMAGE_STORAGE_DIR);
		this.VIDEO_URL_BASE = StorageConfigProperties.storagePathToViewAndDbUrl(VIDEO_STORAGE_DIR);
	}

	
	@GetMapping("/buyProduct")
	@Deprecated
	@DeprecatedDetail(removeIn = "1.0.2", reason = {"unused", "duplicate"})
	public String buyProduct(@RequestParam Integer p_ID,@RequestParam String u_ID,Model model) {
		System.out.println("**********"+p_ID+u_ID);
		cartItemService.insert(p_ID, u_ID);
		ProductInfo product = productService.findByProductID(p_ID);
		model.addAttribute("product", product);
		return "product/Product";
		
	}
	
	@GetMapping("/takeClass/{p_ID}")
	public String takeClass(@PathVariable Integer p_ID,Model model) {
		ProductInfo product = productService.findByProductID(p_ID);
		model.addAttribute("product", product);
		return "product/Product";
	}

	@RequiresAdmin
	@GetMapping("/updateProduct/{p_ID}")
	public String updateProduct(@PathVariable Integer p_ID,Model model) {
		ProductInfo productInfo = productService.findByProductID(p_ID);
		productInfo.setDescString(productInfo.getP_DESC());
		model.addAttribute("productInfo",productInfo);
		return "product/editProduct";
	}

	@GetMapping("/queryProductForUser")
	public String queryProductForUser() {
		return "product/showProductToUser";
	}

	@RequiresAdmin
	@GetMapping("/queryProduct")
	public String sendQueryProduct() {
		return "product/showProduct";
	}
	@RequiresAdmin
	@GetMapping("/findAllProductPending")
	public String findAllProductPending() {
		return "product/pendingAccess";
	}
	@RequiresAdmin
	@GetMapping("/accessResult/{p_ID}")
	public String accessResult(@PathVariable Integer p_ID,Model model) {
		ProductInfo productInfo = productService.findByProductID(p_ID);
		productInfo.setP_Status(1);
		productService.update(productInfo);
		return "product/pendingAccess";
	}

	@RequiresUser
	@GetMapping("insertProduct")
	public String addProduct() {
		return "product/insertProduct";
	}
	@RequiresAdmin
	@PostMapping("/updateProduct/{p_ID}")
	public String updateProduct(@RequestParam String descString,
								@ModelAttribute("productInfo") ProductInfo productInfo,
								BindingResult result,
								RedirectAttributes ra) {
		
		prodcutValidator.validate(productInfo, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError objectError : list) {
				System.out.println("有錯誤:"+objectError);
			}
			return "product/editProduct";
		}
		MultipartFile img = productInfo.getImgFile();
		MultipartFile video = productInfo.getVideoFile();
		if (img != null && img.getSize()>0) {
			try {
				String imgext = StringUtils.getFilenameExtension(img.getOriginalFilename());
				File imageFolder = new File(IMAGE_STORAGE_DIR);
				if (!imageFolder.exists()) {
					imageFolder.mkdirs();
				}
				String imgFilename = StringUtils.stripFilenameExtension(img.getOriginalFilename())+"_"+productInfo.getP_ID()+ "." + imgext;
				File imgFile = new File(imageFolder, imgFilename);
				img.transferTo(imgFile);
				productInfo.setP_Img(IMAGE_URL_BASE + "/" + imgFilename);

				
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: "+ e.getMessage());
			}
			
		}
		
		if (video != null && video.getSize() >0) {
			try {
				

				String videoext = StringUtils.getFilenameExtension(video.getOriginalFilename());
				File videoFolder = new File(VIDEO_STORAGE_DIR);
				if (!videoFolder.exists()) {
					videoFolder.mkdirs();
				}
				String videoFilename = StringUtils.stripFilenameExtension(video.getOriginalFilename())+"_"+productInfo.getP_ID()+ "." + videoext;
				File videoFile = new File(videoFolder, videoFilename);
				video.transferTo(videoFile);
				productInfo.setP_Video(VIDEO_URL_BASE + "/" + videoFilename);
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: "+ e.getMessage());
			}
		}
		productInfo.setP_DESC(SystemUtils.stringToClob(descString));
		productInfo.setP_Status(0);
		productService.update(productInfo);
		ra.addFlashAttribute("successMessage",productInfo.getP_Name()+"更新成功");
		return "redirect:/queryProduct";
	}

	@RequiresUser
	@PostMapping("insertProduct")
	public String saveProduct(@RequestParam String u_ID ,@RequestParam String descString, @ModelAttribute("productInfo")ProductInfo productInfo,BindingResult result,RedirectAttributes ra) {
		prodcutValidator.validate(productInfo, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤"+ error );
			}
			
			return "product/insertProduct";
		}
		MultipartFile img = productInfo.getImgFile();
		MultipartFile video = productInfo.getVideoFile();
		User_Info user_Info = em.find(User_Info.class, u_ID);
		productInfo.setUser_Info(user_Info);
		
		
		//建立時間
		productInfo.setP_createDate(new Date());
		//desc轉檔
		Clob clob = SystemUtils.stringToClob(descString);
		productInfo.setP_DESC(clob);
		productService.save(productInfo,u_ID);
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
			String imgFilename = StringUtils.stripFilenameExtension(img.getOriginalFilename())+"_"+productInfo.getP_ID()+ "." + imgext;
			File imgFile = new File(IMAGE_STORAGE_DIR + "/" + imgFilename);
			img.transferTo(imgFile);
			productInfo.setP_Img(IMAGE_URL_BASE + "/" + imgFilename);
			String videoFilename = StringUtils.stripFilenameExtension(video.getOriginalFilename())+"_"+productInfo.getP_ID()+ "." + videoext;
			File videoFile = new File(VIDEO_STORAGE_DIR + "/" + videoFilename);
			video.transferTo(videoFile);
			productInfo.setP_Video(VIDEO_URL_BASE + "/" + videoFilename);
			productInfo.setP_Status(0);
			productService.update(productInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ra.addFlashAttribute("successMessage", productInfo.getP_Name() + "新增成功");
		
		return "redirect:/queryProductForUser";
	}

	@RequiresAdmin
	@GetMapping("/deleteProduct/{p_ID}")
	public String deleteProduct(@PathVariable("p_ID") Integer p_ID) {
		
		productService.deleteProduct(p_ID);
		
		return "redirect:/queryProduct";
	}
	
	@ModelAttribute("productInfo")
	public ProductInfo getProductInfo(@RequestParam(value = "p_ID",required = false)Integer p_ID) {
		ProductInfo productInfo = null;
		if (p_ID != null) {
			productInfo = productService.findByProductID(p_ID);
		}else {
			productInfo = new ProductInfo();
		}
		return productInfo;
	}
}