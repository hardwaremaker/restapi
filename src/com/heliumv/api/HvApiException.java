package com.heliumv.api;

public class HvApiException extends RuntimeException {
	private static final long serialVersionUID = -3358012047810659866L;
	
	protected HvApiException() {
		super();
	}

	protected HvApiException(String message) {
		super(message) ;
	}

	protected HvApiException(Throwable t) {
		super(t) ;
 	}
		
	protected HvApiException(String message, Throwable t) {
		super(message, t) ;
	}	
}
