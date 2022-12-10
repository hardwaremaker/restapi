package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CountryEntryList {
	private List<CountryEntry> entries;
	private long rowCount;
	
	public CountryEntryList() {
		this(new ArrayList<CountryEntry>());
	}

	public CountryEntryList(List<CountryEntry> entries) {
		this.entries = entries;
		setRowCount(this.entries != null ? this.entries.size() : 0l); 
	}
	
	/**
	 * Die Liste aller L&auml;nder
	 * @return die (leere) Liste aller L&auml;nder
	 */
	public List<CountryEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<CountryEntry> entries) {
		this.entries = entries;
	}
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
