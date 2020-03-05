package com.fussentech.shoporders.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shoporders.dao.ProductRepository;
import com.fussentech.shoporders.model.Product;

@Service
public class ProductEventHandlerImpl implements ProductEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(ProductEventHandlerImpl.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private ProductRepository repo;
	
	@Value("${spring.kafka.topic.product-update}")
	private String TOPIC_UPDATE;
	
	@Value("${spring.kafka.topic.product-creation}")
	private String TOPIC_CREATE;
	
	@Value("${spring.kafka.topic.product-deletion}")
	private String TOPIC_DELETE;

	public ProductEventHandlerImpl(ProductRepository repo) {
		this.repo = repo;
	}
	
	@Override
	@KafkaListener(topics = "${spring.kafka.topic.product-creation}")
	public void handleProductCreation(String productStr) {
		try {
			Product product = OBJECT_MAPPER.readValue(productStr, Product.class);
			saveProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = "${spring.kafka.topic.product-update}")
	public void handleProductUpdate(String productStr) {
		try {
			Product product = OBJECT_MAPPER.readValue(productStr, Product.class);
			updateProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = "${spring.kafka.topic.product-update}")
	public void handleProductDeletion(String productStr) {
		try {
			Product product = OBJECT_MAPPER.readValue(productStr, Product.class);
			deleteProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	private void saveProduct(Product product) {
		Optional<Product> op = repo.findById(product.getId());
		if (!op.isPresent()) {
			logger.info("Kafka consuming product from topic {}", TOPIC_CREATE);
			repo.save(product);
		}
	}
	
	@Transactional
	private void updateProduct(Product product) {
		Optional<Product> op = repo.findById(product.getId());
		if (op.isPresent()) {
			logger.info("Kafka consuming product from topic {}", TOPIC_UPDATE);
			Product p = op.get();
			repo.delete(p);
			repo.save(product);
		}
	}

	@Transactional
	private void deleteProduct(Product product) {
		Optional<Product> op = repo.findById(product.getId());
		if (op.isPresent()) {
			logger.info("Kafka consuming product from topic {}", TOPIC_DELETE);
			Product p = op.get();
			if (p.equals(product)) {
				repo.delete(p);
			}
		}
	}

}
