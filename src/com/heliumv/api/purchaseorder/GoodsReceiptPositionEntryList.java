package com.heliumv.api.purchaseorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GoodsReceiptPositionEntryList {
	private List<GoodsReceiptPositionEntry> entries;
	
	public GoodsReceiptPositionEntryList() { 
		this(new ArrayList<GoodsReceiptPositionEntry>());
	}

	public GoodsReceiptPositionEntryList(List<GoodsReceiptPositionEntry> entries) {
		setEntries(entries);
	}
	
	
	/**
	 * Die (leere) Liste aller Wareneing&auml;nge
	 * @return die (leere) Liste aller Wareneing&auml;nge
	 */
	public List<GoodsReceiptPositionEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<GoodsReceiptPositionEntry> entries) {
		this.entries = entries;
	}
}
