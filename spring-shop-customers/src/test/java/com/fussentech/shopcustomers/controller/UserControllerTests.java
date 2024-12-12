package com.fussentech.shopcustomers.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fussentech.shopcustomers.model.User;
import com.fussentech.shopcustomers.service.UserService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

@WebMvcTest
public class UserControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
			
	@Autowired
	private UserController controller;
	
	@MockBean
	private UserService service;
	
	@Test
	public void test_getUsers_OK() throws Exception {
		User user = new User();
		user.setFirstname("Tom");
		user.setLastname("Smith");
		user.setEmail("tom@example.com");
		
		List<User> users = Arrays.asList(user);
		
		Mockito.when(controller.getUsers().getBody()).thenReturn(users);
		
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].firstname", is(user.getFirstname())))
			.andExpect(jsonPath("$[0].lastname", is(user.getLastname())))
			.andExpect(jsonPath("$[0].email", is(user.getEmail())))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			;
	}
		
	@Test
	public void test_getUserByEmail_NotFound() throws Exception {
		String email = "foo@example.com";		
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/users/email/" + email)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isNotFound())
			;
	}
		
	@Test
	public void test_getUserById_NotFound() throws Exception {
		// no records
		Long id = 1L;				
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/users/id/" + id)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isNotFound())
			;
	}
		
	@Test
	public void test_createUser_OK() throws Exception {
		String user = "{\"firstname\": \"John\", "
					+ "\"lastname\": \"Smith\", "
					+ "\"email\": \"john.smith@exmaple.com\"}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/users")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			;
	}

	@Test
	public void test_createUser_withEmptyEmail() throws Exception {
		String user = "{\"firstname\": \"John\", "
				+ "\"lastname\": \"Smith\" "
				+ "}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/users")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.email", containsString("must not be")))
			;
	}
	
	@Test
	public void test_createUser_withoutFirstname() throws Exception {
		String user = "{"
				+ "\"lastname\": \"Smith\", "
				+ "\"email\": \"john.smith@exmaple.com\"}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/users")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstname", containsString("must not be")))
			;
	}
	
	@Test
	public void test_createUser_withoutLastname() throws Exception {
		String user = "{\"firstname\": \"John\", "
				+ "\"email\": \"john.smith@exmaple.com\"}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/users")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
	        .andExpect(jsonPath("$.lastname", containsString("must not be")))
	        ;
	}

	@Test
	public void test_createUser_withEmptyBody() throws Exception {
		String user = "{}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/users")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.firstname", containsString("must not be")))
			.andExpect(jsonPath("$.lastname", containsString("must not be")))
			.andExpect(jsonPath("$.email", containsString("must not be")))
			;
	}

}
