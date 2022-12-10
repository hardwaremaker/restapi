package com.heliumv.api;

import javax.naming.NamingException;

public class HvNamingException extends HvApiException {
	private static final long serialVersionUID = 1018499556218274412L;

	public HvNamingException(NamingException e) {
		super(e) ;
	}
}
