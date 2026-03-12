package com.group5.springboot.controller.product;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.group5.springboot.controller.product.ProductTestUtils.aRandomProduct;
import static com.group5.springboot.controller.user.UserTestUtils.aUserKen;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {
	private final MockMvc mockMvc;
	private final UserTestUtils userTestUtils;
	private final GenericDao dao;

	private User_Info ken;
	private ProductInfo product1;
	private ProductInfo product1CreateRequest;
	private ProductInfo product2Approved;
	private ProductInfo product2CreateRequest;
	private MockHttpSession mockHttpSession = new MockHttpSession();


	@Autowired
	ProductControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.userTestUtils = new UserTestUtils(mockMvc);
		this.dao = dao;
	}


	@BeforeEach
	void setUp() {
		mockHttpSession = new MockHttpSession();

		// prepare fresh test data
		// new user ken + ken creates new course
		this.ken = dao.save(aUserKen());
		this.product1CreateRequest = aRandomProduct();
		this.product1 = dao.saveProductButSkipStorage(product1CreateRequest, ken);
		this.product2CreateRequest = aRandomProduct();
		this.product2Approved = dao.saveProductButSkipStorage(product2CreateRequest, ken);
		dao.adminApprovesProduct(product2Approved);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		this.product1CreateRequest = null;
		this.product2CreateRequest = null;
		dao.deleteAll(ProductInfo.class);
		dao.deleteAll(User_Info.class);
	}

	@Test
	@DisplayName("GET /takeClass/{p_id}")
	void takeClass() throws Exception {
		mockMvc.perform(get("/takeClass/{p_ID}", product1.getP_ID()))

				.andExpect(status().isOk())
				.andExpect(view().name("products/detail"))
				.andExpect(model().attributeExists("product"));
	}

	@Test
	@DisplayName("GET /updateProduct/{p_ID} - success")
	void gotoUpdateProduct_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/updateProduct/{p_ID}", product1.getP_ID())
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("products/admin/edit"))
				.andExpect(model().attributeExists("productInfo"));
	}

	@Test
	@DisplayName("GET /updateProduct/{p_ID} - requires admin")
	void gotoUpdateProduct_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/updateProduct/{p_ID}", product1.getP_ID()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /queryProductForUser")
	void queryProductForUser() throws Exception {
		mockMvc.perform(get("/queryProductForUser"))

				.andExpect(status().isOk())
				.andExpect(view().name("products/list"));
	}

	@Test
	@DisplayName("GET /queryProduct - success")
	void sendQueryProduct_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/queryProduct")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("products/admin/list"));
	}

	@Test
	@DisplayName("GET /queryProduct - requires admin")
	void sendQueryProduct_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/queryProduct"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /findAllProductPending - success")
	void findAllProductPending_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/findAllProductPending")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("products/admin/pending-list"));
	}

	@Test
	@DisplayName("GET /findAllProductPending - requires admin")
	void findAllProductPending_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/findAllProductPending"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /accessResult/{p_ID} - success")
	void accessResult_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/accessResult/{p_ID}", product1.getP_ID())
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("products/admin/pending-list"));
	}

	@Test
	@DisplayName("GET /accessResult/{p_ID} - requires admin")
	void accessResult_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/accessResult/{p_ID}", product1.getP_ID()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /insertProduct - success")
	void addProduct_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(ken, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/insertProduct")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("products/add"));
	}

	@Test
	@DisplayName("GET /insertProduct - requires user")
	void addProduct_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/insertProduct"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /updateProduct/{p_ID} - success")
	void updateProduct_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(multipart("/updateProduct/{p_ID}", product2Approved.getP_ID())
						.file((MockMultipartFile) product2CreateRequest.getImgFile())
						.file((MockMultipartFile) product2CreateRequest.getVideoFile())
						.session(mockHttpSession)
						.contentType(MULTIPART_FORM_DATA)
						.param("p_Name", "How to Serve a Beautiful 300'c-baked Matcha Milk Shake")
						.param("p_Class",  "烏克蘭文")
						.param("p_Price", "12805")
						.param("descString", product2CreateRequest.getDescString()))

				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/queryProduct"))
				.andExpect(flash().attributeExists("successMessage"))
				.andReturn();
	}

	@Test
	@DisplayName("POST /updateProduct/{p_ID} - requires admin")
	void updateProduct_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(multipart("/updateProduct/{p_ID}", product2Approved.getP_ID()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("POST /updateProduct/{p_ID} - empty field")
	void updateProduct_whenEmptyField_ThenRequestIsRejected() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(multipart("/updateProduct/{p_ID}", product2Approved.getP_ID())
						.file((MockMultipartFile) product2CreateRequest.getImgFile())
						.file((MockMultipartFile) product2CreateRequest.getVideoFile())
						.session(mockHttpSession)
						.contentType(MULTIPART_FORM_DATA)
						.param("p_Name", "") // mandatory field empty
						.param("p_Class",  "烏克蘭文")
						.param("p_Price", "12805")
						.param("descString", product2CreateRequest.getDescString()))

				.andExpect(view().name("products/admin/edit"))
				.andExpect(model().attributeHasFieldErrors("productInfo", "p_Name"));
	}

	@Test
	@DisplayName("POST /insertProduct - success")
	void saveProduct_success() throws Exception {
		// 0. user login + prepare insert data
		userTestUtils.loginAs(ken, mockHttpSession);
		ProductInfo rawTestProduct = aRandomProduct();


		// 1. main
		mockMvc.perform(multipart("/insertProduct")
							.file((MockMultipartFile) rawTestProduct.getImgFile())
							.file((MockMultipartFile) rawTestProduct.getVideoFile())
							.session(mockHttpSession)
							.contentType(MULTIPART_FORM_DATA)
							.param("u_ID", ken.getU_id())
							.param("p_Name", rawTestProduct.getP_Name())
							.param("p_Class", rawTestProduct.getP_Class())
							.param("p_Price", rawTestProduct.getP_Price() + "")
							.param("descString", rawTestProduct.getDescString()))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/queryProductForUser"));
	}

	@Test
	@DisplayName("POST /insertProduct - requires user")
	void saveProduct_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(multipart("/insertProduct"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /insertProduct - empty field")
	void saveProduct_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. user login + prepare insert data
		userTestUtils.loginAs(ken, mockHttpSession);
		ProductInfo rawTestProduct = aRandomProduct();


		// 1. main
		mockMvc.perform(multipart("/insertProduct")
						.file((MockMultipartFile) rawTestProduct.getImgFile())
						.file((MockMultipartFile) rawTestProduct.getVideoFile())
						.session(mockHttpSession)
						.contentType(MULTIPART_FORM_DATA)
						.param("u_ID", ken.getU_id())
						.param("p_Name", "") // mandatory field empty
						.param("p_Class", rawTestProduct.getP_Class())
						.param("p_Price", rawTestProduct.getP_Price() + "")
						.param("descString", rawTestProduct.getDescString()))

				.andExpect(view().name("products/add"))
				.andExpect(model().attributeHasFieldErrors("productInfo", "p_Name"));
	}

	@Test
	@DisplayName("GET /deleteProduct/{p_ID} - success")
	void deleteProduct_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/deleteProduct/{p_ID}", product1.getP_ID())
						.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/queryProduct"));
	}

	@Test
	@DisplayName("GET /deleteProduct/{p_ID} - requires admin")
	void deleteProduct_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/deleteProduct/{p_ID}", product1.getP_ID()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}
}