package com.heliumv.api.invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoicePaymentEntryList {
	private List<InvoicePaymentEntry> entries ;
	
	public InvoicePaymentEntryList() {
		entries = new ArrayList<InvoicePaymentEntry>() ;
	}

	public InvoicePaymentEntryList(List<InvoicePaymentEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Die Liste aller <code>InvoicePaymentEntry</code> Eintr&auml;ge
	 * @return die (leere) Liste aller Zahlungen einer Rechnung
	 */
	public List<InvoicePaymentEntry> getEntries() {
		return entries ;
	}
	
	public void setEntries(List<InvoicePaymentEntry> entries) {
		this.entries = entries ;
	}
}
