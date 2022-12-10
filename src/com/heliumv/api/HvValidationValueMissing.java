package com.heliumv.api;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.heliumv.api.BaseApi.HvErrorCode;

public class HvValidationValueMissing extends HvValidationException {
	private static final long serialVersionUID = -4370320117558105172L;

	private String key ;
	
	public HvValidationValueMissing(String key) {
		this.key = key ;
	}
	
	public String getKey() {
		return key ;
	}
	
	@Override
	public Response toResponse() {
		return new ResponseBuilderImpl()
			.status(Response.Status.BAD_REQUEST.getStatusCode())
			.header(BaseApi.X_HV_ERROR_CODE, HvErrorCode.VALIDATION_FAILED.toString())
			.header(BaseApi.X_HV_ERROR_KEY, getKey()) 
			.header(BaseApi.X_HV_ERROR_VALUE, "{empty}")
			.build();
	}
}
