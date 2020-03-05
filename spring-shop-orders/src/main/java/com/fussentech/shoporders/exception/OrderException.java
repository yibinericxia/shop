package com.fussentech.shoporders.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class OrderException extends HttpStatusCodeException {

	private static final long serialVersionUID = 6448045843871825939L;

	public OrderException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

}
