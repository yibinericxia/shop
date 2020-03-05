package com.fussentech.shoporders.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTests {

	@Test
	void test_equals_OK() {
		User user1 = new User();
		user1.setId(1L);
		user1.setFirstname("John");
		user1.setLastname("Smith");
		user1.setEmail("john.smith@example.com");
		user1.setTimeZone("America/Phoenix");
		
		User user2 = new User();
		user2.setId(user1.getId());
		user2.setFirstname(user1.getFirstname());
		user2.setLastname(user1.getLastname());
		user2.setEmail(user1.getEmail());
		user2.setTimeZone(user1.getTimeZone());
		
		assertEquals(user1, user2);
		assertEquals(user1.hashCode(), user2.hashCode());
	}

	@Test
	void test_equals_withDifferentTimeZone() {
		User user1 = new User();
		user1.setId(1L);
		user1.setFirstname("John");
		user1.setLastname("Smith");
		user1.setEmail("john.smith@example.com");
		user1.setTimeZone("America/Phoenix");
		
		User user2 = new User();
		user2.setId(user1.getId());
		user2.setFirstname(user1.getFirstname());
		user2.setLastname(user1.getLastname());
		user2.setEmail(user1.getEmail());
		user2.setTimeZone("America/New York");
		
		assertEquals(user1, user2);
		assertEquals(user1.hashCode(), user2.hashCode());
	}

	@Test
	void test_equals_withDifferentId() {
		User user1 = new User();
		user1.setId(1L);
		user1.setFirstname("John");
		user1.setLastname("Smith");
		user1.setEmail("john.smith@example.com");
		user1.setTimeZone("America/Phoenix");
		
		User user2 = new User();
		user2.setId(2L);
		user2.setFirstname(user1.getFirstname());
		user2.setLastname(user1.getLastname());
		user2.setEmail(user1.getEmail());
		user2.setTimeZone(user1.getTimeZone());
		
		assertNotEquals(user1, user2);
		assertNotEquals(user1.hashCode(), user2.hashCode());
	}

}
