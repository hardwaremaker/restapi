package com.heliumv.api.item;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.loader.IArtikelLoaderCall;
import com.heliumv.factory.loader.IItemLoaderAttribute;
import com.heliumv.factory.loader.ItemLoaderComments;
import com.heliumv.factory.loader.ItemLoaderCommentsMedia;
import com.heliumv.factory.loader.ItemLoaderCommentsMediaContent;
import com.heliumv.factory.loader.ItemLoaderDocuments;
import com.heliumv.factory.loader.ItemLoaderProducerInfo;
import com.heliumv.factory.loader.ItemLoaderStockinfoSummary;
import com.heliumv.factory.loader.ItemLoaderStockplaceInfo;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelDto;

public class ItemService implements IItemService {
	private static Logger log = LoggerFactory.getLogger(ItemService.class) ;

	@Autowired
	private ItemLoaderComments itemloaderComments ;
	@Autowired
	private ItemLoaderStockinfoSummary itemloaderStockinfoSummary ;
	@Autowired
	private ItemLoaderProducerInfo itemLoaderProducerInfo ;
	@Autowired
	private ItemLoaderStockplaceInfo itemLoaderStockplaceInfo;
	@Autowired
	private ItemLoaderDocuments itemLoaderDocuments;
	@Autowired
	private ItemLoaderCommentsMedia itemLoaderCommentsMedia;
	@Autowired
	private ItemLoaderCommentsMediaContent itemLoaderCommentsMediaContent;
	@Autowired
	private IArtikelLoaderCall artikelLoaderCall;
	@Autowired
	protected ILagerCall lagerCall;
	@Autowired
	protected IArtikelCall artikelCall ;
	@Autowired
	private ModelMapper modelMapper ;
	
	@Override
	public ItemEntry mapFromInternal(ItemEntryInternal itemEntryInternal) {
		if(itemEntryInternal == null) return null ;
		
		ItemEntry itemEntry = modelMapper.map(itemEntryInternal, ItemEntry.class) ;
		return itemEntry ;
	}

	@Override
	public ItemV1Entry mapFromInternalV1(ItemEntryInternal itemEntryInternal) {
		if(itemEntryInternal == null) return null ;
		
		ItemV1Entry itemEntry = new ItemV1Entry() ;
		itemEntry.setAvailable(itemEntryInternal.isAvailable());
		itemEntry.setBillOfMaterialType(itemEntryInternal.getBillOfMaterialType());
		itemEntry.setCnr(itemEntryInternal.getCnr());
		itemEntry.setComments(itemEntryInternal.getComments());
		itemEntry.setCosts(itemEntryInternal.getCosts());
		itemEntry.setDescription(itemEntryInternal.getDescription());
		itemEntry.setDescription2(itemEntryInternal.getDescription2());
		itemEntry.setHasChargenr(itemEntryInternal.getHasChargenr());
		itemEntry.setHasSerialnr(itemEntryInternal.getHasSerialnr());
		itemEntry.setHidden(itemEntryInternal.getHidden());
		itemEntry.setId(itemEntryInternal.getId());
		itemEntry.setIndex(itemEntryInternal.getIndex());
		itemEntry.setItemclassCnr(itemEntryInternal.getItemclassCnr());
		itemEntry.setItemgroupCnr(itemEntryInternal.getItemgroupCnr());
		itemEntry.setName(itemEntryInternal.getName());
		itemEntry.setProducerInfoEntry(itemEntryInternal.getProducerInfo());
		itemEntry.setReferenceNumber(itemEntryInternal.getReferenceNumber());
		itemEntry.setRevision(itemEntryInternal.getRevision());
		itemEntry.setShortName(itemEntryInternal.getShortName());
		itemEntry.setStockAmount(itemEntryInternal.getStockAmount());
		itemEntry.setStockAmountInfo(itemEntryInternal.getStockAmountInfo());
		itemEntry.setTypeCnr(itemEntryInternal.getTypeCnr());
		itemEntry.setUnitCnr(itemEntryInternal.getUnitCnr());
		itemEntry.setPackagingEntries(itemEntryInternal.getPackagingEntries());
		itemEntry.setStockplaceInfoEntries(itemEntryInternal.getStockplaceInfoEntries());
		itemEntry.setDocumentInfoEntries(itemEntryInternal.getDocumentInfoEntries());
		itemEntry.setItemCommentMediaInfoEntries(itemEntryInternal.getItemCommentMediaInfoEntries());
		return itemEntry ;
	}
	
