package com.heliumv.api;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heliumv.api.BaseApi.HvErrorCode;

public class HvIOExceptionMapper implements ExceptionMapper<IOException> {
	private static Logger log = LoggerFactory.getLogger(HvIOExceptionMapper.class) ;

	@Override
	public Response toResponse(IOException exc) {
		log.info("default-log", exc);
		return new ResponseBuilderImpl()
			.header(BaseApi.X_HV_ERROR_CODE, HvErrorCode.IO_EXCEPTION.toString())
			.header(BaseApi.X_HV_ERROR_CODE_DESCRIPTION, exc.getMessage())
			.status(Response.Status.INTERNAL_SERVER_ERROR).build() ;
	}

}
