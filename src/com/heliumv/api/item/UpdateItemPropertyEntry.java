package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.system.UpdatePropertyEntry;

@XmlRootElement
public class UpdateItemPropertyEntry extends UpdatePropertyEntry {

	private Integer itemId;
	private String itemCnr;
	
	public UpdateItemPropertyEntry() {
	}

	/**
	 * Id des Artikels dieser Eigenschaft (optional)</br>
	 * Ist auch die Artikelnummer angegeben hat die Id Priorit&auml;t
	 * @return
	 */
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * Artikelnummer des Artikels dieser Eigenschaft (optional)
	 * @return
	 */
	public String getItemCnr() {
		return itemCnr;
	}
	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}
}
