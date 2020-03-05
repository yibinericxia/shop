package com.fussentech.shopproducts.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fussentech.shopproducts.controller.ProductController;
import com.fussentech.shopproducts.model.Product;
import com.fussentech.shopproducts.service.ProductService;

@WebMvcTest
public class ProductControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
			
	@Autowired
	private ProductController controller;
	
	@MockBean
	private ProductService service;
	
	@Test
	public void test_getAllProducts_OK() throws Exception {
		Product product = new Product();
		product.setName("Icecream");
		product.setMaker("Dummy");
		product.setPrice(BigDecimal.valueOf(3.5));
		
		List<Product> products = Arrays.asList(product);
		
		Mockito.when(controller.getAllProducts(null, null, null, null).getBody()).thenReturn(products);
		
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(product.getName())))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			;
	}
		
	@Test
	public void test_getProductsByMaker_OK() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("Icecream");
		product.setMaker("Dummy");
		product.setPrice(BigDecimal.valueOf(3.5));
		
		List<Product> products = Arrays.asList(product);
		String maker = product.getMaker();				
		
		Mockito.when(controller.getProductsByMaker(maker).getBody()).thenReturn(products);

		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/products/maker/" + maker)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(product.getName())))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			;
	}
		
	@Test
	public void test_getProductById_NotFound() throws Exception {
		// no records
		Long id = 1L;				
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/products/id/" + id)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isNotFound())
			;
	}
		
	@Test
	public void test_createProduct_OK() throws Exception {
		String product = "{\"name\": \"TV\", \"price\": 800}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/products")
				.content(product)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			;
	}

	@Test
	public void test_createProduct_withEmptyName() throws Exception {
		String product = "{\"name\": \"\", \"price\": 800}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/products")
				.content(product)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", is("must not be empty")))
			;
	}
	
	@Test
	public void test_createProduct_withoutName() throws Exception {
		String product = "{\"price\": 800}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/products")
				.content(product)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", containsString("must not be")))
			;
	}
	
	@Test
	public void test_createProduct_withoutPrice() throws Exception {
		String product = "{\"name\": \"TV\"}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/products")
				.content(product)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
	        .andExpect(jsonPath("$.price", is("must not be null")))
	        ;
	}

	@Test
	public void test_createProduct_withEmptyBody() throws Exception {
		String product = "{}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/products")
				.content(product)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.name", containsString("must not be")))
	        .andExpect(jsonPath("$.price", is("must not be null")))
			;
	}

}
