package com.heliumv.api.item;

import java.rmi.RemoteException;
import java.util.Set;

import javax.naming.NamingException;

import com.heliumv.factory.loader.IItemLoaderAttribute;

public interface IItemService {

	ItemEntryInternal findItemByAttributes(Integer itemId, String itemCnr,
			String serialnumber, String eanBarcode, Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException;

	ItemEntryInternal findItemByBarcodeAttributes(String eanBarcode, Set<IItemLoaderAttribute> attributes)
			throws RemoteException, NamingException;

	ItemEntryInternal findItemByIdAttributes(Integer itemId, Set<IItemLoaderAttribute> attributes)
			throws RemoteException, NamingException;

	ItemEntryInternal findItemBySerialCnrAttributes(String serialnumber, String itemCnr,
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException;

	ItemEntry mapFromInternal(ItemEntryInternal itemEntryInternal);

	ItemV1Entry mapFromInternalV1(ItemEntryInternal itemEntryInternal);

	Set<IItemLoaderAttribute> getAttributes(Boolean addComments, Boolean addStockAmountInfos, Boolean addProducerInfos,
			Boolean addStockplaceInfos, Boolean addDocuments, Boolean addCommentsMedia,
			Boolean addCommentsMediaContent);

}
