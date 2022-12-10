package com.heliumv.api;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.heliumv.api.BaseApi.HvErrorCode;

public class HvValidationNotFound extends HvValidationException {
	private static final long serialVersionUID = -636914586242392510L;
	private String key ;
	private String value ;

	public HvValidationNotFound(String key, Integer value) {
		this.key = key ;
		this.value = (value == null ? null : value.toString()) ;
	}
	
	public HvValidationNotFound(String key, String value) {
		this.key = key ;
		this.value = value ;
	}
	
	public String getKey() {
		return key ;
	}
	
	public String getValue() {
		return value ;
	}
	
	@Override
	public Response toResponse() {
		return new ResponseBuilderImpl()
			.status(Response.Status.NOT_FOUND.getStatusCode())
			.header(BaseApi.X_HV_ERROR_CODE, HvErrorCode.UNKNOWN_ENTITY.toString())
			.header(BaseApi.X_HV_ERROR_KEY, getKey())
			.header(BaseApi.X_HV_ERROR_VALUE, getValue())
			.build() ; 
	}
}
