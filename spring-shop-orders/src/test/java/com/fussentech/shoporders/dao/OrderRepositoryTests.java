package com.fussentech.shoporders.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.fussentech.shoporders.dao.OrderRepository;
import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.OrderedProduct;
import com.fussentech.shoporders.model.Product;

//Use the real profile Mongo DB for this testing
@ActiveProfiles("test")
@DataMongoTest
public class OrderRepositoryTests {
	
	@Autowired
	private OrderRepository repo;
	
	@BeforeEach
	public void setup() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Sofa");
		product.setPrice(BigDecimal.valueOf(500.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		Order order = new Order();
		order.setId("1");
		order.setCreatedTime(LocalDateTime.now());
		order.setOrderedProducts(list);
		
		repo.save(order);
	}
	
	@Test
	public void test_getOrders_OK() {
		List<Order> list = repo.findAll();
		Assertions.assertThat(list.size()).isGreaterThan(0);
	}
	
	@Test
	public void test_save_WithDefaultConstructor() {
		Order order = new Order();
		Assertions.assertThat(order=repo.save(order))
			.isInstanceOf(Order.class)
			;
		
		repo.delete(order);
	}
}
