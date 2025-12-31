package com.group5.springboot.dao.test;

import com.group5.springboot.dto.CreateEventRequest;
import com.group5.springboot.dto.cart.ECPayPaymentResult;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.chat.scaffolding.dev.PostWithPoster;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.product.Rating;
import com.group5.springboot.model.question.Question_Info;
import com.group5.springboot.model.chat.scaffolding.dev.ChatInfoWithRedundancy;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.utils.api.ecpay.payment.integration.domain.AioCheckOutOneTime;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.math.BigDecimal;
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
		em.flush();

		return em.find(ProductInfo.class, rawProduct.getP_ID());
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
		em.flush();

		return em.find(Rating.class, rawRating.getR_ID());
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
		em.flush();

		return em.find(Question_Info.class, rawQuestion.getQ_id());
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

		return new ChatInfoWithRedundancy(
				em.find(Chat_Info.class, rawChatInfo.getC_ID()),
				em.find(Chat_Reply.class, chatInfoRedundancy.getC_ID())
		);
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
		em.flush();

		return em.find(Chat_Reply.class, rawChatReply.getC_ID());
	}

	public PostWithPoster[] findPostsWithPosters(int threadId) {
		String hql = "SELECT NEW com.group5.springboot.model.chat.scaffolding.dev.PostWithPoster(c, u) " +
					 "FROM Chat_Reply c LEFT JOIN User_Info u ON c.u_ID = u.u_id WHERE c.c_IDr = :threadId";
		return em.createQuery(hql, PostWithPoster.class)
				.setParameter("threadId", threadId)
				.getResultList().toArray(new PostWithPoster[]{});
	}

	public EventInfo saveEventButNoStorage(CreateEventRequest dto, User_Info loginBean) {
		// all controller logic🫠
		EventInfo eventInfo = dto.toEntity();
		em.persist(eventInfo);
		em.flush();

		eventInfo.setComment(eventInfo.getTransientcomment()); // dto
		eventInfo.setCreationTime(new Timestamp(System.currentTimeMillis())); // schema level
		eventInfo.setA_picturepath("dummy/path/to/image.jpg");
		eventInfo.setUidname(loginBean.getU_lastname() + loginBean.getU_firstname());
		eventInfo.setExpired("未過期"); // computed field or table view
		eventInfo.setVerification("N"); // schema default value
		eventInfo.setA_uid(loginBean.getU_id()); // denormalized column

		em.persist(eventInfo);
		em.flush();

		return em.find(EventInfo.class, eventInfo.getA_aid());
	}

	public void adminApprovesEvent(EventInfo pendingEvent) {
		pendingEvent.setVerification("Y");
		em.merge(pendingEvent);
	}

	/**
	 * <li>Basic dao level CRUD</li>
	 * <li>Skips all validations and side effects required in a usual event signup flow</li>
	 **/
	public void persistEventRegistration(EventInfo event, User_Info applicant) {
		var ef = new Entryform();
		// 打開報名表
		ef.setEventInfo(event);
		ef.setE_id(applicant.getU_id());
		ef.setE_lastname(applicant.getU_lastname());
		ef.setE_firstname(applicant.getU_firstname());
		ef.setE_tel(applicant.getU_tel());
		ef.setE_email(applicant.getU_email());

		em.persist(ef);
	}

	public Entryform findEventRegistration(Long eventId, String userId) {
		Entryform entryform;
		try {
			entryform = em.createQuery("SELECT ef FROM Entryform ef WHERE ef.eventInfo.a_aid = :eventId AND ef.e_id = :userId", Entryform.class)
					.setParameter("eventId", eventId)
					.setParameter("userId", userId)
					.getSingleResult();
		} catch (NoResultException nre) {
			entryform = null;
		}

		return entryform;
	}

	/**
	 * <li>Basic dao level CRUD</li>
	 * <li>Skips all validations and side effects required in a usual flow</li>
	 **/
	public CartItem persistCartItem(ProductInfo product, User_Info customer) {
		// from dao
		var cartItem = new CartItem();
		cartItem.setU_firstname(customer.getU_firstname()); // redundant
		cartItem.setU_lastname(customer.getU_lastname()); // redundant
		cartItem.setP_name(product.getP_Name()); // redundant
		cartItem.setP_price(product.getP_Price()); // redundant
		cartItem.setProductInfo(product);
		cartItem.setUser_Info(customer);
		cartItem.setCart_date("gibberish"); // db-generated

		em.persist(cartItem);
		em.flush();
		em.clear();

//		return em.find(CartItem.class, cartItem.getCart_id());
		var foundCartItem = em.find(CartItem.class, cartItem.getCart_id());
		Hibernate.initialize(foundCartItem.getProductInfo());
		Hibernate.initialize(foundCartItem.getUser_Info());
		return foundCartItem;
	}

	/** Only works for ECPay payment method == credit card */
	private List<OrderInfo> saveOrderAndThenDeleteCart(ECPayPaymentResult dto) {
		// controller raw logic
		String u_id = dto.getCustomField1();
		var customer = em.find(User_Info.class, u_id);
		var cartProducts = em.createQuery("SELECT c.productInfo FROM CartItem c WHERE c.user_Info = :customer", ProductInfo.class)
				.setParameter("customer", customer)
				.getResultList();

		for (var cartProduct : cartProducts) {
			OrderInfo order = new OrderInfo();
				order.setO_id(((BigDecimal) em.createNativeQuery("SELECT IDENT_CURRENT('order_info')").getSingleResult()).intValue());
				order.setO_status("完成"); // belongs to schema-level
				order.setO_amt(Integer.parseInt(dto.getTradeAmt()));
				order.setEcpay_o_id(dto.getMerchantTradeNo());
				order.setEcpay_trade_no(dto.getTradeNo());
		//		order.setO_date(db-default-value);

				order.setProductInfo(cartProduct);
				order.setP_id(cartProduct.getP_ID());
				order.setP_name(cartProduct.getP_Name());
				order.setP_price(cartProduct.getP_Price());

				order.setUser_Info(customer);
				order.setU_id(customer.getU_id());
				order.setU_firstname(customer.getU_firstname());
				order.setU_lastname(customer.getU_lastname());
				order.setU_email(customer.getU_email());

			em.persist(order);
		}

		// delete cart only after order items are persisted
		em.createQuery("DELETE FROM CartItem c WHERE c.user_Info = :customer")
				.executeUpdate();

		return em.createQuery("SELECT o FROM OrderInfo o WHERE o.ecpay_o_id = :merchantTradeNo", OrderInfo.class)
				.setParameter("merchantTradeNo", dto.getMerchantTradeNo())
				.getResultList();
	}

	/**
	 * <li>Basic dao level CRUD that bypasses business flow.</li>
	 * <li>Usually, it constructs order items from the payment result and cart items,
	 * and then deletes cart items in the end</li>
	 **/
	public OrderInfo persistOrder(ProductInfo product, User_Info customer) {
		var order = new OrderInfo();
			order.setO_id(((BigDecimal) em.createNativeQuery("SELECT IDENT_CURRENT('order_info')").getSingleResult()).intValue());
			order.setO_status("完成"); // belongs to schema-level
			order.setO_amt(product.getP_Price());
			order.setEcpay_o_id("studiehub-demo-no-" + UUID.randomUUID());
			order.setEcpay_trade_no("mock-trade-no-8888888");
//			order.setO_date(db-default-value);

			order.setProductInfo(product);
			order.setP_id(product.getP_ID());
			order.setP_name(product.getP_Name());
			order.setP_price(product.getP_Price());

			order.setUser_Info(customer);
			order.setU_id(customer.getU_id());
			order.setU_firstname(customer.getU_firstname());
			order.setU_lastname(customer.getU_lastname());
			order.setU_email(customer.getU_email());

		em.persist(order);

//		return em.find(OrderInfo.class, order.getIdentity_seed());
		var foundOrder = em.find(OrderInfo.class, order.getIdentity_seed());
		Hibernate.initialize(foundOrder.getProductInfo());
		Hibernate.initialize(foundOrder.getUser_Info());
		return foundOrder;
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