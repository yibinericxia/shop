package com.fussentech.shoporders.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.fussentech.shoporders.controller.ProductController;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.ProductService;

@WebMvcTest(value = ProductController.class)
public class ProductServiceTests {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	private ProductService service;
	
	private Product product;
	
	@BeforeEach
	public void setup() {
		product = new Product();
		product.setId(1L);
		product.setName("Icecream");
		product.setMaker("Dummy");
		product.setPrice(BigDecimal.valueOf(3.5));
	}

	@Test
	public void test_findAll_OK() throws Exception {
		List<Product> products = Arrays.asList(product);
		
		Mockito.when(service.findAll()).thenReturn(products);
		
		mockMvc.perform(get("/api/products")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is(product.getName())))
				;
	}

	//@Test
	public void test_findById_OK() throws Exception {
		Long id = 1L;
		Mockito.when(service.findById(id)).thenReturn(Optional.of(product));
				
		mockMvc.perform(
				MockMvcRequestBuilders
					.get("/api/products/id/" + id)
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	@Test
	public void test_findById_NotFound() throws Exception {
		Long id = 1L;
		Mockito.when(service.findById(id)).thenReturn(Optional.empty());
				
		mockMvc.perform(
				MockMvcRequestBuilders
					.get("/api/products/id/" + id)
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_save_Created() throws Exception {
		Mockito.when(service.save(Mockito.any(Product.class))).thenReturn(product);
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(product));
		mockMvc.perform(builder)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(product.getName())))
				.andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(product)));
	}
	
	@Test
	public void test_update_OK() throws Exception {		
		Mockito.when(service.update(Mockito.any(Product.class))).thenReturn(Optional.of(product));
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.put("/api/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(product));
		mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(product.getName())))
				.andExpect(MockMvcResultMatchers.content().string(this.mapper.writeValueAsString(product)));
	}
	
	@Test
	public void test_update_NotFound() throws Exception {		
		Mockito.when(service.update(Mockito.any(Product.class))).thenReturn(Optional.empty());
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.put("/api/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(product));
		mockMvc.perform(builder)
				.andExpect(status().isNotFound());
	}
	
	//@Test
	public void test_delete() throws Exception {
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doNothing().when(serviceSpy).delete(product);

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/products")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status()
				.isNoContent());

		verify(service, times(1)).delete(product);

	}
}
