package com.group5.springboot.dao.test;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.chat.scaffolding.dev.PostWithPoster;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.product.Rating;
import com.group5.springboot.model.question.Question_Info;
import com.group5.springboot.model.chat.scaffolding.dev.ChatInfoWithRedundancy;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** In courtesy of da almighty chatgpt */
@Profile("test")
@Repository
@Transactional
public class GenericDao {
	private final EntityManager em;


	@Autowired
	public GenericDao(EntityManager em) {
		this.em = em;
	}


	// -----------------------------------------
	// Basic operations
	// -----------------------------------------
	public <T, ID> T find(Class<T> clazz, ID id) {
		return em.find(clazz, id);
	}

	public <T> List<T> findAll(Class<T> clazz) {
		return em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz).getResultList();
	}

	public <T> T save(T entity) {
		em.persist(entity);
		em.flush();   // forces DB write
		return entity;
	}

	public <T> T update(T entity) {
		T merged = em.merge(entity);
		em.flush();
		return merged;
	}

	public <T> void delete(T entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
		em.flush();
	}

	public <T> int deleteAll(Class<T> clazz) {
		return em.createQuery("DELETE FROM " + clazz.getSimpleName()).executeUpdate();
	}

	public ProductInfo saveProductButSkipStorage(ProductInfo rawProduct, User_Info uploader) {
		// real
		rawProduct.setUser_Info(uploader);
		rawProduct.setP_Img("dummy/path/to/image.jpg");
		rawProduct.setP_Video("dummy/path/to/video.mp4");
		rawProduct.setP_Status(0);

		// schema design defect(2.0.0)
		rawProduct.setP_createDate(new Date()); // can be defaulted at db level

		// jpa defects(2.0.0)
		rawProduct.setU_ID(uploader.getU_id()); // redundant from denormalized column
		rawProduct.setP_DESC(SystemUtils.stringToClob(rawProduct.getDescString())); // use lob

		em.persist(rawProduct);

		return rawProduct;
	}

	public ProductInfo adminApprovesProduct(ProductInfo dbProduct) {
		ProductInfo merged = em.merge(dbProduct);
		merged.setP_Status(1);

		return merged;
	}

	public Rating saveRating(Rating rawRating, ProductInfo product) {
		rawRating.setProdcuInfo(product); // missing
		rawRating.setRatedIndex(rawRating.getRatedIndex());
		rawRating.setP_ID(product.getP_ID()); // redundant
		rawRating.setComment(SystemUtils.stringToClob(rawRating.getCommentString())); // should be lob

		em.persist(rawRating); // new -> managed

		return rawRating;
	}

	public Question_Info saveQuestionButSkipExtStorage(Question_Info rawQuestion) throws IOException {
		return saveQuestionButSkipExtStorage(rawQuestion, null);
	}

	private Question_Info saveQuestionButSkipExtStorage(Question_Info rawQuestion, User_Info instructor) throws IOException {
		// all from controller
		{
			// db media storage
			Blob imgBlob = SystemUtils.inputStreamToBlob(rawQuestion.getMultipartFilePic().getInputStream());
			Blob audioBlob = SystemUtils.inputStreamToBlob(rawQuestion.getMultipartFileAudio().getInputStream());
			rawQuestion.setQ_picture(imgBlob);
			rawQuestion.setQ_audio(audioBlob);
			rawQuestion.setMimeTypePic("image/jpeg"); // dummy
			rawQuestion.setMimeTypeAudio("audio/mpeg"); // dummy

			// np
			rawQuestion.setVerification("N");

			// default value issue belongs to schema(entity/table) level
			rawQuestion.setCreateDate(new Timestamp(System.currentTimeMillis()));
		}

		em.persist(rawQuestion);

		return rawQuestion;
	}

	public Question_Info adminApprovesQuestion(Question_Info dbQuestion) {
		Question_Info merged = em.merge(dbQuestion);
		merged.setVerification("Y");

		return merged;
	}

	public ChatInfoWithRedundancy saveTopPost(Chat_Info rawChatInfo, User_Info loginBean) {
		// frontend
		rawChatInfo.setU_ID(loginBean.getU_id());
		rawChatInfo.setC_Date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ssa"))); // default setup belongs to schema level

		// #insertChat
		rawChatInfo.setUser_Info(em.merge(loginBean));
		em.persist(rawChatInfo);
		em.flush();

		// #insertFirstChatReply
		// create a row of redundancy in chat_reply
		// check out 99-extra-notes.md if you don't understand,
		// since this is anti-pattern
		Chat_Reply chatInfoRedundancy = new Chat_Reply();
		chatInfoRedundancy.setC_IDr(rawChatInfo.getC_ID());
		chatInfoRedundancy.setC_Conts(rawChatInfo.getC_Conts());
		chatInfoRedundancy.setC_Date(rawChatInfo.getC_Date());
		chatInfoRedundancy.setU_ID(rawChatInfo.getU_ID());
		em.persist(chatInfoRedundancy);
		em.flush();

		return new ChatInfoWithRedundancy(rawChatInfo, chatInfoRedundancy);
	}

	public Chat_Reply saveReply(Chat_Reply rawChatReply, Chat_Info dbTopPost, User_Info loginBean) {
		// frontend
		rawChatReply.setC_Date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ssa"))); // default value belongs to schema level
		rawChatReply.setU_ID(loginBean.getU_id()); // redundant as a field and as an input from frontend
		rawChatReply.setC_IDr(dbTopPost.getC_ID());

		// service/dao
		rawChatReply.setChat_Info(em.find(Chat_Info.class, rawChatReply.getC_IDr()));
		rawChatReply.setUser_Info(em.find(User_Info.class, loginBean.getU_id()));
		em.persist(rawChatReply);

		return rawChatReply;
	}

	public PostWithPoster[] findPostsWithPosters(int threadId) {
		String hql = "SELECT NEW com.group5.springboot.model.chat.scaffolding.dev.PostWithPoster(c, u) " +
					 "FROM Chat_Reply c LEFT JOIN User_Info u ON c.u_ID = u.u_id WHERE c.c_IDr = :threadId";
		return em.createQuery(hql, PostWithPoster.class)
				.setParameter("threadId", threadId)
				.getResultList().toArray(new PostWithPoster[]{});
	}

	// -----------------------------------------
	// Useful helpers for integration tests
	// -----------------------------------------

	/** Reload entity from DB, bypassing persistence context cache */
	public <T, ID> T reload(Class<T> clazz, ID id) {
		em.flush();
		em.clear();
		return em.find(clazz, id);
	}

	/** For test cases where you need full refresh (rare but sometimes needed) */
	public void flush() {
		em.flush();
	}

	public void clear() {
		em.clear();
	}

	public void flushAndClear() {
		em.flush();
		em.clear();
	}

}