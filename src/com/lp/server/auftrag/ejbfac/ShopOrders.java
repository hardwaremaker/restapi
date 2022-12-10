package com.lp.server.auftrag.ejbfac;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.springframework.beans.factory.annotation.Autowired;

import com.lp.server.auftrag.service.CreateOrderResult;
import com.lp.server.auftrag.service.WebshopOrderServiceInterface;
import com.lp.server.system.service.WebshopAuthHeader;

@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@WebService(targetNamespace="http://ejbfac.auftrag.server.lp.com")
public class ShopOrders implements IShopOrders {
	@Autowired
	private WebshopOrderServiceInterface webshopOrderServiceCall;

	@Override
	public CreateOrderResult createOrder(
			@WebParam(header = true) WebshopAuthHeader header,
			@WebParam(name="xmlOpenTransOrder") String xmlOpenTransOrder) {
		return webshopOrderServiceCall.createOrder(header, xmlOpenTransOrder);
	}
}
