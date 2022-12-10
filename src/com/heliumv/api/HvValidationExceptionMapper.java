package com.heliumv.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class HvValidationExceptionMapper implements
		ExceptionMapper<HvValidationException> {

	@Override
	public Response toResponse(HvValidationException arg0) {
		return arg0.toResponse() ;
//		return null ;
//		ResponseBuilderImpl r = new ResponseBuilderImpl() ;
//		return r.build() ;
	}

}
