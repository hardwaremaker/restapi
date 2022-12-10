package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxDescriptionEntryList {
	private List<TaxDescriptionEntry> entries;
	private long rowCount;
	
	public TaxDescriptionEntryList() {
		this(new ArrayList<TaxDescriptionEntry>());
	}
	
	public TaxDescriptionEntryList(List<TaxDescriptionEntry> entries) {
		setEntries(entries);
	}
	
	public List<TaxDescriptionEntry> getEntries() {
		return this.entries;
	}
	
	public void setEntries(List<TaxDescriptionEntry> entries) {
		this.entries = entries == null ? new ArrayList<TaxDescriptionEntry>() : entries;
		setRowCount(this.entries.size());
	}
	
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
