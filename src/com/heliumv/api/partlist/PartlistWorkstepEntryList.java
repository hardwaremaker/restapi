package com.heliumv.api.partlist;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PartlistWorkstepEntryList {
	private List<PartlistWorkstepEntry> entries;
	
	public PartlistWorkstepEntryList() {
		setEntries(new ArrayList<PartlistWorkstepEntry>());
	}

	public List<PartlistWorkstepEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PartlistWorkstepEntry> entries) {
		this.entries = entries;
	}
}
