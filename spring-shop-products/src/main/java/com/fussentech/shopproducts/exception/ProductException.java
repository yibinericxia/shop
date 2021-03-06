package com.fussentech.shopproducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductException extends HttpStatusCodeException {

	private static final long serialVersionUID = 6448045843871825939L;

	public ProductException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

}
