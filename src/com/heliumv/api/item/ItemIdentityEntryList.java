package com.heliumv.api.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Liste der Chargen- bzw. Seriennummern</br>
 * 
 * @author gerold
 *
 */
public class ItemIdentityEntryList {
	private List<ItemIdentityEntry> entries ;
	
	public ItemIdentityEntryList() {
		setEntries(new ArrayList<ItemIdentityEntry>()) ;
	}
	
	public ItemIdentityEntryList(List<ItemIdentityEntry> entries) {
		setEntries(entries) ;
	}

	/**
	 * 
	 * @return die (leere) Liste der Identit&auml;ten dieses 
	 * Artikels. F&uuml;r einen chargennummernbehafteten Artikel die
	 * Chargennummer und die zugeh&ouml;rige Menge. F&uuml;r Seriennummern
	 * die Menge 1 und die jeweilige Seriennummer
	 */
	public List<ItemIdentityEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ItemIdentityEntry> entries) {
		this.entries = entries;
	}
}
