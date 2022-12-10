package com.heliumv.api.customer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactEntryList {

	private List<ContactEntry> contactEntries;
	
	public ContactEntryList() {
		setContactEntries(new ArrayList<ContactEntry>());
	}

	public List<ContactEntry> getContactEntries() {
		return contactEntries;
	}
	
	public void setContactEntries(List<ContactEntry> contactEntries) {
		this.contactEntries = contactEntries;
	}
}
