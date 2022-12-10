package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockInfoItemEntryList {
	private List<StockInfoItemEntry> entries;
	
	public StockInfoItemEntryList() {
		this(new ArrayList<StockInfoItemEntry>());
	}
	
	public StockInfoItemEntryList(List<StockInfoItemEntry> entries) {
		this.setEntries(entries);
	}

	public List<StockInfoItemEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<StockInfoItemEntry> entries) {
		this.entries = entries;
	}
}
