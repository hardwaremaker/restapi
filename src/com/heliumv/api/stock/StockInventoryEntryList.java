package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockInventoryEntryList {
	private List<StockInventoryEntry> entries;
	
	public StockInventoryEntryList() {
		this(new ArrayList<StockInventoryEntry>());
	}
	
	public StockInventoryEntryList(List<StockInventoryEntry> entries) {
		this.setEntries(entries);
	}

	public List<StockInventoryEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<StockInventoryEntry> entries) {
		this.entries = entries;
	}
	
}
