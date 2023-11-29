package com.nebiyu.Kelal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebiyu.Kelal.dto.request.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


