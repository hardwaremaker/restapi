package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemHintEntry;

@XmlRootElement
public class ItemHintEntryList {
	private List<ItemHintEntry> entries;
	
	public ItemHintEntryList() {
		setEntries(new ArrayList<ItemHintEntry>());
	}

	public ItemHintEntryList(List<ItemHintEntry> entries) {
		setEntries(entries);
	}

	public List<ItemHintEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ItemHintEntry> entries) {
		this.entries = entries;
	}
}
