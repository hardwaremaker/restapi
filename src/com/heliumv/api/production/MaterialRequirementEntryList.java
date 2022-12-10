package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MaterialRequirementEntryList {

	private List<MaterialRequirementEntry> entries;
	
	public MaterialRequirementEntryList() {
		setEntries(new ArrayList<MaterialRequirementEntry>());
	}

	public List<MaterialRequirementEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<MaterialRequirementEntry> entries) {
		this.entries = entries;
	}
}
