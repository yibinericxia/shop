package com.fussentech.shoporders.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fussentech.shoporders.exception.ProductException;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	private ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProducts(
			@RequestParam(required = false) List<String> maker,
			@RequestParam(required = false) List<String> category) {
		List<Product> products = null;
		if ((maker == null || maker.isEmpty()) 
			&& (category == null || category.isEmpty())) {
			products = productService.findAll();
		} else {
			products = new ArrayList<>();
			if (maker != null) {
				for (String s: maker) {
					if (s == null) {
						continue;
					}
					products.addAll(productService.findAllByMaker(s));
				}
			}
		}
		return ResponseEntity.ok(products);
	}
	
	@GetMapping("/products/id/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Optional<Product> op = productService.findById(id);
		if (!op.isPresent()) {
			throw new ProductException(HttpStatus.NOT_FOUND, "id " + id + " not found");
		}
		Product product = op.get();
		return ResponseEntity.ok(product);
	}
	
	@PostMapping("/products")
	public ResponseEntity<Product> save(@Valid @RequestBody Product product) {
		Optional<Product> op = productService.findById(product.getId());
		if (op.isPresent()) {
			throw new ProductException(HttpStatus.CONFLICT, "id " + product.getId() + " already exists");
		}
		Product saved = productService.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/products")
	public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
		Optional<Product> op = productService.update(product);
		if (!op.isPresent()) {
			throw new ProductException(HttpStatus.NOT_FOUND, "product not found");
		}
		Product updated = op.get();
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/products")
	public ResponseEntity<Void> delete(@RequestBody Product product) {
		Optional<Product> op = productService.findById(product.getId());
		if (!op.isPresent()) {
			throw new ProductException(HttpStatus.NOT_FOUND, "product not found");
		}
		productService.delete(product);
		return ResponseEntity.noContent().build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	private Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> map = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String field = ((FieldError) error).getField();
			String msg = error.getDefaultMessage();
			map.put(field, msg);
		});
		return map;
	}
}
