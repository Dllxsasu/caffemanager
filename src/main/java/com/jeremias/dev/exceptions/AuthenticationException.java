package com.jeremias.dev.exceptions;

public class AuthenticationException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable th) {
		super(message, th);
	}
}
