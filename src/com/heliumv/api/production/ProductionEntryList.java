package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductionEntryList {

	private List<ProductionEntry> entries;
	
	public ProductionEntryList() {
		this(new ArrayList<ProductionEntry>());
	}
	
	public ProductionEntryList(List<ProductionEntry> entries) {
		setEntries(entries);
	}

	public List<ProductionEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<ProductionEntry> entries) {
		this.entries = entries;
	}
}
