package com.heliumv.api;

public class HvThrowableException extends HvApiException {
	private static final long serialVersionUID = 1018499556218274412L;

	public HvThrowableException(Throwable t) {
		super(t) ;
	}
}
