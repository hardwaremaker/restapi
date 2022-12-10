package com.heliumv.api.property;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemPropertyEntryList;

@XmlRootElement
public class CreateItemIdentityPropertyEntryList extends ItemPropertyEntryList {
	private String identity;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
