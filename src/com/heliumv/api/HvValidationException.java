package com.heliumv.api;

import javax.ws.rs.core.Response;

public abstract class HvValidationException extends RuntimeException {
	private static final long serialVersionUID = 8983496926600284971L;

	public abstract Response toResponse() ;	
}
