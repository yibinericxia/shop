package com.fussentech.shopcustomers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UserException extends HttpStatusCodeException {

	private static final long serialVersionUID = -5323117677173845914L;

	public UserException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

}
