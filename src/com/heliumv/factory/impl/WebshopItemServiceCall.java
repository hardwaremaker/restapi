package com.heliumv.factory.impl;

import java.sql.Timestamp;

import com.heliumv.factory.BaseCall;
import com.lp.server.artikel.service.ShopgroupResult;
import com.lp.server.artikel.service.ShopgroupsFlatResult;
import com.lp.server.artikel.service.ShopgroupsResult;
import com.lp.server.artikel.service.WebshopItemImageResult;
import com.lp.server.artikel.service.WebshopItemResult;
import com.lp.server.artikel.service.WebshopItemServiceFacLocal;
import com.lp.server.artikel.service.WebshopItemServiceInterface;
import com.lp.server.artikel.service.WebshopItemsResult;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.WebshopId;

public class WebshopItemServiceCall extends
		BaseCall<WebshopItemServiceFacLocal> implements WebshopItemServiceInterface {
	public WebshopItemServiceCall()  {
		super(WebshopItemServiceFacLocal.class, WebshopItemServiceFacBean) ;
	}

	@Override
	public ShopgroupsResult getShopGroupsFindAll(WebshopAuthHeader header) {
		return getFac().getShopGroupsFindAll(header);
	}

	@Override
	public ShopgroupsResult getShopGroupsFindAllChanged(
			WebshopAuthHeader header, String changedDate) {
		return getFac().getShopGroupsFindAllChanged(header, changedDate);
	}

	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(
			WebshopAuthHeader header) {
		return getFac().getShopGroupsFlatFindAll(header);
	}

	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(
			WebshopAuthHeader header, String changedDate) {
		return getFac().getShopGroupsFlatFindAllChanged(header, changedDate);
	}

	@Override
	public ShopgroupsResult getShopGroupsFindByCnrChanged(
			WebshopAuthHeader header, String rootShopgruppe, String changedDate) {
		return getFac().getShopGroupsFindByCnrChanged(header, rootShopgruppe, changedDate);
	}

	@Override
	public ShopgroupResult getShopGroupFindByCnr(WebshopAuthHeader header,
			String name) {
		return getFac().getShopGroupFindByCnr(header, name);
	}

	@Override
	public ShopgroupResult getShopGroupFindById(WebshopAuthHeader header,
			Integer id) {
		return getFac().getShopGroupFindById(header, id);
	}

	@Override
	public ShopgroupsResult getShopGroupsFindByIdChanged(
			WebshopAuthHeader header, Integer rootShopgruppeIId,
			String changedDate) {
		return getFac().getShopGroupsFindByIdChanged(header, rootShopgruppeIId, changedDate);
	}

	@Override
	public WebshopItemResult getItemFindByCnr(WebshopAuthHeader header,
			String cnr) {
		return getFac().getItemFindByCnr(header, cnr);
	}

	@Override
	public WebshopItemResult getItemFindById(WebshopAuthHeader header,
			Integer id) {
		return getFac().getItemFindById(header, id);
	}

	@Override
	public WebshopItemImageResult getItemImage(WebshopAuthHeader header,
			String itemImageName) {
		return getFac().getItemImage(header, itemImageName);
	}

	@Override
	public WebshopItemsResult getItems(WebshopAuthHeader header) {
		return getFac().getItems(header);
	}

	@Override
	public WebshopItemsResult getItemsChanged(WebshopAuthHeader header,
			String changedDateTime) {
		return new WebshopItemsResult(WebshopItemsResult.ERROR_NOT_FOUND, "");
	}

	@Override
	public WebshopItemsResult getItemsRestChanged(TheClientDto theClientDto, 
			WebshopId shopId, Timestamp changedTimestamp) {
		return new WebshopItemsResult(WebshopItemsResult.ERROR_NOT_FOUND, "");
	}
}
