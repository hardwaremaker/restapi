package com.heliumv.api.stock;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.StockEntry;

@XmlRootElement
public class StockEntryInfoItemEntry {
	private StockEntry stockEntry;
	private StockInfoEntryList infoEntries;
	
	public StockEntry getStockEntry() {
		return stockEntry;
	}

	public void setStockEntry(StockEntry stockEntry) {
		this.stockEntry = stockEntry;
	}

	public StockInfoEntryList getInfoEntries() {
		return infoEntries;
	}

	public void setInfoEntries(StockInfoEntryList infoEntries) {
		this.infoEntries = infoEntries;
	}	
}
