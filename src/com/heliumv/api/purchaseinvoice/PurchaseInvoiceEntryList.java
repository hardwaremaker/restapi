package com.heliumv.api.purchaseinvoice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurchaseInvoiceEntryList {
	private List<PurchaseInvoiceEntry> entries;
	private long rowCount;
	
	public PurchaseInvoiceEntryList() {
		this(new ArrayList<PurchaseInvoiceEntry>());
	}
	
	public PurchaseInvoiceEntryList(ArrayList<PurchaseInvoiceEntry> entries) {
		setEntries(entries);
	}

	public List<PurchaseInvoiceEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<PurchaseInvoiceEntry> entries) {
		this.entries = entries;
		setRowCount(this.entries == null ? 0l : this.entries.size());
	}	

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
