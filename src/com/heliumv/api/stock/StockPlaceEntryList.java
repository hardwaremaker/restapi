package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockPlaceEntryList {
	
	private List<StockPlaceEntry> entries;

	public StockPlaceEntryList() {
		this(new ArrayList<StockPlaceEntry>());
	}
	
	public StockPlaceEntryList(List<StockPlaceEntry> entries) {
		setEntries(entries);
	}
	
	public List<StockPlaceEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<StockPlaceEntry> entries) {
		this.entries = entries;
	}

}
