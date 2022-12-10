package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.StockEntryMapper;
import com.heliumv.factory.ILagerCall;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerplatzDto;

public class StockInfoEntryMapper {

	@Autowired
	private StockPlaceEntryMapper stockPlaceEntryMapper;
	@Autowired
	private ILagerCall lagerCall;
	
	private Map<Integer, List<StockPlaceEntry>> mapStockInfo;
	
	public List<StockInfoEntry> mapEntries(List<LagerplatzDto> lagerplatzDtos) {
		mapStockInfo = new HashMap<Integer, List<StockPlaceEntry>>();
		
		for (LagerplatzDto dto : lagerplatzDtos) {
			addEntryToMapStockInfo(dto.getLagerIId(), stockPlaceEntryMapper.mapEntry(dto));
		}
		
		List<StockInfoEntry> entries = new ArrayList<StockInfoEntry>();
		StockEntryMapper stockEntryMapper = new StockEntryMapper();
		for (Entry<Integer, List<StockPlaceEntry>> stockEntry : mapStockInfo.entrySet()) {
			LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(stockEntry.getKey());
			if (lagerDto != null) {
				StockInfoEntry entry = new StockInfoEntry();
				entry.setStockEntry(stockEntryMapper.mapEntry(lagerDto));
				entry.setStockplaceEntries(new StockPlaceEntryList(stockEntry.getValue()));
				entries.add(entry);
			}
		}
		return entries;
	}

	private void addEntryToMapStockInfo(Integer lagerIId, StockPlaceEntry entry) {
		List<StockPlaceEntry> stockinfoEntries = mapStockInfo.get(lagerIId);
		if (stockinfoEntries == null) {
			stockinfoEntries = new ArrayList<StockPlaceEntry>();
			mapStockInfo.put(lagerIId, stockinfoEntries);
		}
		stockinfoEntries.add(entry);
	}
	
}
