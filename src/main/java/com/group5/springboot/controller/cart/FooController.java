package com.group5.springboot.controller.cart;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.group5.springboot.service.cart.FooService;

@Controller
public class FooController {

	@Autowired
    private FooService fooService;
    /**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/displayallbeans") 
    public String getHeaderAndBody(Map model){
        model.put("header", fooService.getHeader());
        model.put("message", fooService.getBody());
        return "displayallbeans";
    }
    /**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
    
	// DIY // 用來列出所有bean的方法
	@Autowired
    private ApplicationContext applicationContext;
    
	@RequestMapping(path = "/listAllBeans", method = RequestMethod.GET)
	@ResponseBody
    public String listAllBeans() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        String aa = "";
        for(String beanName : allBeanNames) {
            System.out.println(beanName);
            aa = aa + beanName + "<br>";
        }
        return aa;
    }
	
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping(value = {"/test04"}, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public JsonNode test04(@RequestBody JsonNode b) {
		System.out.println(b.toString());
		System.out.println(b.toPrettyString());
		return b;
	}
}