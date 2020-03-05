package com.fussentech.shoporders.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UserException extends HttpStatusCodeException {

	private static final long serialVersionUID = 8094358578939923961L;

	public UserException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

}
