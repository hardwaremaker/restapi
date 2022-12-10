package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductionWorkstepEntryList {

	private List<ProductionWorkstepEntry> entries;
	
	public ProductionWorkstepEntryList() {
		setEntries(new ArrayList<ProductionWorkstepEntry>());
	}

	public List<ProductionWorkstepEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProductionWorkstepEntry> entries) {
		this.entries = entries;
	}
}
