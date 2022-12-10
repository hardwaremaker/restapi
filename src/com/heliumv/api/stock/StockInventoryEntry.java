package com.heliumv.api.stock;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class StockInventoryEntry extends BaseEntryId {
	private String cnr;
	private StockInfoItemEntryList entries;
	
	/**
	 * Lager-"Name"
	 * @return der Lagername (Hauptlager, Lager Werkstatt, ...)
	 */
	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	public StockInfoItemEntryList getEntries() {
		return entries;
	}

	public void setEntries(StockInfoItemEntryList entries) {
		this.entries = entries;
	}
}
