package com.heliumv.api.item;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemV1EntryList {

	private List<ItemV1Entry> entries;
	
	public ItemV1EntryList() {
		setEntries(new ArrayList<ItemV1Entry>());
	}
	
	public ItemV1EntryList(List<ItemV1Entry> entries) {
		setEntries(entries);
	}
	
	public List<ItemV1Entry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<ItemV1Entry> entries) {
		this.entries = entries;
	}
}
