package com.heliumv.api.partlist;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductionGroupEntryList {
	private List<ProductionGroupEntry> entries;

	public ProductionGroupEntryList() {
		setEntries(new ArrayList<ProductionGroupEntry>());
	}

	public ProductionGroupEntryList(List<ProductionGroupEntry> entries) {
		setEntries(entries);
	}
	
	public List<ProductionGroupEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProductionGroupEntry> entries) {
		this.entries = entries;
	}
}
