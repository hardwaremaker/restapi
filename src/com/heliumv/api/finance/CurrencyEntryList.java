package com.heliumv.api.finance;

import java.util.ArrayList;
import java.util.List;

public class CurrencyEntryList {
	private List<CurrencyEntry> entries;
	private long rowCount; 
	
	public CurrencyEntryList() {
		this(new ArrayList<CurrencyEntry>());
	}
	
	public CurrencyEntryList(List<CurrencyEntry> entries) {
		this.entries = entries;
		setRowCount(this.entries != null ? this.entries.size() : 0l);
	}

	/**
	 * Die Liste aller W&auml;hrungen
	 * @return die (leere) Liste aller W&auml;hrungen 
	 */
	public List<CurrencyEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<CurrencyEntry> entries) {
		this.entries = entries;
	}

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
