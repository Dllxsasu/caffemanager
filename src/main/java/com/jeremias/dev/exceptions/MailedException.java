package com.jeremias.dev.exceptions;

public class MailedException extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	public MailedException(String message) {
		super(message);
	}
}
