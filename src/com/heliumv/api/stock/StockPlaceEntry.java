package com.heliumv.api.stock;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemV1EntryList;

@XmlRootElement
public class StockPlaceEntry extends BaseEntryId {

	private String name;
	private ItemV1EntryList items;
	
	public StockPlaceEntry() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ItemV1EntryList getItems() {
		return items;
	}
	
	public void setItems(ItemV1EntryList items) {
		this.items = items;
	}
}
