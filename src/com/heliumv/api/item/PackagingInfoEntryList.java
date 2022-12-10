package com.heliumv.api.item;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PackagingInfoEntryList {
	private List<PackagingInfoEntry> entries ;
	
	public PackagingInfoEntryList() {
		entries = new ArrayList<PackagingInfoEntry>() ;
	}
	
	public PackagingInfoEntryList(List<PackagingInfoEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Die Liste aller <code>packagingInfoEntry</code> Eintr&auml;ge
	 * @return die (leere) Liste aller PackagingInfoEntry Eintr&auml;ge
	 */
	public List<PackagingInfoEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PackagingInfoEntry> entries) {
		this.entries = entries;
	}	
}
