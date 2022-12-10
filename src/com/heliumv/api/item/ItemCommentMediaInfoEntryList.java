package com.heliumv.api.item;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemCommentMediaInfoEntryList {

	private List<ItemCommentMediaInfoEntry> entries;
	
	public ItemCommentMediaInfoEntryList() {
		setEntries(new ArrayList<ItemCommentMediaInfoEntry>());
	}

	public List<ItemCommentMediaInfoEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<ItemCommentMediaInfoEntry> entries) {
		this.entries = entries;
	}
}
