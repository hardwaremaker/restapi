package com.heliumv.api.worktime;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SpecialTimesEntryList {
	private List<SpecialTimesEntry> entries;
	private long rowCount;
	
	public SpecialTimesEntryList() {
		this(new ArrayList<SpecialTimesEntry>());
	}
	
	public SpecialTimesEntryList(List<SpecialTimesEntry> entries) {
		setEntries(entries);
	}

	/**
	 * Die (leere) Liste aller Sonderzeiten-Eintr&auml;ge
	 * @return die (leere) Liste aller Sonderzeiten Eintr&auml;ge
	 */
	public List<SpecialTimesEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<SpecialTimesEntry> entries) {
		if(entries == null) {
			entries = new ArrayList<SpecialTimesEntry>();
		}
		this.entries = entries;
		setRowCount(entries.size());
	}
	
	/**
	 * Die Anzahl aller Datens&auml;tze</br>
	 * <p>Die hier gelieferte Anzahl muss nicht zwangsl&auml;ufig mit der
	 * Anzahl der Listenelemente &uuml;bereinstimmen</p>
	 * @return die Anzahl der Datens&auml;tze
	 */
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}

}
