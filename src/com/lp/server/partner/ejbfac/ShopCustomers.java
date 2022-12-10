package com.lp.server.partner.ejbfac;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.springframework.beans.factory.annotation.Autowired;

import com.lp.server.partner.service.WebshopCustomerResult;
import com.lp.server.partner.service.WebshopCustomerServiceInterface;
import com.lp.server.partner.service.WebshopCustomersResult;
import com.lp.server.partner.service.WebshopPricelistResult;
import com.lp.server.partner.service.WebshopPricelistsResult;
import com.lp.server.system.service.WebshopAuthHeader;

@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@WebService(targetNamespace="http://ejbfac.partner.server.lp.com")
public class ShopCustomers implements IShopCustomers {
	@Autowired
	private WebshopCustomerServiceInterface webshopCustomerServiceCall;
	

	@Override
	public WebshopCustomersResult getCustomers(
			@WebParam WebshopAuthHeader header) {
		return webshopCustomerServiceCall.getCustomers(header);
	}

	@Override
	@WebMethod
	public WebshopCustomersResult getCustomersChanged(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="changedDateTime") String changedDateTime) {
		return webshopCustomerServiceCall.getCustomersChanged(header, changedDateTime);
	}

	@Override
	public WebshopCustomerResult getCustomerById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="customerId") Integer id) {
		return webshopCustomerServiceCall.getCustomerById(header, id);
	}

	@Override
	public WebshopPricelistResult getPricelistById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="pricelistId") Integer id) {
		return webshopCustomerServiceCall.getPricelistById(header, id);
	}

	@Override
	public WebshopPricelistsResult getPricelists(
			@WebParam WebshopAuthHeader header) {
		return webshopCustomerServiceCall.getPricelists(header);
	}
}
