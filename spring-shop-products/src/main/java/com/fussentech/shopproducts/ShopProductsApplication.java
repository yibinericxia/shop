package com.fussentech.shopproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.fussentech.shopproducts.model.Product;
import com.fussentech.shopproducts.service.ProductService;
*/
@SpringBootApplication
public class ShopProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopProductsApplication.class, args);
	}
	/*
	@Bean
	CommandLineRunner runner(ProductService productService) {
		return args -> {
			productService.save(new Product("TV", BigDecimal.valueOf(500), "Samsung"));
			productService.save(new Product("Sofa", BigDecimal.valueOf(1500), "Costco"));
			productService.save(new Product("iPhone", BigDecimal.valueOf(800), "apple"));
			productService.save(new Product("Laptop", BigDecimal.valueOf(700), "HP"));
			productService.save(new Product("watch", BigDecimal.valueOf(100), "seiko"));
			productService.save(new Product("Beer", BigDecimal.valueOf(9), "Germany"));
		};
	}
	*/
}
