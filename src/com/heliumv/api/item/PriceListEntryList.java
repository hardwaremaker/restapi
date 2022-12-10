package com.heliumv.api.item;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PriceListEntryList {
	private List<PriceListEntry> entries;
	private long rowCount;
	
	public PriceListEntryList() {
		this(new ArrayList<PriceListEntry>());
	}
	
	public PriceListEntryList(List<PriceListEntry> entries) {
		
	}

	public List<PriceListEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PriceListEntry> entries) {
		this.entries = entries;
		setRowCount(entries == null ? 0l : entries.size());
	}
	
	/**
	 * Die Anzahl von Eintr&auml;gen in der Liste
	 * @return die Anzahl der Elemente der Liste
	 */
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
