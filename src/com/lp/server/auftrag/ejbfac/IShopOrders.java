package com.lp.server.auftrag.ejbfac;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.lp.server.auftrag.service.CreateOrderResult;
import com.lp.server.auftrag.service.WebshopOrderServiceInterface;
import com.lp.server.system.service.WebshopAuthHeader;

@WebService
public interface IShopOrders extends WebshopOrderServiceInterface {
	@Override
	public CreateOrderResult createOrder(
			@WebParam(header = true) WebshopAuthHeader header,
			@WebParam(name="xmlOpenTransOrder") String xmlOpenTransOrder);
}
