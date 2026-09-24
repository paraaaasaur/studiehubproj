package com.group5.springboot.controller.chat;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.annotation.dev.RenameSuggestion;
import com.group5.springboot.dto.chat.*;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.chat.ChatService;
import com.group5.springboot.validate.ChatReplyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@Controller
public class ChatController {
	private final ChatService chatService;
	private final ChatReplyValidator chatReplyValidator;


	@Autowired
	public ChatController(ChatService chatService, ChatReplyValidator chatReplyValidator) {
		this.chatService = chatService;
		this.chatReplyValidator = chatReplyValidator;
	}


	@GetMapping("/goSelectAllChat")
	public String goSelectAllChat(){
		return "chat/threads/list";
	}

	@RequiresAdmin
	@GetMapping("/goSelectAllChatAdmin")
	public String goSelectAllChatAdmin(){
		return "chat/threads/admin/list";
	}

	@GetMapping("/goSelectOneChat/{c_ID}")
	public String goSelectOneChat(@PathVariable int c_ID, Model model){
		model.addAttribute("c_ID", c_ID);
		return "chat/threads/detail";
	}

	@RequiresUser
	@GetMapping("/goInsertChat")
	public String insertChat(){
		return "chat/threads/add";
	}

	@RequiresAdmin
	@GetMapping("/goDeleteChatAdmin/{c_ID}")
	public String goDeleteChatAdmin(@PathVariable int c_ID, Model model){
		model.addAttribute("c_ID", c_ID);
		return "chat/threads/admin/delete";
	}

	@RequiresUser
	@GetMapping("/goUpdateChat/{c_ID}")
	@RenameSuggestion("gotoTopPostUpdate")
	public String updateChat(@PathVariable int c_ID, Model model){
		addModelAttributes(model, c_ID);
		return "chat/threads/edit-post";
	}

	@GetMapping("/selectSingleChat/{c_ID}")
	@ResponseBody
	@RenameSuggestion("findTopPost")
	public Chat_Info selectChatById(@PathVariable int c_ID) {
		Chat_Info chat_Info = chatService.selectChatById(c_ID);
		return chat_Info;
	}

	@GetMapping(path = "/selectAllChat", produces = {"application/json"})
	@ResponseBody
	@RenameSuggestion("findAllTopPosts")
	public List<Chat_Info> findAllChat() {
		List<Chat_Info> chat_Info = chatService.findAllChat();
		return chat_Info;
	}

	@RequiresAdmin
	@GetMapping(path = "/selectAllChatAdmin", produces = {"application/json"})
	@ResponseBody
	@RenameSuggestion("findAllTopPostsAdmin")
	public List<Chat_Info> findAllChatAdmin() {
		List<Chat_Info> chat_Info = chatService.findAllChat();
		return chat_Info;
	}

	@GetMapping(path = "/selectOneChat/{c_ID}", produces = {"application/json"})
	@ResponseBody
	@RenameSuggestion("findThread")
	public List<Chat_Reply> findOneChat(@PathVariable int c_ID) {
		List<Chat_Reply> chat_Reply = chatService.findAllChatReply(c_ID);
		return chat_Reply;
	}

	@RequiresUser
	@PostMapping(path = "/insertChat", produces = {"application/json"})
	@ResponseBody
	@RenameSuggestion("insertTopPost")
	public Map<String, String> InsertChat(
			@SessionAttribute User_Info loginBean,
			@RequestBody CreateTopPostRequest req
	) {
		Map<String, String> map = new HashMap<>();
		try {
			chatService.insertTopPostAndRedundancy(loginBean.getU_id(), req);
			map.put("success", "新增成功");
		} catch (Exception e) {
			map.put("fail", "新增失敗");
			e.printStackTrace();
		}
		return map;
	}

	@RequiresUser
	@PostMapping(path = "/insertChatReply", produces = {"application/json"})
	@ResponseBody
	@RenameSuggestion("insertReply")
	public Map<String, String> InsertChatReply(
			@SessionAttribute User_Info loginBean,
			@RequestBody CreateReplyRequest req
	) {
		Map<String, String> map = new HashMap<>();
		try {
			chatService.insertChatReply(loginBean.getU_id(), req);
			map.put("success", "新增成功");
		} catch (Exception e) {
			map.put("fail", "新增失敗");
			e.printStackTrace();
		}
		return map;
	}

	@RequiresAdmin
	@DeleteMapping("/deleteChatAdmin/{c_ID}")
	@ResponseBody
	@RenameSuggestion("deleteThreadAdmin")
	public Map<String, String> deleteChatAdmin(@PathVariable int c_ID){
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

	//技術上：修改討論回覆及討論文章；用意：修改文章
	@RequiresUser
	@PostMapping("/goUpdateChat/{c_ID}")
	@RenameSuggestion("updatePost")
	public String updateChatReply(
			@ModelAttribute("updatePostForm") UpdatePostRequest req, 
			RedirectAttributes ra, 
			Model model
	) {
		var result = chatReplyValidator.validate(req);
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.err.println("有錯誤：" + error);
			}
			
			readdModelAttributes(model, req, result);
			return "chat/threads/edit-post";
		}

		Chat_Reply post = chatService.updateChatReply(req);

		ra.addFlashAttribute("successMessage", "編號: " + post.getC_ID() + "  修改成功!");

		return "redirect:/goSelectOneChat/" + post.getC_IDr();
	}


	// convenience methods
	private void addModelAttributes(Model model, int c_ID) {
		model.addAttribute("updatePostForm", getForm(c_ID));
	}

	private void readdModelAttributes(Model model, UpdatePostRequest req, BindingResult result) {
		model.addAttribute("updatePostForm", toForm(req));
		model.addAttribute(MODEL_KEY_PREFIX + "updatePostForm", result);
	}

	// facades
	private UpdatePostForm getForm(int c_ID) {
		Chat_Reply post = chatService.selectChatReplyById(c_ID);
		return toForm(post);
	}

	// adapters
	private UpdatePostForm toForm(UpdatePostRequest req) {
		return new UpdatePostForm(
				req.getC_IDr(),
				req.getC_Date(),
				req.getC_Conts()
		);
	}

	private UpdatePostForm toForm(Chat_Reply entity) {
		return new UpdatePostForm(
				entity.getC_IDr(),
				entity.getC_Date(),
				entity.getC_Conts()
		);
	}
}