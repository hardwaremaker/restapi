package com.lp.server.partner.ejbfac;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.lp.server.partner.service.WebshopCustomerResult;
import com.lp.server.partner.service.WebshopCustomerServiceInterface;
import com.lp.server.partner.service.WebshopCustomersResult;
import com.lp.server.partner.service.WebshopPricelistResult;
import com.lp.server.partner.service.WebshopPricelistsResult;
import com.lp.server.system.service.WebshopAuthHeader;

@WebService
public interface IShopCustomers extends WebshopCustomerServiceInterface {
	@Override
	public WebshopCustomersResult getCustomersChanged(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="changedDateTime") String changedDate);
	
	@Override
	public WebshopCustomerResult getCustomerById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="customerId") Integer id);
	
	@Override
	public WebshopCustomersResult getCustomers(
			@WebParam WebshopAuthHeader header);
	
	@Override
	public WebshopPricelistResult getPricelistById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="pricelistId") Integer id);
	
	@Override
	public WebshopPricelistsResult getPricelists(
			@WebParam WebshopAuthHeader header);
}
