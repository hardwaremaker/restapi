package com.heliumv.api.purchaseorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GoodsReceiptEntryList {
	private List<GoodsReceiptEntry> entries;
	
	public GoodsReceiptEntryList() { 
		this(new ArrayList<GoodsReceiptEntry>());
	}

	public GoodsReceiptEntryList(List<GoodsReceiptEntry> entries) {
		setEntries(entries);
	}
	
	
	/**
	 * Die (leere) Liste aller Wareneing&auml;nge
	 * @return die (leere) Liste aller Wareneing&auml;nge
	 */
	public List<GoodsReceiptEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<GoodsReceiptEntry> entries) {
		this.entries = entries != null ? entries : new ArrayList<GoodsReceiptEntry>();
	}
}
