package com.fussentech.shoporders.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.fussentech.shoporders.controller.OrderController;
import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.OrderStatus;
import com.fussentech.shoporders.model.OrderedProduct;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.OrderService;

@WebMvcTest(value = OrderController.class)
public class OrderControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
			
	@Autowired
	private OrderController controller;
	
	@MockBean
	private OrderService service;
	
	@Test
	public void test_getOrders_OK() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setPrice(BigDecimal.valueOf(800.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		Order order = new Order();
		order.setStatus(OrderStatus.CREATED);
		order.setOrderedProducts(list);
		order.setCreatedTime(LocalDateTime.now());
		
		List<Order> orders = Arrays.asList(order);
		
		Mockito.when(controller.getOrders().getBody()).thenReturn(orders);
		
		mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].orderedProducts.size()", is(1)))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			;
	}

	//@Test
	public void test_createOrder_OK() throws Exception {
		String clientOrder = "{ \"orderedProducts\": [" + 
				"            {" + 
				"                \"product\": {" + 
				"                    \"id\": 1," + 
				"                    \"name\": \"iPhone\"," + 
				"                    \"maker\": null," + 
				"                    \"category\": null," + 
				"                    \"description\": null," + 
				"                    \"price\": 1500," + 
				"                    \"imageUrl\": null" + 
				"                }," + 
				"                \"quantity\": 2" + 
				"            }" + 
				"        ]," + 
				"        \"createdTime\": \"2018-02-25T19:10:28.646\"" + 
				"}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
			    .characterEncoding("utf-8")
				.content(clientOrder)
			)
			.andExpect(status().isCreated())
			;
	}

	@Test
	public void test_createOrder_withNoProducts() throws Exception {
		String clientOrder = "{\"orderedProducts\": \"null\"}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(clientOrder)
			)
			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.orderedProducts", containsString("must not be")))
			;
	}
	@Test
	public void test_createOrder_withEmptyBody() throws Exception {
		String clientOrder = "{}";
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(clientOrder)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.orderedProducts", containsString("must not be")))
			;
	}

}
