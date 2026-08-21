package com.group5.springboot.controller.question;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import com.group5.springboot.dto.question.*;
import com.group5.springboot.model.question.Question_Info;
import com.group5.springboot.service.question.QuestionService;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.validate.QuestionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@Controller
public class QuestionController {
	final QuestionService questionService;
	final ServletContext context;
	final QuestionValidator questionValidator;

	private final String IMAGE_AUDIO_STORAGE_DIR;


	@Autowired
	public QuestionController(QuestionService questionService, ServletContext context, QuestionValidator questionValidator, StorageConfigProperties props) {
		this.questionService = questionService;
		this.context = context;
		this.questionValidator = questionValidator;
		IMAGE_AUDIO_STORAGE_DIR = props.getQuestionAudioAndImageUploadStorageDir();
	}


	@GetMapping(path = "/question.controller/turnQuestionIndex")
	public String turnQuestionIndex() {
		return "questions/exam/index";
	}

	@RequiresUser
	@GetMapping("/question.controller/insertQuestion")
	public String sendInsertQuestion(Model model) {
		addCreateQuestionAttributes(model, CreateQuestionView.newInstance());
		return "questions/add";
	}

	@RequiresUser
	@PostMapping("/question.controller/insertQuestion")
	public String saveQuestion(CreateQuestionForm form, RedirectAttributes ra, Model model) {
		var errors = questionValidator.validate(form);
		if (errors.hasErrors()) {
			readdCreateQuestionAttributes(model, form, errors);
			return "questions/add";
		}

		var question_Info = questionService.applyToEntity(form);

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
			blob = SystemUtils.inputStreamToBlob(is);
			mimeTypePic = context.getMimeType(namePic);
			question_Info.setQ_picture(blob);
			question_Info.setMimeTypePic(mimeTypePic);

			is = multipartFileAudio.getInputStream();
			nameAudio = multipartFileAudio.getOriginalFilename();
			blob = SystemUtils.inputStreamToBlob(is);
			mimeTypeAudio = context.getMimeType(nameAudio);
			question_Info.setQ_audio(blob);
			question_Info.setMimeTypeAudio(mimeTypeAudio);

			question_Info.setVerification("N");  //設定為待審核 
			question_Info.setCreateDate(new Timestamp(System.currentTimeMillis()));
		} catch(Exception e) {
			e.printStackTrace();
		}

		questionService.insertQuestion(question_Info);
		String extPic = StringUtils.getFilenameExtension(namePic);
		String extAudio = StringUtils.getFilenameExtension(nameAudio);
		// 將上傳的檔案移到指定的資料夾, 目前註解此功能
		try {
			File fileFolder = new File(IMAGE_AUDIO_STORAGE_DIR);
			if (!fileFolder.exists())
				fileFolder.mkdirs();
			String filenamePic = "QuestionFile_" + question_Info.getQ_id() + "." + extPic;
			File filePic = new File(fileFolder, filenamePic);
			multipartFilePic.transferTo(filePic);
			String filenameAudio = "QuestionFile_" + question_Info.getQ_id() + "." + extAudio;
			File fileAudio = new File(fileFolder, filenameAudio);
			multipartFileAudio.transferTo(fileAudio);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
		}

