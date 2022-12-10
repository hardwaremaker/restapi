package com.heliumv.factory.impl;

import com.heliumv.factory.BaseCall;
import com.lp.server.partner.service.WebshopCustomerResult;
import com.lp.server.partner.service.WebshopCustomerServiceFacLocal;
import com.lp.server.partner.service.WebshopCustomerServiceInterface;
import com.lp.server.partner.service.WebshopCustomersResult;
import com.lp.server.partner.service.WebshopPricelistResult;
import com.lp.server.partner.service.WebshopPricelistsResult;
import com.lp.server.system.service.WebshopAuthHeader;

public class WebshopCustomerServiceCall extends
		BaseCall<WebshopCustomerServiceFacLocal> implements WebshopCustomerServiceInterface {
	public WebshopCustomerServiceCall() {
		super(WebshopCustomerServiceFacLocal.class, WebshopCustomerServiceFacBean);
	}

	@Override
	public WebshopCustomersResult getCustomers(WebshopAuthHeader header) {
		return getFac().getCustomers(header);
	}

	@Override
	public WebshopCustomersResult getCustomersChanged(WebshopAuthHeader header,
			String changedDate) {
		return getFac().getCustomersChanged(header, changedDate);
	}

	@Override
	public WebshopCustomerResult getCustomerById(WebshopAuthHeader header,
			Integer id) {
		return getFac().getCustomerById(header, id);
	}

	@Override
	public WebshopPricelistResult getPricelistById(WebshopAuthHeader header,
			Integer id) {
		return getFac().getPricelistById(header, id);
	}

	@Override
	public WebshopPricelistsResult getPricelists(WebshopAuthHeader header) {
		return getFac().getPricelists(header);
	}
}
