package com.fussentech.shopproducts.controller;

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

import com.fussentech.shopproducts.exception.ProductException;
import com.fussentech.shopproducts.model.Product;
import com.fussentech.shopproducts.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	private ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts(
			@RequestParam (required = false) Integer page, 
			@RequestParam (required = false) Integer size,
			@RequestParam (required = false) String direction,
			@RequestParam (required = false) String sort) {
		List<Product> list = null;
		if (page == null && size == null) {
			list = productService.findAll();
		} else if (page != null && size != null
				&& direction == null && sort == null) {
			list = productService.findPaginated(page, size);
			
		} else {
			list = productService.findPaginated(page, size, direction, sort);			
		}
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/products/maker/{maker}")
	public ResponseEntity<List<Product>> getProductsByMaker(@PathVariable String maker) {
		List<Product> list = productService.findAllByMaker(maker);
		return ResponseEntity.ok(list);
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
	public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws Exception {
		Optional<Product> op = productService.findById(product.getId());
		if (op.isPresent()) {
			throw new ProductException(HttpStatus.CONFLICT, "id " + product.getId() + " already exists");
		}
		Product saved = productService.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);	
	}
	
	@PutMapping("/products")
	public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) {
		Optional<Product> op = productService.update(product);
		if (!op.isPresent()) {
			throw new ProductException(HttpStatus.NOT_FOUND, "product not found");
		}
		Product updatedProduct = op.get();
		return ResponseEntity.ok(updatedProduct);
	}
	
	@DeleteMapping("/products")
	public ResponseEntity<Void> deleteProduct(@RequestBody Product product) {
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
