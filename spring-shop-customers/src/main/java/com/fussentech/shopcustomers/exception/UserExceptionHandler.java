package com.fussentech.shopcustomers.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ UserException.class })
	public ResponseEntity<ErrorResponse> handle(UserException ex, WebRequest request) {
		Instant time = Instant.now();
		ErrorResponse msg = 
			new ErrorResponse(ex.getMessage(), request.getDescription(false), time);
		return new ResponseEntity<>(msg, ex.getStatusCode());
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
