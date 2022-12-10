package com.heliumv.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heliumv.api.BaseApi.HvErrorCode;

public class HvThrowableExceptionMapper implements
		ExceptionMapper<Throwable> {
	private static Logger log = LoggerFactory.getLogger(HvThrowableExceptionMapper.class) ;

	@Override
	public Response toResponse(Throwable e) {
		log.error("default-log", e);
		return new ResponseBuilderImpl()
			.header(BaseApi.X_HV_ERROR_CODE, HvErrorCode.THROWABLE_EXCEPTION.toString())
			.header(BaseApi.X_HV_ERROR_CODE_DESCRIPTION, e.getMessage())
			.status(Response.Status.INTERNAL_SERVER_ERROR).build() ;
	}
}
