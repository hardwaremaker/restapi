package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockInfoEntryList {

	private List<StockInfoEntry> entries;
	
	public StockInfoEntryList() {
		setEntries(new ArrayList<StockInfoEntry>());
	}
	
	public void setEntries(List<StockInfoEntry> entries) {
		this.entries = entries;
	}
	
	/**
	 * Enth&auml;lt Lager- und Lagerplatzinformationen<br \> 
	 * Es sind nur jene Lager enthalten, auf die der angemeldete Benutzer Zugriff hat
	 * 
	 * @return eine (leere) Liste der Lager- und Lagerplatzeintr&auml;ge
	 */
	public List<StockInfoEntry> getEntries() {
		return entries;
	}
}
