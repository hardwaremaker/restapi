package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

public class ItemUnitEntryList {
	private List<ItemUnitEntry> entries;
	private long rowCount;
	
	public ItemUnitEntryList() {
		this(new ArrayList<ItemUnitEntry>());
	}
	
	public ItemUnitEntryList(List<ItemUnitEntry> entries) {
		setEntries(entries);
	}
	
	public List<ItemUnitEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<ItemUnitEntry> entries) {
		this.entries = entries == null ? new ArrayList<ItemUnitEntry>() : entries;
		setRowCount(this.entries.size());
	}
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
