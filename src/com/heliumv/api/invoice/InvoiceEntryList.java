package com.heliumv.api.invoice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InvoiceEntryList {
	private List<InvoiceEntry> entries ;
	
	public InvoiceEntryList() {
		entries = new ArrayList<InvoiceEntry>() ;
	}

	public InvoiceEntryList(List<InvoiceEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Die Liste aller <code>InvoiceEntry</code> Eintr&auml;ge
	 * @return die (leere) Liste aller Rechnungen
	 */
	public List<InvoiceEntry> getEntries() {
		return entries ;
	}
	
	public void setEntries(List<InvoiceEntry> entries) {
		this.entries = entries ;
	}
}
