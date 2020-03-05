package com.fussentech.shopnotification.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	@Value("${mail.host}")
	private String host;
	@Value("${mail.port}")
	private int port;
	@Value("${mail.protocol}")
	private String protocol;
	@Value("${mail.smtp.auth}")
	private boolean auth;
	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;
	@Value("${mail.username}")
	private String username;
	@Value("${mail.password}")
	private String password;
	
	@Bean
	public JavaMailSender sender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties props = new Properties();
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.starttls.enable", starttls);
		props.put("mail.smtp.debug", "true");
		mailSender.setJavaMailProperties(props);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		return mailSender;
	}
	
}
