package com.lp.server.system.ejbfac;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.lp.server.system.service.WebshopError;
import com.lp.server.system.service.WebshopErrorResult;
import com.lp.server.system.service.WebshopSystemInfoServiceInterface;

@WebService
public interface IShopSystemInfo extends WebshopSystemInfoServiceInterface {
	
	@Override
	public WebshopErrorResult error(
			@WebParam(name="error") WebshopError anError);
}
