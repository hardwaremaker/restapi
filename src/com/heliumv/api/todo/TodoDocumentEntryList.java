package com.heliumv.api.todo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoDocumentEntryList {
	private List<TodoDocumentEntry> entries;
	private long rowCount;
	
	public TodoDocumentEntryList() {
		this(new ArrayList<TodoDocumentEntry>());
	}
	
	public TodoDocumentEntryList(List<TodoDocumentEntry> entries) {
		this.setEntries(entries);
		setRowCount(entries.size());
	}

	/**
	 * Die Anzahl der Dokumente
	 * @return die Anzahl der Dokumente
	 */
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * Die (leere) Liste aller Dokumente eines Todos.
	 * @return die (leere) Liste der Dokumente eines Todos
	 */
	public List<TodoDocumentEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TodoDocumentEntry> entries) {
		this.entries = entries;
	}
	
	public void add(TodoDocumentEntry entry) {
		if(entry != null) {
			this.entries.add(entry);
			this.rowCount++;
		}
	}
}
