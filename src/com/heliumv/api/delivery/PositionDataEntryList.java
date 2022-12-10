package com.heliumv.api.delivery;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PositionDataEntryList {
	private List<PositionDataEntry> entries;
	
	public PositionDataEntryList() {
		setEntries(new ArrayList<PositionDataEntry>());
	}
	
	public PositionDataEntryList(List<PositionDataEntry> entries) {
		setEntries(entries);
	}

	public List<PositionDataEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PositionDataEntry> entries) {
		this.entries = entries;
	}
}
