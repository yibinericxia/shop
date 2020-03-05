package com.fussentech.shopproducts.service;

import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shopproducts.dao.ProductRepository;
import com.fussentech.shopproducts.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private ProductRepository repo;

	private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Value("${spring.kafka.topic.product-update}")
	private String TOPIC_UPDATE;
	
	@Value("${spring.kafka.topic.product-creation}")
	private String TOPIC_CREATE;
	
	@Value("${spring.kafka.topic.product-deletion}")
	private String TOPIC_DELETE;

	@Value("${test.mode.rest_only}")
	private boolean testRestOnly;
	
	public ProductServiceImpl(ProductRepository repo, 
			KafkaTemplate<Long, String> kafkaTemplate) {
		this.repo = repo;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@Override
	public List<Product> findAll() {
		return repo.findAll();
	}
	
	@Override
	public List<Product> findPaginated(int page, int size) {
		Page<Product> p = repo.findAll(PageRequest.of(page, size));
		return p.getContent();
	}
	
	@Override
	public List<Product> findPaginated(int page, int size, String direction, String field) {
		Direction dir = Sort.Direction.fromString(direction);
		Page<Product> p = repo.findAll(PageRequest.of(page, size, dir, field));
		return p.getContent();
	}
	
	@Override
	public List<Product> findAllByMaker(String maker) {
		return repo.findAllByMaker(maker);
		/*
		return findAll().stream()
				.filter(product -> product.getMaker().equals(maker))
				.collect(Collectors.toList());
		*/
	}

	@Override
	public Optional<Product> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	@Transactional
	public Product save(Product product) {
		Product saved = repo.save(product);
		if (saved.equals(product)) {
			raiseProductCreationEvent(product);
		}
		return saved;
	}

	@Override
	@Transactional
	public Optional<Product> update(Product product) {
		Optional<Product> op = repo.findById(product.getId());
		if (op.isPresent()) {
			Product p = op.get();
			BeanUtils.copyProperties(product, p);		
			raiseProductUpdateEvent(product);
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
				raiseProductDeletionEvent(product);
			}
		}
	}

	private void raiseProductCreationEvent(Product product) {
		if (testRestOnly) {
			return;
		}
		logger.info("kafka sending the product {} to topic {}", product, TOPIC_CREATE);
		try {
			String value = OBJECT_MAPPER.writeValueAsString(product);
			kafkaTemplate.send(TOPIC_CREATE, product.getId(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void raiseProductUpdateEvent(Product product) {
		if (testRestOnly) {
			return;
		}
		logger.info("kafka sending the product {} to topic {}", product, TOPIC_UPDATE);
		try {
			String value = OBJECT_MAPPER.writeValueAsString(product);
			kafkaTemplate.send(TOPIC_UPDATE, product.getId(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void raiseProductDeletionEvent(Product product) {
		if (testRestOnly) {
			return;
		}
		try {
			String value = OBJECT_MAPPER.writeValueAsString(product);
			kafkaTemplate.send(TOPIC_DELETE, product.getId(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
