package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockMovementEntryList {
	private List<StockMovementEntry> entries;
	
	public StockMovementEntryList() {
		this(new ArrayList<StockMovementEntry>());
	}
	
	public StockMovementEntryList(List<StockMovementEntry> entries) {
		setEntries(entries);
	}  
	
	public void setEntries(List<StockMovementEntry> entries) {
		this.entries = entries == null ? new ArrayList<StockMovementEntry>() : entries;
	}

	/**
	 * Die Lagerbewegungen</br>
	 * @return (leere) Liste der Lagerbewegungen
	 */
	public List<StockMovementEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Die Anzahl der Lagerbewegungen</br>
	 * @return Anzahl der Lagerbewegungen
	 */	
	public int getRowCount() {
		return entries.size();
	}
}
