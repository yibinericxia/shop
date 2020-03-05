package com.fussentech.shoporders.service;

import java.util.List;
import java.util.Optional;

import com.fussentech.shoporders.model.Product;

public interface ProductService {

	List<Product> findAll();
	List<Product> findAllByMaker(String maker);
	Optional<Product> findById(Long id);
	Product save(Product product);
	Optional<Product> update(Product product);
	void delete(Product product);
}
