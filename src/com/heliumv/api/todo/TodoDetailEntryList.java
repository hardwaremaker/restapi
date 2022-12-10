package com.heliumv.api.todo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoDetailEntryList {
	private List<TodoDetailEntry> entries ;
	private long rowCount ;
	
	public TodoDetailEntryList() {
		this(new ArrayList<TodoDetailEntry>()) ;
	}
	
	public TodoDetailEntryList(List<TodoDetailEntry> entries) {
		this.entries = entries ;
		setRowCount(this.entries.size());
	}
	
	/**
	 * Die (leere) Liste aller Todoeintr&auml;ge
	 * @return die (leere) Liste aller Todoeintr&auml;ge
	 */
	public List<TodoDetailEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TodoDetailEntry> entries) {
		this.entries = entries;
	}

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
