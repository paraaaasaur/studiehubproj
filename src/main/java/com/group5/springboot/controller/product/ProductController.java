package com.group5.springboot.controller.product;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.management.RuntimeErrorException;
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
	
	@GetMapping("/updateProduct/{p_ID}")
	public String updateProduct(@PathVariable Integer p_ID,Model model) {
		ProductInfo product = productService.findByProductID(p_ID);
		product.setDescString(product.getP_DESC());
		model.addAttribute("productInfo",product);
		
		return "product/editProduct";
	}
	
	@GetMapping("/queryProduct")
	public String sendQueryProduct() {
		return "product/showProduct";
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
		ProductInfo oldProduct = productService.findByProductID(productInfo.getP_ID());
		
		prodcutValidator.validate(productInfo, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError objectError : list) {
				System.out.println("有錯誤:"+objectError);
			}
			return "product/editProduct";
		}
		Blob blob = null;
		String mimeType = "";
		String name ="";
		MultipartFile imgFile = productInfo.getImgFile();
		MultipartFile videoFile = productInfo.getVideoFile();
		if (imgFile != null && imgFile.getSize()>0) {
			try {
				InputStream is = imgFile.getInputStream();
				name = imgFile.getOriginalFilename();
				blob = SystemUtils.inputStreamToBlob(is);
				mimeType = context.getMimeType(name);
				productInfo.setP_Img(blob);
				productInfo.setImg_mimeType(mimeType);
				
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: "+ e.getMessage());
			}
			
//		}else {
//			productInfo.setImg_mimeType(oldProduct.getImg_mimeType());
//			productInfo.setP_Img(oldProduct.getP_Img());
//			productInfo.setPictureString(oldProduct.getPictureString());
		}
		
		if (videoFile != null && videoFile.getSize() >0) {
			try {
				
				InputStream is = videoFile.getInputStream();
				name = videoFile.getOriginalFilename();
				mimeType = context.getMimeType(name);
				blob = SystemUtils.inputStreamToBlob(is);
				
				productInfo.setVideo_mimeType(mimeType);
				productInfo.setP_Video(blob);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: "+ e.getMessage());
			}
//		}else {
//			productInfo.setVideo_mimeType(oldProduct.getVideo_mimeType());
//			productInfo.setVideoString(oldProduct.getVideoString());
//			productInfo.setP_Video(oldProduct.getP_Video());
		}
		
		
//		Clob clob = SystemUtils.stringToClob(descString);
//		productInfo.setP_DESC(clob);
//		productService.save(productInfo);
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
		Blob blob = null;
		String img_mimeType = "";
		String video_mimeType = "";
		String name = "";
		MultipartFile img = productInfo.getImgFile();
		MultipartFile video = productInfo.getVideoFile();
		
		try {
			InputStream is = img.getInputStream();
			name = img.getOriginalFilename();
			blob = SystemUtils.inputStreamToBlob(is);
			img_mimeType = context.getMimeType(name);
			productInfo.setImg_mimeType(img_mimeType);
			productInfo.setP_Img(blob);
			is = video.getInputStream();
			name = video.getOriginalFilename();
			blob = SystemUtils.inputStreamToBlob(is);
			video_mimeType = context.getMimeType(name);
			productInfo.setP_Video(blob);
			productInfo.setVideo_mimeType(video_mimeType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//建立時間
		productInfo.setP_createDate(new Date());
		//desc轉檔
		Clob clob = SystemUtils.stringToClob(descString);
		productInfo.setP_DESC(clob);
		productService.save(productInfo);
		
		ra.addFlashAttribute("successMessage", productInfo.getP_Name() + "新增成功");
		
		return "redirect:/queryProduct";
	}
	
	@ModelAttribute("productInfo")
	public ProductInfo getProductInfo(@RequestParam(value = "p_ID",required = false)Integer p_ID) {
		ProductInfo productInfo = null;
		if (p_ID != null) {
			productInfo = productService.findByProductID(p_ID);
			System.out.println(productInfo);
		}else {
			productInfo = new ProductInfo();
			System.out.println("haha");
		}
		return productInfo;
	}
}
