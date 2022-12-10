package com.heliumv.api.stock;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemV1EntryList;

@XmlRootElement
public class StockInventoryInfoEntry {
	private StockInventoryEntryList entries;
	private ItemV1EntryList items;
	
	public StockInventoryInfoEntry() {
	}

	public StockInventoryEntryList getEntries() {
		return entries;
	}

	public void setEntries(StockInventoryEntryList entries) {
		this.entries = entries;
	}

	public ItemV1EntryList getItems() {
		return items;
	}

	public void setItems(ItemV1EntryList items) {
		this.items = items;
	}
}
