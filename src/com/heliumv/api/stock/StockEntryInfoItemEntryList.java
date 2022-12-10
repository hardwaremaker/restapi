package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockEntryInfoItemEntryList {
	private List<StockEntryInfoItemEntry> entries;
	
	public StockEntryInfoItemEntryList() {
		this(new ArrayList<StockEntryInfoItemEntry>());
	}
	
	public StockEntryInfoItemEntryList(List<StockEntryInfoItemEntry> entries) {
		this.setEntries(entries);
	}

	public List<StockEntryInfoItemEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<StockEntryInfoItemEntry> entries) {
		this.entries = entries;
	}
}
