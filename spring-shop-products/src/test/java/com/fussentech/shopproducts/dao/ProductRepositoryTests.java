package com.fussentech.shopproducts.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.fussentech.shopproducts.dao.ProductRepository;
import com.fussentech.shopproducts.model.Product;

@DataJpaTest
class ProductRepositoryTests {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private ProductRepository repo;
	
	@BeforeEach
	public void setup() {
		Product product = new Product();
		product.setName("Foo");
		product.setMaker("Bar");
		product.setPrice(BigDecimal.valueOf(10.0));
		
		repo.save(product);
	}

	@Test
	public void test_findAllByMaker() {
		Product product = new Product();
		product.setName("abc");
		product.setMaker("Bar");
		product.setPrice(BigDecimal.valueOf(50.0));
		
		testEntityManager.persist(product);
		testEntityManager.flush();
		
		List<Product> list = repo.findAllByMaker(product.getMaker());
		Assertions.assertThat(list.size())
			.isEqualTo(2);
		Assertions.assertThat(list)
			.isNotNull();
	}
	
	@Test
	public void test_findById() {
		Long id = 1L;
		Optional<Product> op = repo.findById(id);
		Product product = op.get();
		assertNotNull(product);
		assertNotNull(product.getName());
		assertEquals(product.getId(), id);
	}
	
	@Test
	public void test_save_WithDefaultConstructor() {
		Product product = new Product();
		
		Exception e = assertThrows(ConstraintViolationException.class, () -> {
			repo.save(product);
		});
		assertTrue(e.getMessage().contains("Validation failed"));
		assertTrue(e.getMessage().contains("must not be null"));
	}
	
	@Test
	public void test_save_WithMakerOnly() {
		Product product = new Product();
		product.setMaker("Dummy");
		
		Assertions.assertThatThrownBy(() -> {
			repo.save(product);
		})
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("must not be null")
		;
	}
}
