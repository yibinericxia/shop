package com.fussentech.shoporders;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.fussentech.shoporders.controller.OrderController;
import com.fussentech.shoporders.dto.OrderDTO;
import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.OrderStatus;
import com.fussentech.shoporders.model.OrderedProduct;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.OrderService;

//Use the real profile Mongo DB for this testing
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class OrderIntegrationTests {

	@LocalServerPort
	private int port;
	
	@Mock
	private OrderService service;
	@InjectMocks
	private OrderController controller;
	
    @BeforeAll
    public void init() {
    	// need to use @TestInstance(Lifecycle.PER_CLASS)
        baseURI = "http://localhost:" + port;
        
    }
    
	@Test
	public void test_createOrder_Created() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setMaker("Samsung");
		product.setPrice(BigDecimal.valueOf(800.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		Order order = new Order();
		order.setOrderedProducts(list);
		order.setCreatedTime(LocalDateTime.now());
		order.setStatus(OrderStatus.CREATED);
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderedProducts(order.getOrderedProducts());
		orderDTO.setCreatedTime(order.getCreatedTime());
		
//		Order retval = 
			given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderDTO)
			.when()
			.post("/api/orders")
			.then()
			.statusCode(HttpStatus.CREATED.value())
//			.extract()
//			.as(Order.class)
			;
//		assertThat(retval).isEqualTo(order);
	}
	
	@Test
	public void test_createOrders_withoutProducts() {
		List<OrderedProduct> list = new ArrayList<>();
		OrderDTO dto = new OrderDTO();
		dto.setOrderedProducts(list);
		dto.setCreatedTime(LocalDateTime.now());
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(dto)
			.when()
			.post("/api/orders")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body(containsString("must not be"))
			;
	}
	
	@Test
	public void test_createOrders_withoutCreatedTime() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setPrice(BigDecimal.valueOf(800.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		OrderDTO dto = new OrderDTO();
		dto.setOrderedProducts(list);
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(dto)
			.when()
			.post("/api/products")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body(containsString("must not be null"))
			;
	}
	
	@Test
	public void test_getOrdersDTO_OK() {
        get("/api/orders")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("content.size()", greaterThan(0))
			.body(containsString("TV"))
			.body(containsString("Samsung"))
			.body("orderedProducts.size()", greaterThan(0))
			;
	}

	@Test
	public void test_getOrders_OK() {
		get("/api/orders/details")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("content.size()", greaterThan(0))
			.body(containsString("TV"))
			.body(containsString("Samsung"))
			.body("orderedProducts.size()", greaterThan(0))
			;
	}

}