package com.jeremias.dev.exceptions;

public class AccessUnauthorizedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccessUnauthorizedException(String message) {
		super(message);
	}
}
