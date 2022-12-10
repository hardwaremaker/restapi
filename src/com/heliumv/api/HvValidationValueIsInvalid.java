package com.heliumv.api;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.heliumv.api.BaseApi.HvErrorCode;

public class HvValidationValueIsInvalid extends HvValidationException {
	private static final long serialVersionUID = -6516217111859742923L;

	private String key;
	private String value ;
	
	public HvValidationValueIsInvalid(String key) {
		this.key = key;
	}
	
	public HvValidationValueIsInvalid(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public Response toResponse() {
		String v = getValue() == null ? "{empty}" : getValue();
		return new ResponseBuilderImpl()
			.status(Response.Status.BAD_REQUEST.getStatusCode())
			.header(BaseApi.X_HV_ERROR_CODE, HvErrorCode.VALIDATION_FAILED.toString())
			.header(BaseApi.X_HV_ERROR_KEY, getKey()) 
			.header(BaseApi.X_HV_ERROR_VALUE, v)
			.build();
	}
}
