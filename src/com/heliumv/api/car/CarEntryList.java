package com.heliumv.api.car;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CarEntryList {
	private List<CarEntry> entries;
	private long rowCount;
	
	public CarEntryList() {
		this(new ArrayList<CarEntry>());
	}
	
	public CarEntryList(List<CarEntry> entries) {
		this.setEntries(entries);
	}

	/**
	 * Die Liste aller CarEntry Eintr&auml;ge
	 * @return
	 */
	public List<CarEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<CarEntry> entries) {
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
