package com.heliumv.factory.impl;

import com.heliumv.factory.BaseCall;
import com.lp.server.auftrag.service.CreateOrderResult;
import com.lp.server.auftrag.service.WebshopOrderServiceFacLocal;
import com.lp.server.auftrag.service.WebshopOrderServiceInterface;
import com.lp.server.system.service.WebshopAuthHeader;

public class WebshopOrderServiceCall extends
		BaseCall<WebshopOrderServiceFacLocal> implements
		WebshopOrderServiceInterface {

	public WebshopOrderServiceCall() {
		super(WebshopOrderServiceFacLocal.class, WebshopCustomerOrderServiceFacBean);
	}
	
	@Override
	public CreateOrderResult createOrder(WebshopAuthHeader header,
			String xmlOpenTransOrder) {
		return getFac().createOrder(header, xmlOpenTransOrder);
	}
}
