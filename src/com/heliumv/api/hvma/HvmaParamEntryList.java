package com.heliumv.api.hvma;

import java.util.ArrayList;
import java.util.List;

public class HvmaParamEntryList {
	private List<HvmaParamEntry> entries;
	private long rowCount;
	
	public HvmaParamEntryList() {
		this(new ArrayList<HvmaParamEntry>());
	}
	
	public HvmaParamEntryList(List<HvmaParamEntry> paramEntries) {
		setEntries(paramEntries);
	}
	
	public void setEntries(List<HvmaParamEntry> paramEntries) {
		this.entries = paramEntries;
		setRowCount(paramEntries == null ? 0l : paramEntries.size());
	}

	public List<HvmaParamEntry> getEntries() {
		return this.entries;
	}
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
