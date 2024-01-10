package com.nebiyu.Kelal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KelalApplicationTests {

	@Test
	void contextLoads() {
	}

}
//////	@Container
//////	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:4.4.2");
////	@Autowired
////	private MockMvc mockMvc;
////	@Autowired
////	private ObjectMapper objectMapper;
////	@DynamicPropertySource
////static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
////	dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
////
////}
//	@Test
//	void shouldLoginUser() throws Exception {
//
////		AuthenticationRequest request = getUserInfo();
////		String requesString = objectMapper.writeValueAsString(request);
////		mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
////		.contentType(MediaType.APPLICATION_JSON).content(requesString))
////				.andExpect(status().isCreated());
//	}
//
////	private AuthenticationRequest getUserInfo() {
////		return AuthenticationRequest.builder()
////				.email("neba@gmail.com")
////				.password("123@Neba")
////				.build();
////	}


