package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductionTargetMaterialEntryList {

	private List<ProductionTargetMaterialEntry> entries;
	
	public ProductionTargetMaterialEntryList() {
		setEntries(new ArrayList<ProductionTargetMaterialEntry>());
	}

	public List<ProductionTargetMaterialEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProductionTargetMaterialEntry> entries) {
		this.entries = entries;
	}
	
}
