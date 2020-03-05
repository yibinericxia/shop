package com.fussentech.shopcustomers.service;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shopcustomers.model.User;
import com.fussentech.shopcustomers.service.UserService;

@WebMvcTest
public class UserServiceTests {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	private UserService userService;
	
	private User user;
	
	@BeforeEach
	public void setup() {
		user = new User();
		user.setFirstname("Tom");
		user.setLastname("Smith");
		user.setEmail("tom@example.com");
		
	}
	
	@Test
	public void getUsers() throws Exception {
		List<User> users = Arrays.asList(user);
		
		Mockito.when(userService.getUsers()).thenReturn(users);
		
		mockMvc.perform(get("/api/users")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].email", is(user.getEmail())));
	}

	@Test
	public void getUserByEmail_OK() throws Exception {
		String email = "tom@example.com";
		Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
				
		mockMvc.perform(
				MockMvcRequestBuilders
					.get("/api/users/email/" + email)
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	@Test
	public void getUserByEmail_NotFound() throws Exception {
		String email = "tom@example.com";
		Mockito.when(userService.findByEmail(email)).thenReturn(Optional.empty());
				
		mockMvc.perform(
				MockMvcRequestBuilders
					.get("/api/users/email/" + email)
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void postUser() throws Exception {
		Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user);
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.post("/api/users")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(user));
		mockMvc.perform(builder)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.email", is(user.getEmail())))
				.andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(user)));
	}
	
	@Test
	public void updateUser_OK() throws Exception {		
		Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(Optional.of(user));
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.put("/api/users")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(user));
		mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is(user.getEmail())))
				.andExpect(MockMvcResultMatchers.content().string(this.mapper.writeValueAsString(user)));
	}
	
	@Test
	public void updateUser_NotFound() throws Exception {		
		Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(Optional.empty());
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.put("/api/users")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(user));
		mockMvc.perform(builder)
				.andExpect(status().isNotFound());
	}
	
	public void deleteUser() throws Exception {
		UserService serviceSpy = Mockito.spy(userService);
		Mockito.doNothing().when(serviceSpy).deleteUser(user);

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status()
				.isNoContent());

		verify(userService, times(1)).deleteUser(user);

	}
}
