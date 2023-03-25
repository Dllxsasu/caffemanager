package com.jeremias.dev.exceptions;

public class JwtTokenValidationException extends RuntimeException {
	public JwtTokenValidationException(String message) {
		super(message);
	}
	
	public JwtTokenValidationException(String message, Throwable th) {
		super(message, th);
	}

}
