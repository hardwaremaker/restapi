package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TextblockEntryList {
	private List<TextblockEntry> entries;
	private long rowCount;
	
	public TextblockEntryList() {
		this(new ArrayList<TextblockEntry>());
	}
	
	public TextblockEntryList(List<TextblockEntry> entries) {
		setEntries(entries);
	}
	
	public List<TextblockEntry> getEntries() {
		return this.entries;
	}
	
	public void setEntries(List<TextblockEntry> entries) {
		this.entries = entries == null ? new ArrayList<TextblockEntry>() : entries;
		setRowCount(this.entries.size());
	}
	
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
