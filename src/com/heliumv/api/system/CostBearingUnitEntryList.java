package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CostBearingUnitEntryList {
	private List<CostBearingUnitEntry> entries;
	private long rowCount;
	
	public CostBearingUnitEntryList() {
		this(new ArrayList<CostBearingUnitEntry>());
	}
	
	public CostBearingUnitEntryList(List<CostBearingUnitEntry> entries) {
		setEntries(entries);
	}
	
	public List<CostBearingUnitEntry> getEntries() {
		return this.entries;
	}
	
	public void setEntries(List<CostBearingUnitEntry> entries) {
		this.entries = entries == null ? new ArrayList<CostBearingUnitEntry>() : entries;
		setRowCount(this.entries.size());
	}
	
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
