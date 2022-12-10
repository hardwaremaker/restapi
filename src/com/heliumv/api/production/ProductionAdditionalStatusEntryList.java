package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductionAdditionalStatusEntryList {

	private List<ProductionAdditionalStatusEntry> entries;

	public ProductionAdditionalStatusEntryList() {
		setEntries(new ArrayList<ProductionAdditionalStatusEntry>());
	}

	public List<ProductionAdditionalStatusEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProductionAdditionalStatusEntry> entries) {
		this.entries = entries;
	}
	
}
