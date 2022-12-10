package com.heliumv.api.partlist;

import java.util.ArrayList;
import java.util.List;

public class MountingMethodEntryList {

	private List<MountingMethodEntry> entries;
	
	public MountingMethodEntryList() {
		setEntries(new ArrayList<MountingMethodEntry>());
	}

	public List<MountingMethodEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<MountingMethodEntry> entries) {
		this.entries = entries;
	}

}
