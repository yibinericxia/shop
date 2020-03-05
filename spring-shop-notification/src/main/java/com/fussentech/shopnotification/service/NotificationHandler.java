package com.fussentech.shopnotification.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fussentech.shopnotification.model.User;

@Service
public class NotificationHandler {

	private static final Logger logger = LoggerFactory.getLogger(NotificationHandler.class);

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private JavaMailSender sender;
	
	@Value("${spring.kafka.topic.user-creation}")
	private String TOPIC_USER_CREATION;
	
	@Value("${spring.kafka.topic.order-creation}")
	private String TOPIC_ORDER_CREATION;

	public NotificationHandler(JavaMailSender sender) {
		this.sender = sender;
	}
	
	@KafkaListener(topics = "${spring.kafka.topic.user-creation}")
	public void handleUserCreation(String userStr) {
		logger.info("kafka consuming user from topic {}", TOPIC_USER_CREATION);
		try {
			User user = OBJECT_MAPPER.readValue(userStr, User.class);
			String to = user.getEmail();
			String subject = "Welcome";
			String body = "Thank you for registration";
			
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true);
			msgHelper.setTo(to);
			msgHelper.setSubject(subject);
			msgHelper.setText(body);
			sender.send(msg);
			logger.info("sent an email to {}", to);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@KafkaListener(topics = "${spring.kafka.topic.order-creation}")
	public void handleOrderCreation(String userStr) {
		logger.info("kafka consuming user from topic {}", TOPIC_ORDER_CREATION);
		try {
			User user = OBJECT_MAPPER.readValue(userStr, User.class);
			String to = user.getEmail();
			String subject = "Your order";
			String body = "Thank you for your purchase";
			
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true);
			msgHelper.setTo(to);
			msgHelper.setSubject(subject);
			msgHelper.setText(body);
			sender.send(msg);
			logger.info("sent an email to {}", to);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
