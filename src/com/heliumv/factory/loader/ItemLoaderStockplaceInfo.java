package com.heliumv.factory.loader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryInternal;
import com.heliumv.api.item.StockEntryMapper;
import com.heliumv.api.stock.StockInfoEntry;
import com.heliumv.api.stock.StockInfoEntryList;
import com.heliumv.api.stock.StockPlaceEntryList;
import com.heliumv.api.stock.StockPlaceEntryMapper;
import com.heliumv.factory.ILagerCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerplatzInfoDto;

public class ItemLoaderStockplaceInfo implements IItemLoaderAttribute {

	@Autowired
	private ILagerCall lagerCall;

	@Override
	public ItemEntryInternal load(ItemEntryInternal entry, ArtikelDto artikelDto) {
		List<LagerplatzInfoDto> infoDtos = lagerCall.lagerplatzInfoFindByArtikelIIdOhneExc(artikelDto.getIId());
		StockInfoEntryList infoEntries = new StockInfoEntryList();
		StockEntryMapper stockEntryMapper = new StockEntryMapper();
		StockPlaceEntryMapper stockPlaceEntryMapper = new StockPlaceEntryMapper();
		
		for (LagerplatzInfoDto info : infoDtos) {
			if (!lagerCall.hatRolleBerechtigungAufLager(info.getLagerDto().getIId()))
				continue;
			
			StockInfoEntry infoEntry = new StockInfoEntry();
			infoEntry.setStockEntry(stockEntryMapper.mapEntry(info.getLagerDto()));
			infoEntry.setStockplaceEntries(new StockPlaceEntryList(
					stockPlaceEntryMapper.mapEntries(info.getLagerplaetze())));
			infoEntries.getEntries().add(infoEntry);
		}
		entry.setStockplaceInfoEntries(infoEntries);
		
		return entry;
	}

}
