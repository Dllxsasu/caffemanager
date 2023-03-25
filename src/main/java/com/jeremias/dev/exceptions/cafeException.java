package com.jeremias.dev.exceptions;

public class cafeException extends RuntimeException{
	 	
		public cafeException(String exMessage, Exception exception) {
	        super(exMessage, exception);
	    }

	    public cafeException(String exMessage) {
	        super(exMessage);
	    }
}
