package com.heliumv.api;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.heliumv.api.BaseApi.HvErrorCode;
import com.lp.util.EJBExceptionLP;

public class HvValidationNoPositions extends HvValidationException {
	private static final long serialVersionUID = 3111747176536198320L;

	public HvValidationNoPositions() {
	}
	
	@Override
	public Response toResponse() {
		return new ResponseBuilderImpl()
			.status(417)//Response.Status.EXPECTATION_FAILED.getStatusCode())
			.header(BaseApi.X_HV_ERROR_CODE, HvErrorCode.EXPECTATION_FAILED.toString())
			.header(BaseApi.X_HV_ERROR_CODE_EXTENDED, EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN)
			.build();
	}

}
