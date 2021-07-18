package com.group5.springboot.controller.product;

import java.io.File;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.product.ProductInfo;
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
	
	@GetMapping("/buyProduct")
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
	
	@GetMapping("/queryProduct")
	public String sendQueryProduct() {
		return "product/showProduct";
	}
	@GetMapping("/findAllProductPending")
	public String findAllProductPending() {
		return "product/pendingAccess";
	}
	@GetMapping("/accessResult/{p_ID}")
	public String accessResult(@PathVariable Integer p_ID,Model model) {
		ProductInfo productInfo = productService.findByProductID(p_ID);
		productInfo.setP_Status(1);
		productService.save(productInfo);
		return "product/pendingAccess";
	}
	
	@GetMapping("insertProduct")
	public String addProduct() {
		return "product/insertProduct";
	}
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
				String imgext = SystemUtils.getExtFilename(img.getOriginalFilename());
				File imageFolder = new File("C:\\_SpringBoot\\workspace\\studiehubproj\\src\\main\\resources\\static\\images\\productImages");
				if (!imageFolder.exists()) {
					imageFolder.mkdirs();
				}
				File imgFile = new File(imageFolder,SystemUtils.getFilename(img.getOriginalFilename())+"_"+productInfo.getP_ID()+imgext);
				img.transferTo(imgFile);
				productInfo.setP_Img(SystemUtils.getFilename(img.getOriginalFilename())+"_"+productInfo.getP_ID()+imgext);

				
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: "+ e.getMessage());
			}
			
		}
		
		if (video != null && video.getSize() >0) {
			try {
				

				String videoext = SystemUtils.getExtFilename(video.getOriginalFilename());
				File videoFolder = new File("C:\\_SpringBoot\\workspace\\studiehubproj\\src\\main\\resources\\static\\video\\productVideo");
				if (!videoFolder.exists()) {
					videoFolder.mkdirs();
				}
				File videoFile = new File(videoFolder,SystemUtils.getFilename(video.getOriginalFilename())+"_"+productInfo.getP_ID()+videoext);
				video.transferTo(videoFile);
				productInfo.setP_Video(SystemUtils.getFilename(video.getOriginalFilename())+"_"+productInfo.getP_ID()+videoext);
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: "+ e.getMessage());
			}
		}
		
		
		productInfo.setP_Status(0);
		productService.update(productInfo);
		ra.addFlashAttribute("successMessage",productInfo.getP_Name()+"更新成功");
		return "redirect:/queryProduct";
	}

	@PostMapping("insertProduct")
	public String saveProduct(@RequestParam String descString, @ModelAttribute("productInfo")ProductInfo productInfo,BindingResult result,RedirectAttributes ra) {
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
		
		
		//建立時間
		productInfo.setP_createDate(new Date());
		//desc轉檔
		Clob clob = SystemUtils.stringToClob(descString);
		productInfo.setP_DESC(clob);
		productService.save(productInfo);
		try {
			String imgext = SystemUtils.getExtFilename(img.getOriginalFilename());
			String videoext = SystemUtils.getExtFilename(video.getOriginalFilename());
			File imageFolder = new File("C:\\_SpringBoot\\workspace\\studiehubproj\\src\\main\\resources\\static\\images\\productImages");
			File videoFolder = new File("C:\\_SpringBoot\\workspace\\studiehubproj\\src\\main\\resources\\static\\video\\productVideo");
			if (!imageFolder.exists()) {
				imageFolder.mkdirs();
			}
			if (!videoFolder.exists()) {
				videoFolder.mkdirs();
			}
			File imgFile = new File(imageFolder,SystemUtils.getFilename(img.getOriginalFilename())+"_"+productInfo.getP_ID()+imgext);
			img.transferTo(imgFile);
			productInfo.setP_Img(SystemUtils.getFilename(img.getOriginalFilename())+"_"+productInfo.getP_ID()+imgext);
			File videoFile = new File(videoFolder,SystemUtils.getFilename(video.getOriginalFilename())+"_"+productInfo.getP_ID()+videoext);
			video.transferTo(videoFile);
			productInfo.setP_Video(SystemUtils.getFilename(video.getOriginalFilename())+"_"+productInfo.getP_ID()+videoext);
			productInfo.setP_Status(0);
			productService.save(productInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ra.addFlashAttribute("successMessage", productInfo.getP_Name() + "新增成功");
		
		return "redirect:/queryProductForUser";
	}
	
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
