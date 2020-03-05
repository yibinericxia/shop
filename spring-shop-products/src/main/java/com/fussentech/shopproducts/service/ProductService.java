package com.fussentech.shopproducts.service;

import java.util.List;
import java.util.Optional;

import com.fussentech.shopproducts.model.Product;

public interface ProductService {

	List<Product> findAll();
	List<Product> findPaginated(int page, int size);
	List<Product> findPaginated(int page, int size, String direction, String field);
	List<Product> findAllByMaker(String maker);
	Optional<Product> findById(Long id);
	Product save(Product product);
	Optional<Product> update(Product product);
	void delete(Product product);

}
