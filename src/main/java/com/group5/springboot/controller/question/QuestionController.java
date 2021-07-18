package com.group5.springboot.controller.question;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.question.Question_Info;
import com.group5.springboot.service.question.QuestionService;
import com.group5.springboot.utils.SystemUtilsNickUse;
import com.group5.springboot.validate.QuestionValidator;

@Controller
public class QuestionController {

	@Autowired
	QuestionService questionService;
	
	@Autowired
	ServletContext context;
	
	@Autowired
	QuestionValidator questionValidator;

	
////前往提庫首頁
	@GetMapping(path = "/question.controller/turnQuestionIndex")
	public String turnQuestionIndex() {
		return "question/intro_QuestionIndex";
	}	
	
////送空白表單
	@GetMapping("/question.controller/insertQuestion")
	public String sendInsertQuestion() {
		return "question/insertQuestion";
	}
	
////新增試題
	@PostMapping("/question.controller/insertQuestion")
	public String saveQuestion(@ModelAttribute("Q1") Question_Info question_Info,
			BindingResult result,
			RedirectAttributes ra
			) {
		questionValidator.validate(question_Info, result);
		if (result.hasErrors()) {
			//下列敘述可以理解Spring MVC如何處理錯誤			
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			return "question/insertQuestion";
		}
		
		
		Blob blob = null;
		String mimeTypePic = "";
		String mimeTypeAudio = "";
		String namePic = "";
		String nameAudio = "";
		MultipartFile multipartFilePic = question_Info.getMultipartFilePic();
		MultipartFile multipartFileAudio = question_Info.getMultipartFileAudio();

		try {
			InputStream is = multipartFilePic.getInputStream();
			namePic = multipartFilePic.getOriginalFilename();
			blob = SystemUtilsNickUse.inputStreamToBlob(is);
			mimeTypePic = context.getMimeType(namePic);
			question_Info.setQ_picture(blob);
			question_Info.setMimeTypePic(mimeTypePic);
			
			is = multipartFileAudio.getInputStream();
			nameAudio = multipartFileAudio.getOriginalFilename();
			blob = SystemUtilsNickUse.inputStreamToBlob(is);
			mimeTypeAudio = context.getMimeType(nameAudio);
			question_Info.setQ_audio(blob);
			question_Info.setMimeTypeAudio(mimeTypeAudio);
			
			question_Info.setVerification("N");  //設定為待審核 
			question_Info.setCreateDate(new Timestamp(System.currentTimeMillis()));
			//加入時間戳記

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		questionService.insertQuestion(question_Info);
		String extPic = SystemUtilsNickUse.getExtFilename(namePic);
		String extAudio = SystemUtilsNickUse.getExtFilename(nameAudio);
		// 將上傳的檔案移到指定的資料夾, 目前註解此功能
		try {
			File fileFolder = new File(SystemUtilsNickUse.QUESTION_FILE_FOLDER);
			if (!fileFolder.exists())
				fileFolder.mkdirs();
			File filePic = new File(fileFolder, "QuestionFile_" + question_Info.getQ_id() + extPic);
			multipartFilePic.transferTo(filePic);
			File fileAudio = new File(fileFolder, "QuestionFile_" + question_Info.getQ_id() + extAudio);
			multipartFileAudio.transferTo(fileAudio);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
		}
		
		ra.addFlashAttribute("successMessage", "申請編號: " + question_Info.getQ_id() + "，  已成功送至審核作業中！");
		// 新增或修改成功，要用response.sendRedirect(newURL) 通知瀏覽器對newURL發出請求
		return "redirect:/question.controller/guestQueryQuestion";  
	}
	

////送出顯示所有試題的表單 //使用者
	@GetMapping("/question.controller/guestQueryQuestion")
	public String sendGuestQueryQuestion() {
		return "question/guestQueryQuestion";
	}	
////單筆詳細資料 //使用者
	@GetMapping("/question.controller/guestOneQuestion/{q_id}")
    public String guestOneQuestion(
    		@PathVariable Long q_id, Model model
    ) {
		Question_Info question_Info = questionService.findById(q_id);
		model.addAttribute("Q1", question_Info);
		return "question/guestOneQuestion";
	}	
	
	
	
////送出顯示所有試題的表單 //後台
	@GetMapping("/question.controller/queryQuestion")
	public String sendQueryQuestion() {
		return "question/queryQuestion";
	}
	
////回傳所有試題 (送JSON)
	//produces:指定返回的內容類型，僅當request請求頭中的(Accept)類型中包含該指定類型才返回
	@GetMapping(value="/question.controller/findAllQuestions", produces = "application/json; charset=UTF-8")	
	public @ResponseBody Map<String, Object> findAllQuestions(){
		return questionService.findAllQuestions();
	}	
	
	
////模糊搜尋問題內容

	@GetMapping(value="/question.controller/queryByName", produces = "application/json; charset=UTF-8")	
	public @ResponseBody Map<String, Object> queryByName(
			@RequestParam("qname") String qname
	){
		//System.out.println("rname=" + rname);
		return questionService.queryByName(qname);
	}

////修改試題內容	
	//送該表單
	@GetMapping("/question.controller/modifyQuestion/{q_id}")
    public String sendEditPage(
    		@PathVariable Long q_id, Model model
    ) {
		Question_Info question_Info = questionService.findById(q_id);
	    
		//處理複選答案，把資料庫字串答案塞到暫存的answer陣列欄位
		String q_answer = question_Info.getQ_answer();
	    System.out.println(q_answer);
	    question_Info.setAnswers(q_answer.split(","));
		
		model.addAttribute("Q1", question_Info);
		return "question/editQuestion";
	}	
	//提出表單修改要求
	@PostMapping("/question.controller/modifyQuestion/{q_id}")
	public String updateQuestion(@ModelAttribute("Q1") Question_Info question_Info,
			BindingResult result, 
			RedirectAttributes ra) {
		
		questionValidator.validate(question_Info, result);
		if (result.hasErrors()) {
			//下列敘述可以理解Spring MVC如何處理錯誤			
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			return "question/editQuestion";
		}
		
		Blob blob = null;
		String mimeTypePic = "";
		String mimeTypeAudio = "";
		String namePic = "";
		String nameAudio = "";
		MultipartFile multipartFilePic = question_Info.getMultipartFilePic();
		MultipartFile multipartFileAudio = question_Info.getMultipartFileAudio();
		
		System.out.println("multipartFilePic=" + multipartFilePic);
		System.out.println("size=" + multipartFilePic.getSize());

		if (multipartFilePic != null && multipartFilePic.getSize() > 0) {
			try {
				InputStream is = multipartFilePic.getInputStream();
				namePic = multipartFilePic.getOriginalFilename();
				blob = SystemUtilsNickUse.inputStreamToBlob(is);
				mimeTypePic = context.getMimeType(namePic);
				question_Info.setQ_picture(blob);
				question_Info.setMimeTypePic(mimeTypePic);
			String extPic = SystemUtilsNickUse.getExtFilename(namePic);
			// 將上傳的檔案移到指定的資料夾, 目前註解此功能
			try {
				File fileFolder = new File(SystemUtilsNickUse.QUESTION_FILE_FOLDER);
				if (!fileFolder.exists())
					fileFolder.mkdirs();
				File filePic = new File(fileFolder, "QuestionFile_" + question_Info.getQ_id() + extPic);
				multipartFilePic.transferTo(filePic);
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		
			}
		if (multipartFileAudio != null && multipartFileAudio.getSize() > 0) {
			try {
				InputStream is = multipartFileAudio.getInputStream();
				nameAudio = multipartFileAudio.getOriginalFilename();
				blob = SystemUtilsNickUse.inputStreamToBlob(is);
				mimeTypeAudio = context.getMimeType(nameAudio);
				question_Info.setQ_audio(blob);
				question_Info.setMimeTypeAudio(mimeTypeAudio);
		
			String extAudio = SystemUtilsNickUse.getExtFilename(nameAudio);
			// 將上傳的檔案移到指定的資料夾, 目前註解此功能
			try {
				File fileFolder = new File(SystemUtilsNickUse.QUESTION_FILE_FOLDER);
				if (!fileFolder.exists())
					fileFolder.mkdirs();
				File fileAudio = new File(fileFolder, "QuestionFile_" + question_Info.getQ_id() + extAudio);
				multipartFileAudio.transferTo(fileAudio);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
			
			} catch(Exception e) {
				e.printStackTrace();
			}
	    }
		
		
        String a = question_Info.toString().replaceAll("\\s+","");  //陣列轉字串，將空白替處理掉
        question_Info.setQ_answer(a.substring(1,a.length()-1));  //處理掉中框弧
        System.out.println(a.substring(1,a.length()-1));
		
		
		questionService.update(question_Info);
		ra.addFlashAttribute("successMessage", "題目編號: " + question_Info.getQ_id() + "  修改成功!");
		// 新增或修改成功，要用response.sendRedirect(newURL) 通知瀏覽器對newURL發出請求
		return "redirect:/question.controller/queryQuestion";  
	}
	
////依照鍵值刪除單筆會員資料
//	@GetMapping("/deleteQuestionById/{q_id}")
//	public @ResponseBody Map<String, String> deleteQuestionById(@PathVariable(required = true) Long q_id) {
//		Map<String, String> map = new HashMap<>();
//		try {
//			questionService.deleteQuestionById(q_id);
//			map.put("success", "刪除成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("fail", "刪除失敗");
//			System.out.println("刪除失敗");
//		}
//		return map;
//	}

	
////刪除單筆試題(by bean)	
	@GetMapping("/question.controller/deleteQuestion/{q_id}")
	public String deleteEditPage(@PathVariable Long q_id, Model model,RedirectAttributes ra) {
	Question_Info question_Info = questionService.findById(q_id);
	questionService.deleteQuestion(question_Info);
	ra.addFlashAttribute("successMessage", "題目編號: " + question_Info.getQ_id() + "  刪除成功!");
	return "redirect:/question.controller/queryQuestion";	
	}
	
////送出顯示所有試題的表單
	@GetMapping("/question.controller/startRandomExam")
	public String startRandomExam() {
		return "question/examQuestion";
	}	
	
////回傳隨機X筆試題 (JSON)
	//produces:指定返回的內容類型，僅當request請求頭中的(Accept)類型中包含該指定類型才返回
	@GetMapping(value="/question.controller/sendRandomExam", produces = "application/json; charset=UTF-8")	
	public @ResponseBody Map<String, Object> sendRandomExam(){
		return questionService.sendRandomExam();
	}
	
////回傳隨機X筆綜合題 (JSON)
	//produces:指定返回的內容類型，僅當request請求頭中的(Accept)類型中包含該指定類型才返回
	@GetMapping(value="/question.controller/sendRandomMixExam", produces = "application/json; charset=UTF-8")	
	public @ResponseBody Map<String, Object> sendRandomMixExam(){
		return questionService.sendRandomMixExam();
	}

////test多選題表單
	@GetMapping("/question.controller/tstartRandomExam")
	public String tstartRandomExam() {
		return "question/examMultipleQuestion";
	}
	
////綜合題表單
	@GetMapping("/question.controller/startRandomMixExam")
	public String startRandomMixExam() {
		return "question/examMixQuestion";
	}
	
////送往後台審核頁面
	@GetMapping("/question.controller/intoVerifyQuestion")
	public String intoVerifyQuestion() {
		return "question/verifyQuestion";
	}
////回傳待審核資料 (JSON)
	@GetMapping(value="/question.controller/sendVerifyQuestion", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> sendVerifyQuestion(){
		return questionService.sendVerifyQuestion();
	}
	
////審核通過，審核欄位N改為Y	
	@GetMapping("/question.controller/verifyPassQuestion/{q_id}")
	public String verifyPassQuestion(@PathVariable Long q_id,Model model,RedirectAttributes ra) {
		Question_Info question_Info = questionService.findById(q_id);
		question_Info.setVerification("Y");
		questionService.update(question_Info);
		ra.addFlashAttribute("successMessage", "申請編號: " + question_Info.getQ_id() + "  審核通過！");
		return "redirect:/question.controller/intoVerifyQuestion";
	}
////審核失敗，刪除試題
	@GetMapping("/question.controller/verifyDeleteQuestion/{q_id}")
	public String verifydeleteEditPage(@PathVariable Long q_id, Model model,RedirectAttributes ra) {
	Question_Info question_Info = questionService.findById(q_id);
	questionService.deleteQuestion(question_Info);
	ra.addFlashAttribute("successMessage", "申請編號: " + question_Info.getQ_id() + "  未通過審核，已取消申請！");
	return "redirect:/question.controller/intoVerifyQuestion";	
	}
////詳細的單筆申請資料
	@GetMapping("/question.controller/verifyOneQuestion/{q_id}")
    public String verifyOneQuestion(
    		@PathVariable Long q_id, Model model
    ) {
		Question_Info question_Info = questionService.findById(q_id);
		model.addAttribute("Q1", question_Info);
		return "question/verifyOneQuestion";
	}	
	

	
	
//// ModelAttribute :  ////
	
	@ModelAttribute("Q1")
	public Question_Info getQuestion1(@RequestParam(value="q_id", required = false ) Long q_id) {
		System.out.println("------------------------------------------");
		Question_Info question_Info = null;
		if (q_id != null) {
			question_Info = questionService.findById(q_id);
		} else {
			question_Info = new Question_Info();
		}
		System.out.println("In @ModelAttribute, question_Info=" + question_Info);
		return question_Info;
	}
	
	
	@ModelAttribute("classList")
    public Map<String, String>  getClassList(){
		Map<String, String> map = new HashMap<>();
		map.put("英語", "英語");
		map.put("日語", "日語");
		map.put("德語", "德語");
		return map;
    }	
	
	@ModelAttribute("typeList")
    public Map<String, String>  getTypeList(){
		Map<String, String> map = new HashMap<>();
		map.put("單選題", "單選題");
		map.put("多選題", "多選題");
		map.put("聽力題", "聽力題");
		return map;
    }
	
	@ModelAttribute("answerList")
    public Map<String, String>  getAnswerList(){
		Map<String, String> map = new HashMap<>();
		map.put("A", "選項A");
		map.put("B", "選項B");
		map.put("C", "選項C");
		map.put("D", "選項D");
		map.put("E", "選項E");
		return map;
    }
	
	
}
