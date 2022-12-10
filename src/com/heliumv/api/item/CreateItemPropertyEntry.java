package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.system.CreatePropertyEntry;

@XmlRootElement
public class CreateItemPropertyEntry extends CreatePropertyEntry {

	private Integer itemId;
	private String itemCnr;
	
	public CreateItemPropertyEntry() {
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
