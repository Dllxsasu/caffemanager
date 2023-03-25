package com.jeremias.dev.exceptions;

public class appException  extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public appException(String message) {
		super(message);
	}

	public appException(String message, Throwable th) {
		super(message, th);
	}
}
