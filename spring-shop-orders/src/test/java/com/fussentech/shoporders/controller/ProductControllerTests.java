package com.fussentech.shoporders.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fussentech.shoporders.controller.ProductController;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.ProductService;

@WebMvcTest(value = ProductController.class)
public class ProductControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
			
	@Autowired
	private ProductController controller;
	
	@MockBean
	private ProductService service;
	
	@Test
	public void test_getProducts_All() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("Icecream");
		product.setMaker("Dummy");
		product.setPrice(BigDecimal.valueOf(3.5));
		
		List<Product> products = Arrays.asList(product);
		
		Mockito.when(controller.getProducts(null, null).getBody()).thenReturn(products);
		
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(product.getName())))
			.andExpect(jsonPath("$[0].maker", is(product.getMaker())))
			.andExpect(jsonPath("$[0].id", notNullValue()))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			;
	}
		
	@Test
	public void test_getProducts_ByMaker() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("Icecream");
		product.setMaker("Dummy");
		product.setPrice(BigDecimal.valueOf(3.5));
		
		List<Product> products = Arrays.asList(product);
		String maker = product.getMaker();	
		List<String> makers = Arrays.asList(maker);
		int id = product.getId().intValue();
		Mockito.when(controller.getProducts(makers, null).getBody()).thenReturn(products);

		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/products?maker=" + maker)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(product.getName())))
			.andExpect(jsonPath("$[0].maker", is(product.getMaker())))
			.andExpect(jsonPath("$[0].id", is(id)))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			;
	}
		
	@Test
	public void test_getProductById_OK() throws Exception {	
		Long id = 50L;
		Product product = new Product();
		product.setId(id);
		product.setName("TV");
		product.setPrice(BigDecimal.valueOf(500.0));
		product.setMaker("Samsung");
		
		Mockito.when(service.findById(id)).thenReturn(Optional.of(product));
        
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/products/id/" + id)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(product.getName())))
			.andExpect(jsonPath("$.maker", equalTo(product.getMaker())))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			
	}
	
	@Test
	public void test_getProductById_NotFound() throws Exception {
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
