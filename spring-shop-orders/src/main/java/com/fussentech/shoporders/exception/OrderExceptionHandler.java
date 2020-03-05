package com.fussentech.shoporders.exception;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(OrderExceptionHandler.class);

	@ExceptionHandler({ OrderException.class })
	public ResponseEntity<ErrorResponse> handle(OrderException ex, WebRequest req) {
		Instant time = Instant.now();
		ErrorResponse res = 
			new ErrorResponse(ex.getMessage(), req.getDescription(false), time);
		logger.error(res.toString());
		return new ResponseEntity<>(res, ex.getStatusCode());
	}

	static class ErrorResponse {
		private String message;
		private String requestInfo;
		private Instant timestamp;
		
		public ErrorResponse(String msg, String reqInfo, Instant time) {
			this.message = msg;
			this.requestInfo = reqInfo;
			this.timestamp = time;
		}

		public String getMessage() {
			return message;
		}

		public String getRequestInfo() {
			return requestInfo;
		}

		public Instant getTimestamp() {
			return timestamp;
		}
		
		@Override
		public String toString() {
			return timestamp + " Error: " + message + " [" + requestInfo + "]";
		}
	}
}
