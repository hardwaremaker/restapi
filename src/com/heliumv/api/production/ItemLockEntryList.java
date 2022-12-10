package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemLockEntry;

@XmlRootElement
public class ItemLockEntryList {
	private List<ItemLockEntry> entries;
	
	public ItemLockEntryList() {
		setEntries(new ArrayList<ItemLockEntry>());
	}

	public ItemLockEntryList(List<ItemLockEntry> entries) {
		setEntries(entries);
	}
	
	public List<ItemLockEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ItemLockEntry> entries) {
		this.entries = entries;
	}
}
