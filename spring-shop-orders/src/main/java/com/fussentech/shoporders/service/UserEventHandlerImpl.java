package com.fussentech.shoporders.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shoporders.dao.UserRepository;
import com.fussentech.shoporders.model.User;

@Service
public class UserEventHandlerImpl implements UserEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(UserEventHandlerImpl.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Value("${spring.kafka.topic.user-update}")
	private String TOPIC_UPDATE;
	
	@Value("${spring.kafka.topic.user-creation}")
	private String TOPIC_CREATE;
	
	@Value("${spring.kafka.topic.user-deletion}")
	private String TOPIC_DELETE;

	private UserRepository repo;
	
	public UserEventHandlerImpl(UserRepository repo) {
		this.repo = repo;
	}
	@Override
	@KafkaListener(topics = "${spring.kafka.topic.user-creation}")
	public void handleUserCreation(String userStr) {
		try {
			User user = OBJECT_MAPPER.readValue(userStr, User.class);
			saveUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = "${spring.kafka.topic.user-update}")
	public void handleUserUpdate(String userStr) {
		try {
			User user = OBJECT_MAPPER.readValue(userStr, User.class);
			updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = "${spring.kafka.topic.user-deletion}")
	public void handleUserDeletion(String userStr) {
		try {
			User user = OBJECT_MAPPER.readValue(userStr, User.class);
			deleteUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	private void saveUser(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (!op.isPresent()) {
			logger.info("kafka consuming user from topic {}", TOPIC_CREATE);
			repo.save(user);
		}
	}

	@Transactional
	private void updateUser(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (op.isPresent()) {
			logger.info("kafka consuming user from topic {}", TOPIC_UPDATE);
			User u = op.get();
			repo.delete(u);
			repo.save(user);
		}
	}

	@Transactional
	private void deleteUser(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (op.isPresent()) {
			logger.info("kafka consuming user from topic {}", TOPIC_DELETE);
			User u = op.get();
			if (u.equals(user)) {
				repo.delete(u);
			}
		}
	}

}
