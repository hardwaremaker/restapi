package com.lp.server.artikel.ejbfac;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.lp.server.artikel.service.ShopgroupResult;
import com.lp.server.artikel.service.ShopgroupsFlatResult;
import com.lp.server.artikel.service.ShopgroupsResult;
import com.lp.server.artikel.service.WebshopItemImageResult;
import com.lp.server.artikel.service.WebshopItemResult;
import com.lp.server.artikel.service.WebshopItemServiceInterface;
import com.lp.server.artikel.service.WebshopItemsResult;
import com.lp.server.system.service.WebshopAuthHeader;

@WebService
public interface IShopItems extends WebshopItemServiceInterface {
	@Override
	public WebshopItemResult getItemFindByCnr(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="cnr") String cnr);

	@Override
	public WebshopItemResult getItemFindById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="id") Integer id);	
	
	@Override
	public WebshopItemImageResult getItemImage(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="itemImageName") String itemImageName);
	
	@Override
	public WebshopItemsResult getItems(
			@WebParam WebshopAuthHeader header);
	
	@Override
	public WebshopItemsResult getItemsChanged(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="changedDateTime") String changedDateTime);
	
	@Override
	public ShopgroupResult getShopGroupFindByCnr(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="cnr") String name);
	
	@Override
	public ShopgroupResult getShopGroupFindById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="id") Integer id);

	@Override
	public ShopgroupsResult getShopGroupsFindAll(
			@WebParam  WebshopAuthHeader header);
	
	@Override
	public ShopgroupsResult getShopGroupsFindAllChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="changedDateTime") String changedDate);
	
	@Override
	public ShopgroupsResult getShopGroupsFindByCnrChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="cnr") String rootShopgruppe,
			@WebParam(name="changedDateTime") String changedDate);
	
	@Override
	public ShopgroupsResult getShopGroupsFindByIdChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="id") Integer rootShopgruppeIId,
			@WebParam(name="changedDateTime") String changedDate);	
	
	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(
			@WebParam WebshopAuthHeader header);
	
	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="changedDateTime") String changedDate);
}
