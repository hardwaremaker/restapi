package com.heliumv.api.purchaseorder;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryInternal;
import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.api.item.ItemV1Entry;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BestellpositionDto;

public class PurchaseOrderPositionEntryMapper {
	@Autowired
	private ItemEntryMapper itemEntryMapper;
	@Autowired
	private ModelMapper modelMapper ;

	public PurchaseOrderPositionEntry mapEntry(BestellpositionDto besposDto, ArtikelDto artikelDto) {
		PurchaseOrderPositionEntry entry = new PurchaseOrderPositionEntry();
		entry.setId(besposDto.getIId());
		entry.setItemId(besposDto.getArtikelIId());
		entry.setOpenQuantity(besposDto.getNOffeneMenge());
		entry.setQuantity(besposDto.getNMenge());
		
		if(artikelDto != null) {
			entry.setItemEntry(mapFromInternal(itemEntryMapper.mapEntry(artikelDto)));			
		}
		
		return entry;
	}
	
	protected ItemV1Entry mapFromInternal(ItemEntryInternal itemEntryInternal) {
		if(itemEntryInternal == null) return null ;
		
		ItemV1Entry itemEntry = modelMapper.map(itemEntryInternal, ItemV1Entry.class) ;
		return itemEntry ;
	}
}
