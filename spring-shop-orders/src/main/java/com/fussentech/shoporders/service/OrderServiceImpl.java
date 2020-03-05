package com.fussentech.shoporders.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shoporders.dao.OrderRepository;
import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.User;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private OrderRepository repo;
	
	private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Value("${spring.kafka.topic.order-creation}")
	private String TOPIC_CREATE;

	@Value("${test.mode.rest_only}")
	private boolean testRestOnly;
	
	public OrderServiceImpl(OrderRepository repo, 
			KafkaTemplate<Long, String> kafkaTemplate) {
		this.repo = repo;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@Override
	public List<Order> findAll() {
		return repo.findAll();
	}

	@Override
	public Optional<Order> findById(String id) {
		return repo.findById(id);
	}

	@Override
	@Transactional
	public Order save(Order order) {
		logger.info("received order: " + order);
		
		Order savedOrder = repo.save(order);
		raiseOrderCreationEvent(order);
		return savedOrder;
	}

	private void raiseOrderCreationEvent(Order order) {
		if (testRestOnly) {
			return;
		}
		User user = order.getUser();
		
		user = new User();
		user.setEmail("foo@example.com");
		
		if (user == null || user.getEmail() == null) {
			return;
		}
		logger.info("kafka sending order to topic {}", TOPIC_CREATE);
		try {
			String value = OBJECT_MAPPER.writeValueAsString(user);
			kafkaTemplate.send(TOPIC_CREATE, user.getId(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
