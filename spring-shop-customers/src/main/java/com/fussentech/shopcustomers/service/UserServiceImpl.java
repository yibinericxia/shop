package com.fussentech.shopcustomers.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shopcustomers.dao.UserRepository;
import com.fussentech.shopcustomers.exception.UserException;
import com.fussentech.shopcustomers.model.User;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private UserRepository repo;
	
	private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Value("${spring.kafka.topic.user-update}")
	private String TOPIC_UPDATE;
	
	@Value("${spring.kafka.topic.user-creation}")
	private String TOPIC_CREATE;
	
	@Value("${spring.kafka.topic.user-deletion}")
	private String TOPIC_DELETE;

	@Value("${test.mode.rest_only}")
	private boolean testRestOnly;
	
	public UserServiceImpl(UserRepository repo, 
			KafkaTemplate<Long, String> kafkaTemplate) {
		this.repo = repo;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@Override
	public List<User> getUsers() {
		return repo.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return repo.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user) {
		User saved = repo.save(user);
		// id may be auto generated
		user.setId(saved.getId());
		if (saved.equals(user)) {
			raiseUserCreationEvent(user);
		}
		return saved;
	}

	@Override
	@Transactional
	public Optional<User> updateUser(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (op.isPresent()) {
			User u = op.get();
			BeanUtils.copyProperties(user, u);		
			raiseUserUpdateEvent(user);
			return Optional.of(u);
		};
		return Optional.empty();
	}

	@Override
	public void deleteUser(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.BAD_REQUEST, "id " + user.getId() + " not found");
		} 
		User u = op.get();
		if (u.equals(user)) {
			repo.delete(user);			
			raiseUserDeletionEvent(user);
		}		
	}

	private void raiseUserCreationEvent(User user) {
		if (testRestOnly) {
			return;
		}
		Long id = user.getId();
		logger.info("kafka sending user with id='{}' to topic='{}'", id, TOPIC_CREATE);
		try {
			String value = OBJECT_MAPPER.writeValueAsString(user);
			kafkaTemplate.send(TOPIC_CREATE, id, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void raiseUserUpdateEvent(User user) {
		if (testRestOnly) {
			return;
		}
		Long id = user.getId();
		logger.info("kafka sending user with id='{}' to topic='{}'", id, TOPIC_UPDATE);
		try {
			String value = OBJECT_MAPPER.writeValueAsString(user);
			kafkaTemplate.send(TOPIC_UPDATE, id, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void raiseUserDeletionEvent(User user) {
		if (testRestOnly) {
			return;
		}
		Long id = user.getId();
		logger.info("kafka sending user with id='{}' to topic='{}'", id, TOPIC_DELETE);
		try {
			String value = OBJECT_MAPPER.writeValueAsString(user);
			kafkaTemplate.send(TOPIC_DELETE, id, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
