package com.fussentech.shopcustomers.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.fussentech.shopcustomers.model.User;

@DataJpaTest
public class UserRepositoryTests {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void setup() {
		User user = new User();
		user.setFirstname("Foo");
		user.setLastname("Bar");
		user.setEmail("foo.bar@example.com");
		
		userRepository.save(user);
	}
	
	@Test
	public void testFindByEmail() {
		User user = new User();
		user.setFirstname("Tom");
		user.setLastname("Smith");
		user.setEmail("foo@example.com");
		
		testEntityManager.persist(user);
		testEntityManager.flush();
		
		Optional<User> op = userRepository.findByEmail(user.getEmail());
		User user2 = op.get();
		Assertions.assertThat(user2.getEmail())
			.isEqualTo(user.getEmail());
		Assertions.assertThat(user2.getId())
			.isNotNull();
	}
	
	@Test
	public void testFindBy() {
		String email = "foo.bar@example.com";
		Optional<User> op = userRepository.findByEmail(email);
		User user = op.get();
		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals(user.getEmail(), email);
	}
	
	@Test
	public void testSaveUserWithDefaultConstructor() {
		User user = new User();
		Exception e = assertThrows(ConstraintViolationException.class, () -> {
			userRepository.save(user);
		});
		assertTrue(e.getMessage().contains("Validation failed"));
		assertTrue(e.getMessage().contains("must not be null"));
	}
	
	@Test
	public void testSaveUserWithFirstnameOnly() {
		User user = new User();
		user.setFirstname("Foo");
		Assertions.assertThatThrownBy(() -> {
			userRepository.save(user);
		})
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("must not be null")
		;
	}
	@Test
	public void testSaveUserWithLastnameOnly() {
		User user = new User();
		user.setLastname("Bar");
		Assertions.assertThatThrownBy(() -> {
			userRepository.save(user);
		})
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("must not be null")
		;
	}
	@Test
	public void testSaveUserWithEmailOnly() {
		User user = new User();
		user.setEmail("dummy@example.com");
		Assertions.assertThatThrownBy(() -> {
			userRepository.save(user);
		})
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("must not be null")
		;
	}
}
