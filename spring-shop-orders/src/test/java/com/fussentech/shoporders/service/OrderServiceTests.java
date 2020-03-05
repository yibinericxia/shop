package com.fussentech.shoporders.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

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
import com.fussentech.shoporders.controller.OrderController;
import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.OrderStatus;
import com.fussentech.shoporders.model.OrderedProduct;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.OrderService;

@WebMvcTest(value = OrderController.class)
public class OrderServiceTests {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	private OrderService service;
	
	private Order order;
	
	@BeforeEach
	public void setup() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setPrice(BigDecimal.valueOf(800.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		order = new Order();
		order.setStatus(OrderStatus.CREATED);
		order.setOrderedProducts(list);
		order.setCreatedTime(LocalDateTime.now());
	}

	@Test
	public void test_findAll_OK() throws Exception {
		List<Order> orders = Arrays.asList(order);
		
		Mockito.when(service.findAll()).thenReturn(orders);
		
		mockMvc.perform(get("/api/orders")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].orderedProducts.size()", is(1)))
				;
	}
	
	@Test
	public void test_findById_NotFound() throws Exception {
		String id = "dummy";
		Mockito.when(service.findById(id)).thenReturn(Optional.empty());
				
		mockMvc.perform(
				MockMvcRequestBuilders
					.get("/api/orders/id/" + id)
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_save_Created() throws Exception {
		Mockito.when(service.save(Mockito.any(Order.class))).thenReturn(order);
		
		MockHttpServletRequestBuilder builder = 
			MockMvcRequestBuilders.post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(order));
		mockMvc.perform(builder)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.orderedProducts.size()", is(1)))
				.andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(order)));
	}
	
}
