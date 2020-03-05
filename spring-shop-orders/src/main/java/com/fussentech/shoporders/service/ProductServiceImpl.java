package com.fussentech.shoporders.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fussentech.shoporders.dao.ProductRepository;
import com.fussentech.shoporders.model.Product;

@Repository
public class ProductServiceImpl implements ProductService {

	private ProductRepository repo;
	
	public ProductServiceImpl(ProductRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<Product> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Product> findAllByMaker(String maker) {
		return repo.findAllByMaker(maker);
	}

	@Override
	public Optional<Product> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Product save(Product product) {
		Product saved = repo.save(product);
		if (product.equals(saved)) {
			//return product;
		}
		return saved;
	}

	@Override
	public Optional<Product> update(Product product) {
		Optional<Product> op = repo.findById(product.getId());
		if (op.isPresent()) {
			Product p = op.get();
			repo.delete(p);
			p = product;
			repo.save(p);
			return Optional.of(p);
		}
		return Optional.empty();
	}

	@Override
	public void delete(Product product) {
		Optional<Product> op = repo.findById(product.getId());
		if (op.isPresent()) {
			Product p = op.get();
			if (p.equals(product)) {
				repo.delete(product);
			}
		}
	}

}
