package com.heliumv.api.forecast;

import java.util.ArrayList;
import java.util.List;

public class LinecallEntryList {

	private List<LinecallEntry> entries;
	
	public LinecallEntryList() {
		setEntries(new ArrayList<LinecallEntry>());
	}

	public List<LinecallEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<LinecallEntry> entries) {
		this.entries = entries;
	}

}
