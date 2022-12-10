package com.heliumv.api.property;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemPropertyEntry;

@XmlRootElement
public class CreateItemIdentityPropertyEntry extends ItemPropertyEntry {
	private String identity;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
