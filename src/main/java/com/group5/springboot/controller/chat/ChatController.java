package com.group5.springboot.controller.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.group5.springboot.controller.user.UserController;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.service.chat.ChatService;

@Controller
@SessionAttributes(names = {"loginBean","adminBean"})
public class ChatController {
	
	@Autowired
	ChatService chatService;
	@Autowired
	Chat_Info chat_Info;
	@Autowired
	UserController uc;
	
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
	
	@GetMapping("/goUpdateChat")
	public String updateChat(){
		return "chat/UpdateChat";
	}
	
	@GetMapping("/selectSingleChat/{c_ID}")
	@ResponseBody
	public Chat_Info selectChatById(@PathVariable int c_ID) {
		Chat_Info chat_Info = chatService.selectChatById(c_ID);
		return chat_Info;
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
	
	@PostMapping(path = "/insertChat", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> InsertChat(@RequestBody Chat_Info chat_Info){
		Map<String, String> map = new HashMap<>();
		try {
			chatService.insertChat(chat_Info);
			chatService.createChatTable("chat_Reply" + chat_Info.getC_ID());
			map.put("success", "新增成功");
		} catch (Exception e) {
			map.put("fail", "新增失敗");
			e.printStackTrace();
		}
		return map;
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
			chatService.deleteChat(c_ID);
			chatService.deleteChatTable("chat_Reply" + c_ID);
			map.put("success", "刪除成功");
		} catch (Exception e) {
			map.put("fail", "刪除失敗，請再試一次...");
			e.printStackTrace();
		}
		return map;
	}
	
	@PutMapping(path = "/updateChat/{c_ID}", consumes = { "application/json" }, produces = {"application/json" })
	@ResponseBody
	public Map<String, String> updateChat(@RequestBody Chat_Info chat_Info, @PathVariable int c_ID) {
		Map<String, String> map = new HashMap<>();
		Chat_Info chatIf = null;
		if (String.valueOf(c_ID) != null) {
			chatIf = chatService.selectChatById(c_ID);
			if (chatIf == null || String.valueOf(chatIf.getC_ID()).length() == 0) {
				map.put("fail", "文章: " + c_ID + " 不存在!");
			} else {
				System.out.println("********************************");
				System.out.println("\'id!=null\'， 要修改的文章帳號為: " + c_ID);
				System.out.println("********************************");
				try {
					chatService.updateChat(chat_Info);
					map.put("success", "資料修改成功!");
				} catch (Exception e) {
					map.put("fail", "修改失敗!");
				}
			}
		} else {
			System.out.println("id: " + c_ID + "沒傳進來啊...厚唷");
		}
		try {
			chatService.updateChat(chat_Info);
			map.put("success", "新增成功");
		} catch (Exception e) {
			map.put("fail", "新增失敗");
			e.printStackTrace();
		}
		return map;
	}

}
