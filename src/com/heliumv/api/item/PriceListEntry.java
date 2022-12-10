package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class PriceListEntry extends BaseEntryId {
	private String cnr;
	private String currency;
	private boolean active;
	
	/**
	 * @return die Kennung der Preisliste
	 */
	public String getCnr() {
		return cnr;
	}
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	
	/**
	 * @return die W&auml;hrung der Preisliste
	 */
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * @return true wenn die Preisliste aktiv ist
	 */
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
