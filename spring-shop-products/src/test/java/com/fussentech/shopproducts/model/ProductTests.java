package com.fussentech.shopproducts.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ProductTests {

	@Test
	void test_equals_OK() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setName("abc");
		p1.setMaker("xyz");
		p1.setCategory(Arrays.asList("foo", "bar"));
		p1.setDescription("foo");
		p1.setPrice(BigDecimal.valueOf(10.55));
		p1.setImageUrl("/path/to/image");

		Product p2 = new Product();
		p2.setId(p1.getId());
		p2.setName(p1.getName());
		p2.setMaker(p1.getMaker());
		p2.setCategory(p1.getCategory());
		p2.setDescription(p1.getDescription());
		p2.setPrice(p1.getPrice());
		p2.setImageUrl(p1.getImageUrl());
		
		assertEquals(p1, p2);
		assertEquals(p1.hashCode(), p2.hashCode());
	}

	@Test
	void test_equals_withDifferentId() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setName("abc");
		p1.setMaker("xyz");
		p1.setCategory(Arrays.asList("foo", "bar"));
		p1.setDescription("foo");
		p1.setPrice(BigDecimal.valueOf(10.55));
		p1.setImageUrl("/path/to/image");

		Product p2 = new Product();
		p2.setId(2L);
		p2.setName(p1.getName());
		p2.setMaker(p1.getMaker());
		p2.setCategory(p1.getCategory());
		p2.setDescription(p1.getDescription());
		p2.setPrice(p1.getPrice());
		p2.setImageUrl(p1.getImageUrl());
		
		assertNotEquals(p1, p2);
		assertNotEquals(p1.hashCode(), p2.hashCode());
	}

	@Test
	void test_equals_withDifferentImageUrl() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setName("abc");
		p1.setMaker("xyz");
		p1.setCategory(Arrays.asList("foo", "bar"));
		p1.setDescription("foo");
		p1.setPrice(BigDecimal.valueOf(10.55));
		p1.setImageUrl("/path/to/image");

		Product p2 = new Product();
		p2.setId(p1.getId());
		p2.setName(p1.getName());
		p2.setMaker(p1.getMaker());
		p2.setCategory(p1.getCategory());
		p2.setDescription(p1.getDescription());
		p2.setPrice(p1.getPrice());
		p2.setImageUrl("/path");
		
		assertNotEquals(p1, p2);
		assertEquals(p1.hashCode(), p2.hashCode());
	}

}
