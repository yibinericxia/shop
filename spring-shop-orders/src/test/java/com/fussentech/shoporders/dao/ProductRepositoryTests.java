package com.fussentech.shoporders.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import com.fussentech.shoporders.dao.ProductRepository;
import com.fussentech.shoporders.model.Product;

// Use the real profile Mongo DB for this testing
@ActiveProfiles("test")
@DataMongoTest
class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;
	
	private void saveProduct(Long id, String name, String maker, BigDecimal price) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setMaker(maker);
		product.setPrice(price);
		
		repo.save(product);
	}
	
	@BeforeEach
	public void setup() {
		saveProduct(100L, "foo", "Bar", BigDecimal.valueOf(30.0));
		saveProduct(200L, "abc", "Bar", BigDecimal.valueOf(50.0));
	}

	@Test
	public void test_findAll_OK() {
		List<Product> list = repo.findAll();
		Assertions.assertThat(list.size()).isGreaterThan(1);
	}
	
	@Test
	public void test_findAllByMaker() {
		String maker = "Bar";
		List<Product> list = repo.findAllByMaker(maker);
		Assertions.assertThat(list.size()).isEqualTo(2);
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
		
		Exception e = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			repo.save(product);
		});
		assertTrue(e.getMessage().contains("Cannot"));
	}
	
	@Test
	public void test_save_WithMakerOnly() {
		Product product = new Product();
		product.setMaker("Dummy");
		
		Assertions.assertThatThrownBy(() -> {
			repo.save(product);
		})
		.isInstanceOf(InvalidDataAccessApiUsageException.class)
		.hasMessageContaining("Cannot")
		;
	}
}
