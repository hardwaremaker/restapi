package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PropertyLayoutEntryList {
	private List<PropertyLayoutEntry> entries;

	public PropertyLayoutEntryList() {
		setEntries(new ArrayList<PropertyLayoutEntry>());
	}

	public List<PropertyLayoutEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<PropertyLayoutEntry> entries) {
		this.entries = entries;
	}
}
