package com.heliumv.api.purchaseorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurchaseOrderProposalPositionEntryList {
	
	private List<PurchaseOrderProposalPositionEntry> entries;
	private Integer rowCount;

	public PurchaseOrderProposalPositionEntryList() {
		this(new ArrayList<PurchaseOrderProposalPositionEntry>());
	}

	public PurchaseOrderProposalPositionEntryList(List<PurchaseOrderProposalPositionEntry> entries) {
		setEntries(entries);
	}
	
	/**
	 * Die Liste aller <code>PurchaseOrderProposalPositionEntry</code> Eintr&auml;ge
	 * @return die Liste aller PurchaseOrderEntry
	 */
	public List<PurchaseOrderProposalPositionEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<PurchaseOrderProposalPositionEntry> entries) {
		this.entries = entries;
	}
	
	/**
	 * Die Anzahl der Eintr&auml;ge
	 * @return die Anzahl der Eintr&auml;ge
	 */
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
}
