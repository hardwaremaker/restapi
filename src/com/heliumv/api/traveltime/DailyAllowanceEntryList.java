package com.heliumv.api.traveltime;

import java.util.ArrayList;
import java.util.List;

public class DailyAllowanceEntryList {
	private List<DailyAllowanceEntry> entries;
	private long rowCount;
	
	public DailyAllowanceEntryList() {
		this(new ArrayList<DailyAllowanceEntry>());
	}
	
	public DailyAllowanceEntryList(List<DailyAllowanceEntry> entries) {
		setEntries(entries);
	}
	
	public List<DailyAllowanceEntry> getEntries() {
		return this.entries;
	}
	
	public void setEntries(List<DailyAllowanceEntry> entries) {
		this.entries = entries;
		setRowCount(this.entries != null ? this.entries.size() : 0l);
	}
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
