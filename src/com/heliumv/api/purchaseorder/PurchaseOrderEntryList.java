package com.heliumv.api.purchaseorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurchaseOrderEntryList {
	private List<PurchaseOrderEntry> entries;
	private long rowCount;
	
	public PurchaseOrderEntryList() {
		setEntries(new ArrayList<PurchaseOrderEntry>());
	}

	public PurchaseOrderEntryList(List<PurchaseOrderEntry> entries) {
		setEntries(entries);
	}
	
	/**
	 * Die Liste aller <code>PurchaseOrderEntry</> Eintr&auml;ge
	 * @return die Liste aller PurchaseOrderEntry
	 */
	public List<PurchaseOrderEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PurchaseOrderEntry> entries) {
		this.entries = entries;
		setRowCount(entries == null ? 0 : entries.size());
	}

	/**
	 * Die Anzahl der Eintr&auml;ge
	 * @return die Anzahl der Eintr&auml;ge
	 */
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
