package com.fussentech.shoporders.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class OrderTests {

	@Test
	void test_equals_OK() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setMaker("Samsung");
		product.setCategory(Arrays.asList("dummy"));
		product.setDescription("foo");
		product.setPrice(BigDecimal.valueOf(800.0));
		product.setImageUrl("/path/to/image");
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		
		Order order1 = new Order();
		order1.setStatus(OrderStatus.CREATED);
		order1.setOrderedProducts(list);
		order1.setCreatedTime(LocalDateTime.now());


		Order order2 = new Order();
		order2.setStatus(order1.getStatus());
		order2.setOrderedProducts(order1.getOrderedProducts());
		order2.setCreatedTime(order1.getCreatedTime());

		assertEquals(order1, order2);
		assertEquals(order1.hashCode(), order2.hashCode());
	}

	@Test
	void test_equals_withDifferentId() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setMaker("Samsung");
		product.setCategory(Arrays.asList("dummy"));
		product.setDescription("foo");
		product.setPrice(BigDecimal.valueOf(800.0));
		product.setImageUrl("/path/to/image");
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		
		Order order1 = new Order();
		order1.setId("1");
		order1.setStatus(OrderStatus.CREATED);
		order1.setOrderedProducts(list);
		order1.setCreatedTime(LocalDateTime.now());


		Order order2 = new Order();
		order2.setId("2");
		order2.setStatus(order1.getStatus());
		order2.setOrderedProducts(order1.getOrderedProducts());
		order2.setCreatedTime(order1.getCreatedTime());

		assertNotEquals(order1, order2);
		assertNotEquals(order1.hashCode(), order2.hashCode());
	}

	@Test
	void test_equals_withDifferentTime() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setMaker("Samsung");
		product.setCategory(Arrays.asList("dummy"));
		product.setDescription("foo");
		product.setPrice(BigDecimal.valueOf(800.0));
		product.setImageUrl("/path/to/image");
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		
		Order order1 = new Order();
		order1.setId("1");
		order1.setStatus(OrderStatus.CREATED);
		order1.setOrderedProducts(list);
		order1.setCreatedTime(LocalDateTime.now());

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Order order2 = new Order();
		order2.setId(order1.getId());
		order2.setStatus(order1.getStatus());
		order2.setOrderedProducts(order1.getOrderedProducts());
		order2.setCreatedTime(LocalDateTime.now());

		assertNotEquals(order1, order2);
		assertNotEquals(order1.hashCode(), order2.hashCode());
	}

}
