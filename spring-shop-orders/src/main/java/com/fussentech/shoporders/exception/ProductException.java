package com.fussentech.shoporders.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductException extends HttpStatusCodeException {

	private static final long serialVersionUID = 319672250448120555L;

	public ProductException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

}
