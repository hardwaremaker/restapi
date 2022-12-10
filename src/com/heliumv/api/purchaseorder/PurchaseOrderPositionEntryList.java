package com.heliumv.api.purchaseorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurchaseOrderPositionEntryList {
	private List<PurchaseOrderPositionEntry> entries;
	
	public PurchaseOrderPositionEntryList() {
		this(new ArrayList<PurchaseOrderPositionEntry>());
	}
	
	public PurchaseOrderPositionEntryList(List<PurchaseOrderPositionEntry> entries) {
		setEntries(entries);
	}
	
	public List<PurchaseOrderPositionEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<PurchaseOrderPositionEntry> entries) {
		this.entries = entries;
	}
}
