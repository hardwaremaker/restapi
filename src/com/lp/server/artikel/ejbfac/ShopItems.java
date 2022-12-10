package com.lp.server.artikel.ejbfac;

import java.sql.Timestamp;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.springframework.beans.factory.annotation.Autowired;

import com.lp.server.artikel.service.ShopgroupResult;
import com.lp.server.artikel.service.ShopgroupsFlatResult;
import com.lp.server.artikel.service.ShopgroupsResult;
import com.lp.server.artikel.service.WebshopItemImageResult;
import com.lp.server.artikel.service.WebshopItemResult;
import com.lp.server.artikel.service.WebshopItemServiceInterface;
import com.lp.server.artikel.service.WebshopItemsResult;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.WebshopId;

@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@WebService(targetNamespace="http://ejbfac.artikel.server.lp.com")
public class ShopItems implements IShopItems {
	@Autowired
	private WebshopItemServiceInterface webshopItemServiceCall;

	@Override
	public ShopgroupsResult getShopGroupsFindAll(WebshopAuthHeader header) {
		return webshopItemServiceCall.getShopGroupsFindAll(header);
	}

	@Override
	public ShopgroupsResult getShopGroupsFindAllChanged(
			WebshopAuthHeader header, String changedDate) {
		return webshopItemServiceCall.getShopGroupsFindAllChanged(header, changedDate);
	}

	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(
			WebshopAuthHeader header) {
		return webshopItemServiceCall.getShopGroupsFlatFindAll(header);
	}

	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(
			WebshopAuthHeader header, String changedDate) {
		return webshopItemServiceCall.getShopGroupsFlatFindAllChanged(header, changedDate);
	}

	@Override
	public ShopgroupsResult getShopGroupsFindByCnrChanged(
			WebshopAuthHeader header, String rootShopgruppe, String changedDate) {
		return webshopItemServiceCall.getShopGroupsFindByCnrChanged(header, rootShopgruppe, changedDate);
	}

	@Override
	public ShopgroupResult getShopGroupFindByCnr(WebshopAuthHeader header,
			String name) {
		return webshopItemServiceCall.getShopGroupFindByCnr(header, name);
	}

	@Override
	public ShopgroupResult getShopGroupFindById(WebshopAuthHeader header,
			Integer id) {
		return webshopItemServiceCall.getShopGroupFindById(header, id);
	}

	@Override
	public ShopgroupsResult getShopGroupsFindByIdChanged(
			WebshopAuthHeader header, Integer rootShopgruppeIId,
			String changedDate) {
		return webshopItemServiceCall.getShopGroupsFindByIdChanged(header, rootShopgruppeIId, changedDate);
	}

	@Override
	public WebshopItemResult getItemFindByCnr(WebshopAuthHeader header,
			String cnr) {
		return webshopItemServiceCall.getItemFindByCnr(header, cnr);
	}

	@Override
	public WebshopItemResult getItemFindById(WebshopAuthHeader header,
			Integer id) {
		return webshopItemServiceCall.getItemFindById(header, id);
	}

	@Override
	public WebshopItemImageResult getItemImage(WebshopAuthHeader header,
			String itemImageName) {
		return webshopItemServiceCall.getItemImage(header, itemImageName);
	}

	@Override
	public WebshopItemsResult getItems(WebshopAuthHeader header) {
		return webshopItemServiceCall.getItems(header);
	}

	@Override
	public WebshopItemsResult getItemsChanged(WebshopAuthHeader header,
			String changedDateTime) {
		return webshopItemServiceCall.getItemsChanged(header, changedDateTime);
	}

	@Override
	public WebshopItemsResult getItemsRestChanged(TheClientDto theClientDto,
			WebshopId shopId, Timestamp changedTimestamp) {
		return webshopItemServiceCall.getItemsRestChanged(
				theClientDto, shopId, changedTimestamp);
	}
}
