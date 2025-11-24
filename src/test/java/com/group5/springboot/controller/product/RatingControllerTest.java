package com.group5.springboot.controller.product;

import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.product.Rating;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.group5.springboot.controller.product.ProductTestUtils.aCustomRating;
import static com.group5.springboot.controller.product.ProductTestUtils.aRandomProduct;
import static com.group5.springboot.controller.user.UserTestUtils.aUserKen;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RatingControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;

	private ProductInfo product1;
	private MockHttpSession mockHttpSession = new MockHttpSession();


	@Autowired
	RatingControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
	}


	@BeforeEach
	void setUp() {
		mockHttpSession = new MockHttpSession();

		// prepare fresh test data
		// new user ken + ken creates a new course + gets 2 ratings
		User_Info ken = dao.save(aUserKen());
		this.product1 = dao.saveProductButSkipStorage(aRandomProduct(), ken);
		dao.adminApprovesProduct(product1);
		dao.saveRating(aCustomRating("Great!", 2), product1);
		dao.saveRating(aCustomRating("OK", 5), product1);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(Rating.class);
		dao.deleteAll(ProductInfo.class);
		dao.deleteAll(User_Info.class);
	}


	@Test
	@DisplayName("GET /findRatingById")
	void findRatingById() throws Exception {
		mockMvc.perform(get("/findRatingById")
						.param("p_ID", product1.getP_ID() + ""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size", is(2)))
				.andExpect(jsonPath("$.list..ratedIndex", hasItems(2, 5)));
	}

	@Disabled("todo@1.0.2: fix POST /saveRating confusing intention before enabling this test")
	@Test
	@DisplayName("POST /saveRating")
	void saveRatingResult() throws Exception {
		mockMvc.perform(post("/saveRating")
						.contentType(APPLICATION_FORM_URLENCODED)
						.param("p_ID", product1.getP_ID() + "")
						.param("commentString", "善哉")
						.param("ratedIndex", "4"))

//				.andExpect(status().isOk())
//				.andExpect(view().name("product/Product"));

				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/takeClass/1"));
	}
}