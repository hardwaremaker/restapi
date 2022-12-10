package com.heliumv.api.document;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DocumentInfoEntryList {

	private List<DocumentInfoEntry> entries;
	
	public DocumentInfoEntryList() {
		setEntries(new ArrayList<DocumentInfoEntry>());
	}

	public void setEntries(List<DocumentInfoEntry> entries) {
		this.entries = entries;
	}
	
	public List<DocumentInfoEntry> getEntries() {
		return entries;
	}
}
