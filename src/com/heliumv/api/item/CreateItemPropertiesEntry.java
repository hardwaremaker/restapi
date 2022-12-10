package com.heliumv.api.item;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.system.CreatePropertyEntry;

@XmlRootElement
public class CreateItemPropertiesEntry {
	
	private Integer itemId;
	private String itemCnr;
	private List<CreatePropertyEntry> propertyEntries;
	
	public CreateItemPropertiesEntry() {
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

	/**
	 * Liste der zu erzeugenden Eigenschaften
	 * @return
	 */
	public List<CreatePropertyEntry> getPropertyEntries() {
		return propertyEntries;
	}
	
	public void setPropertyEntries(List<CreatePropertyEntry> propertyEntries) {
		this.propertyEntries = propertyEntries;
	}
}