		ra.addFlashAttribute("successMessage", "申請編號: " + question_Info.getQ_id() + "，  已成功送至審核作業中！");
		return "redirect:/question.controller/guestQueryQuestion";
	}

	@GetMapping("/question.controller/guestQueryQuestion")
	public String sendGuestQueryQuestion() {
		return "questions/list";
	}

	@GetMapping("/question.controller/guestOneQuestion/{q_id}")
    public String guestOneQuestion(@PathVariable Long q_id, Model model) {
		model.addAttribute("questionDetail", getPublicQuestionDetail(q_id));
		return "questions/detail";
	}

	@RequiresAdmin
	@GetMapping("/question.controller/queryQuestion")
	public String sendQueryQuestion() {
		return "questions/admin/list";
	}

	@GetMapping(value="/question.controller/findAllQuestions", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findAllQuestions(){
		return questionService.findAllQuestions();
	}

	@GetMapping(value="/question.controller/queryByName", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> queryByName(@RequestParam("qname") String qname) {
		return questionService.queryByName(qname);
	}

	@RequiresAdmin
	@GetMapping("/question.controller/modifyQuestion/{q_id}")
    public String sendEditPage(@PathVariable Long q_id, Model model) {
		addUpdateQuestionAttributes(model, q_id);
		return "questions/admin/edit";
	}

	@RequiresAdmin
	@PostMapping("/question.controller/modifyQuestion/{q_id}")
	public String updateQuestion(
			@PathVariable Long q_id,
			UpdateQuestionForm form,
			RedirectAttributes ra,
			Model model)
	{
		var errors = questionValidator.validate(form);
		if (errors.hasErrors()) {
			readdUpdateQuestionAttributes(model, q_id, form, errors);
			return "questions/admin/edit";
		}


		var question_Info = questionService.applyToEntity(q_id, form);

		Blob blob = null;
		String mimeTypePic = "";
		String mimeTypeAudio = "";
		String namePic = "";
		String nameAudio = "";
		MultipartFile multipartFilePic = question_Info.getMultipartFilePic();
		MultipartFile multipartFileAudio = question_Info.getMultipartFileAudio();


		if (multipartFilePic != null && multipartFilePic.getSize() > 0) {
			try {
				InputStream is = multipartFilePic.getInputStream();
				namePic = multipartFilePic.getOriginalFilename();
				blob = SystemUtils.inputStreamToBlob(is);
				mimeTypePic = context.getMimeType(namePic);
				question_Info.setQ_picture(blob);
				question_Info.setMimeTypePic(mimeTypePic);
			String extPic = StringUtils.getFilenameExtension(namePic);
			try {
				File fileFolder = new File(IMAGE_AUDIO_STORAGE_DIR);
				if (!fileFolder.exists())
					fileFolder.mkdirs();
				String filenamePic = "QuestionFile_" + question_Info.getQ_id() + "." + extPic;
				File filePic = new File(fileFolder, filenamePic);
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
				blob = SystemUtils.inputStreamToBlob(is);
				mimeTypeAudio = context.getMimeType(nameAudio);
				question_Info.setQ_audio(blob);
				question_Info.setMimeTypeAudio(mimeTypeAudio);

			String extAudio = StringUtils.getFilenameExtension(nameAudio);
			try {
				File fileFolder = new File(IMAGE_AUDIO_STORAGE_DIR);
				if (!fileFolder.exists())
					fileFolder.mkdirs();
				String filenameAudio = "QuestionFile_" + question_Info.getQ_id() + "." + extAudio;
				File fileAudio = new File(fileFolder, filenameAudio);
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
		return "redirect:/question.controller/queryQuestion";
	}

	@RequiresAdmin
	@GetMapping("/question.controller/deleteQuestion/{q_id}")
	public String deleteEditPage(@PathVariable Long q_id, RedirectAttributes ra) {
		Question_Info question_Info = questionService.findById(q_id);
		questionService.deleteQuestion(question_Info);
		ra.addFlashAttribute("successMessage", "題目編號: " + question_Info.getQ_id() + "  刪除成功!");
		return "redirect:/question.controller/queryQuestion";
	}

	@GetMapping(value="/question.controller/sendRandomMixExam", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> sendRandomMixExam(){
		return questionService.sendRandomMixExam();
	}

	@GetMapping("/question.controller/startRandomMixExam")
	public String startRandomMixExam() {
		return "questions/exam/jp-mixed-types";
	}

	@RequiresAdmin
	@GetMapping("/question.controller/intoVerifyQuestion")
	public String intoVerifyQuestion() {
		return "questions/admin/pending-list";
	}

	@RequiresAdmin
	@GetMapping(value="/question.controller/sendVerifyQuestion", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> sendVerifyQuestion(){
		return questionService.sendVerifyQuestion();
	}

	@RequiresAdmin
	@GetMapping("/question.controller/verifyPassQuestion/{q_id}")
	public String verifyPassQuestion(@PathVariable Long q_id, RedirectAttributes ra) {
		Question_Info question_Info = questionService.findById(q_id);
		question_Info.setVerification("Y");
		questionService.update(question_Info);
		ra.addFlashAttribute("successMessage", "申請編號: " + question_Info.getQ_id() + "  審核通過！");
		return "redirect:/question.controller/intoVerifyQuestion";
	}

	@RequiresAdmin
	@GetMapping("/question.controller/verifyDeleteQuestion/{q_id}")
	public String verifydeleteEditPage(@PathVariable Long q_id, RedirectAttributes ra) {
	Question_Info question_Info = questionService.findById(q_id);
	questionService.deleteQuestion(question_Info);
	ra.addFlashAttribute("successMessage", "申請編號: " + question_Info.getQ_id() + "  未通過審核，已取消申請！");
	return "redirect:/question.controller/intoVerifyQuestion";
	}

	@RequiresAdmin
	@GetMapping("/question.controller/verifyOneQuestion/{q_id}")
    public String verifyOneQuestion(@PathVariable Long q_id, Model model) {
		model.addAttribute("questionDetail", getPendingQuestionDetail(q_id));
		return "questions/admin/pending-detail";
	}


	// helpers
	private void addCreateQuestionAttributes(Model model, CreateQuestionView view) {
		model.addAttribute("classList", getClassList());
		model.addAttribute("typeList", getTypeList());
		model.addAttribute("answerList", getAnswerList());
		model.addAttribute("createQuestionView", CreateQuestionView.newInstance());
	}

	private void readdCreateQuestionAttributes(Model model, CreateQuestionForm form, BindingResult errors) {
		var view = questionService.mapToCreateQuestionView(form);

		model.addAttribute("createQuestionView", view);
		model.addAttribute(MODEL_KEY_PREFIX + "createQuestionView", errors);
		model.addAttribute("classList", getClassList());
		model.addAttribute("typeList", getTypeList());
		model.addAttribute("answerList", getAnswerList());
	}

	private void addUpdateQuestionAttributes(Model model, Long q_id) {
		var entity = questionService.findApprovedById(q_id);
		var view = questionService.mapToUpdateQuestionView(entity);

		model.addAttribute("classList", getClassList());
		model.addAttribute("typeList", getTypeList());
		model.addAttribute("answerList", getAnswerList());
		model.addAttribute("updateQuestionView", view);
	}

	private void readdUpdateQuestionAttributes(Model model, Long q_id, UpdateQuestionForm form, BindingResult errors) {
		var view = questionService.mapToUpdateQuestionView(q_id, form);

		model.addAttribute("classList", getClassList());
		model.addAttribute("typeList", getTypeList());
		model.addAttribute("answerList", getAnswerList());
		model.addAttribute("updateQuestionView", view);
		model.addAttribute(MODEL_KEY_PREFIX + "updateQuestionView", errors);
	}

	// facades
	private QuestionDetail getPendingQuestionDetail(Long q_id) {
		var entity = questionService.findById(q_id);
		var questionDetail = questionService.mapToQuestionDetail(entity);
		return questionDetail;
	}

	private QuestionDetail getPublicQuestionDetail(Long q_id) {
		var entity = questionService.findApprovedById(q_id);
		var questionDetail = questionService.mapToQuestionDetail(entity);
		return questionDetail;
	}

	// model attrs
	public Map<String, String> getClassList() {
		Map<String, String> map = new HashMap<>();
		map.put("英語", "英語");
		map.put("日語", "日語");
		map.put("德語", "德語");
		return map;
	}

	public Map<String, String> getTypeList() {
		Map<String, String> map = new HashMap<>();
		map.put("單選題", "單選題");
		map.put("多選題", "多選題");
		map.put("聽力題", "聽力題");
		return map;
	}

	public Map<String, String>  getAnswerList() {
		Map<String, String> map = new HashMap<>();
		map.put("A", "選項A");
		map.put("B", "選項B");
		map.put("C", "選項C");
		map.put("D", "選項D");
		map.put("E", "選項E");
		return map;
	}
}