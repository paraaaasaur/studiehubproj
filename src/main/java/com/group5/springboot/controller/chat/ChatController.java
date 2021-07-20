package com.group5.springboot.controller.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.controller.user.UserController;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.service.chat.ChatService;
import com.group5.springboot.validate.ChatValidator;

@Controller
@SessionAttributes(names = {"loginBean","adminBean"})
public class ChatController {
	
	@Autowired
	ChatService chatService;
	@Autowired
	Chat_Info chat_Info;
	@Autowired
	UserController uc;
	@Autowired
	ChatValidator chatValidator;
	
	@GetMapping(path = "/chatIndex")
	public String chatIndex() {
		return "chat/ChatIndex";
	}
	
	@GetMapping("/goSelectAllChat")
	public String goSelectAllChat(){
		return "chat/selectAllChat";
	}
	
	@GetMapping("/goSelectAllChatAdmin")
	public String goSelectAllChatAdmin(){
		return "chat/selectAllChatAdmin";
	}
	
	@GetMapping("/goSelectOneChat/{c_ID}")
	public String goSelectOneChat(@PathVariable int c_ID, Model model){
		model.addAttribute("c_ID", c_ID);
		return "chat/selectOneChat";
	}
	
	@GetMapping("/goInsertChat")
	public String insertChat(Model model){
		boolean loginResult = uc.checkIfLogin(model);
		if (loginResult) {
			return "chat/insertChat";
		}else {
			return "user/login";
		}
	}
	
	@GetMapping("/goInsertChatReply")
	public String goInsertChat(){
		return "chat/insertChatReply";
	}
	
	@GetMapping("/goDeleteChat/{c_ID}")
	public String goDeleteChat(@PathVariable int c_ID, Model model){
		model.addAttribute("c_ID", c_ID);
		return "chat/DeleteChat";
	}
	
	@GetMapping("/goDeleteChatAdmin/{c_ID}")
	public String goDeleteChatAdmin(@PathVariable int c_ID, Model model){
		model.addAttribute("c_ID", c_ID);
		return "chat/deleteChatAdmin";
	}
	
	@GetMapping("/goUpdateChat/{c_ID}")
	public String updateChat(@PathVariable int c_ID, Model model){
		Chat_Reply chat_Reply = chatService.selectChatReplyById(c_ID);
		model.addAttribute("chatReply", chat_Reply);
		return "chat/updateChatReply";
	}
	
	@GetMapping("/selectSingleChat/{c_ID}")
	@ResponseBody
	public Chat_Info selectChatById(@PathVariable int c_ID) {
		Chat_Info chat_Info = chatService.selectChatById(c_ID);
		return chat_Info;
	}
	
	@GetMapping("/selectSingleChatReply/{c_ID}")
	@ResponseBody
	public Chat_Reply selectChatReplyById(@PathVariable int c_ID) {
		Chat_Reply chat_Reply = chatService.selectChatReplyById(c_ID);
		return chat_Reply;
	}
	
	@GetMapping(path = "/selectAllChat", produces = {"application/json"})
	@ResponseBody
	public List<Chat_Info> findAllChat() {
		List<Chat_Info> chat_Info = chatService.findAllChat();
		return chat_Info;
	}
	
	@GetMapping(path = "/selectAllChatAdmin", produces = {"application/json"})
	@ResponseBody
	public List<Chat_Info> findAllChatAdmin() {
		List<Chat_Info> chat_Info = chatService.findAllChat();
		return chat_Info;
	}
	
	@GetMapping(path = "/selectOneChat/{c_ID}", produces = {"application/json"})
	@ResponseBody
	public List<Chat_Reply> findOneChat(@PathVariable int c_ID) {
		List<Chat_Reply> chat_Reply = chatService.findAllChatReply(c_ID);
		return chat_Reply;
	}
	
	@PostMapping(path = "/insertChat", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> InsertChat(@RequestBody Chat_Info chat_Info){
		Map<String, String> map = new HashMap<>();
		try {
			chatService.insertChat(chat_Info);
			chatService.insertFirstChatReply(chat_Info);
			map.put("success", "新增成功");
		} catch (Exception e) {
			map.put("fail", "新增失敗");
			e.printStackTrace();
		}
		return map;
	}
	
	@PostMapping(path = "/insertChatReply", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> InsertChatReply(@RequestBody Chat_Reply chat_Reply){
		Map<String, String> map = new HashMap<>();
		try {
			chatService.insertChatReply(chat_Reply);
			map.put("success", "新增成功");
		} catch (Exception e) {
			map.put("fail", "新增失敗");
			e.printStackTrace();
		}
		return map;
	}
	
	@PostMapping("/goInsertChatReply")
	public String insertChatReply(@ModelAttribute("chatReply") Chat_Reply chat_Reply, BindingResult result, RedirectAttributes ra){
		chatValidator.validate(chat_Reply, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			return "chat/insertChatReply";
		}
		chatService.insertChatReply(chat_Reply);
		ra.addFlashAttribute("successMessage", "編號: " + chat_Reply.getC_ID() + "  新增成功!");
		return "redirect:/goSelectOneChat/" + chat_Reply.getC_IDr();
	}
	
	@DeleteMapping("/deleteChat/{c_ID}")
	@ResponseBody
	public Map<String, String> deleteChat(@PathVariable(required = true) int c_ID){
		Map<String, String> map = new HashMap<>();
		try {
			chatService.deleteChat(c_ID);
			map.put("success", "刪除成功");
		} catch (Exception e) {
			map.put("fail", "刪除失敗，請再試一次...");
			e.printStackTrace();
		}
		return map;
	}
	
	@DeleteMapping("/deleteChatAdmin/{c_ID}")
	@ResponseBody
	public Map<String, String> deleteChatAdmin(@PathVariable(required = true) int c_ID){
		Map<String, String> map = new HashMap<>();
		try {
			chatService.deleteChatReply(c_ID);
			chatService.deleteChat(c_ID);
			map.put("success", "刪除成功");
		} catch (Exception e) {
			map.put("fail", "刪除失敗，請再試一次...");
			e.printStackTrace();
		}
		return map;
	}
		
	//修改會員資料
	@PostMapping("/goUpdateChat/{c_ID}")
	public String updateChatReply(@ModelAttribute("chatReply") Chat_Reply chat_Reply, BindingResult result, RedirectAttributes ra){
		chatValidator.validate(chat_Reply, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			return "chat/updateChatReply";
		}
		chatService.updateChatReply(chat_Reply);
		ra.addFlashAttribute("successMessage", "編號: " + chat_Reply.getC_ID() + "  修改成功!");
		return "redirect:/goSelectOneChat/" + chat_Reply.getC_IDr();
	}
	
}