	@Override
	public Set<IItemLoaderAttribute> getAttributes(
			Boolean addComments, Boolean addStockAmountInfos, Boolean addProducerInfos, 
			Boolean addStockplaceInfos, Boolean addDocuments, Boolean addCommentsMedia,
			Boolean addCommentsMediaContent) {
		Set<IItemLoaderAttribute> attributes = new HashSet<IItemLoaderAttribute>() ;
		if(Boolean.TRUE.equals(addComments)) {
			attributes.add(itemloaderComments) ;
		}
		if(Boolean.TRUE.equals(addStockAmountInfos)) {
			attributes.add(itemloaderStockinfoSummary) ;
		}
		if(Boolean.TRUE.equals(addProducerInfos)) {
			attributes.add(itemLoaderProducerInfo) ;
		}
		if (Boolean.TRUE.equals(addStockplaceInfos)) {
			attributes.add(itemLoaderStockplaceInfo);
		}
		if (Boolean.TRUE.equals(addDocuments)) {
			attributes.add(itemLoaderDocuments);
		}
		if (Boolean.TRUE.equals(addCommentsMedia)) {
			attributes.add(itemLoaderCommentsMedia);
		}
		if (Boolean.TRUE.equals(addCommentsMediaContent)) {
			attributes.add(itemLoaderCommentsMediaContent);
		}

		return attributes ;
	}
 
	@Override
	public ItemEntryInternal findItemByIdAttributes(Integer itemId, 
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		HvValidateBadRequest.notNull(itemId, "itemId");
		return findItemEntryByIdImpl(itemId, attributes);
	}	
	
	@Override
	public ItemEntryInternal findItemBySerialCnrAttributes(String serialnumber, String itemCnr,
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		HvValidateBadRequest.notEmpty(serialnumber, "serialnumber");
		ItemEntryInternal itemEntry = findItemEntryBySerialnumberCnr(
				serialnumber, itemCnr, attributes) ;
		return itemEntry ;		
	}
	
	@Override
	public ItemEntryInternal findItemByBarcodeAttributes(String eanBarcode, Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		HvValidateBadRequest.notEmpty(eanBarcode, "eanBarcode");
		ItemEntryInternal itemEntry = findItemEntryByEanImpl(eanBarcode, attributes) ;
		return itemEntry ;				
	}
	
	@Override
	public ItemEntryInternal findItemByAttributes(Integer itemId, String itemCnr,
			String serialnumber, String eanBarcode, Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		if(itemId != null) {
			ItemEntryInternal itemEntry = findItemEntryByIdImpl(itemId, attributes);
			return itemEntry ;
		}

		if(!StringHelper.isEmpty(serialnumber)) {
			ItemEntryInternal itemEntry = findItemEntryBySerialnumberCnr(
					serialnumber, itemCnr, attributes) ;
			return itemEntry ;
		}
		
		if(!StringHelper.isEmpty(itemCnr)) {
			ItemEntryInternal itemEntry = findItemEntryByCnrImpl(itemCnr, attributes) ;
			return itemEntry ;				
		}
		
		if(!StringHelper.isEmpty(eanBarcode)) {
			ItemEntryInternal itemEntry = findItemEntryByEanImpl(eanBarcode, attributes) ;
			return itemEntry ;				
		}
		
		return null;
	}
	
	private ItemEntryInternal findItemEntryBySerialnumberCnr(String serialnumber, String cnr, Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		Integer itemId = lagerCall.artikelIdFindBySeriennummerOhneExc(serialnumber) ;
		if(itemId == null) return null ;
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId) ;
		if(artikelDto == null) return null ;
		
		if(!StringHelper.isEmpty(cnr)) {
			if(!artikelDto.getCNr().equals(cnr)) return null ;
		}
		
		return artikelLoaderCall.artikelFindByCNrOhneExc(artikelDto.getCNr(), attributes) ;
	}

	private ItemEntryInternal findItemEntryByIdImpl(Integer itemId,
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		ItemEntryInternal itemEntry = artikelLoaderCall.artikelFindByIdOhneExc(itemId, attributes) ;
		return itemEntry ;	
	}
	
	private ItemEntryInternal findItemEntryByCnrImpl(String cnr, 
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		ItemEntryInternal itemEntry = artikelLoaderCall.artikelFindByCNrOhneExc(cnr, attributes) ;
		return itemEntry ;
	}

	private ItemEntryInternal findItemEntryByEanImpl(String ean,
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		ItemEntryInternal itemEntry = artikelLoaderCall.artikelFindByEanOhneExc(ean, attributes) ;
		return itemEntry ;
	}
}
