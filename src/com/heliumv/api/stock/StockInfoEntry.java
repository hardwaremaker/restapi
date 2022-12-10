package com.heliumv.api.stock;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.StockEntry;

@XmlRootElement
public class StockInfoEntry {

	private StockEntry stockEntry;
	private StockPlaceEntryList stockplaceEntries;
	
	public StockEntry getStockEntry() {
		return stockEntry;
	}
	
	public void setStockEntry(StockEntry stockEntry) {
		this.stockEntry = stockEntry;
	}

	/**
	 * Die zum Lager zugeteilten Lagerpl&auml;tze
	 * 
	 * @return Lagerpl&auml;tze des Lagers
	 */
	public StockPlaceEntryList getStockplaceEntries() {
		return stockplaceEntries;
	}
	
	public void setStockplaceEntries(StockPlaceEntryList stockplaceEntries) {
		this.stockplaceEntries = stockplaceEntries;
	}
}
