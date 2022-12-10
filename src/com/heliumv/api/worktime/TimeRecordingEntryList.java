package com.heliumv.api.worktime;

import java.util.ArrayList;
import java.util.List;

public class TimeRecordingEntryList {
	private List<TimeRecordingEntry> entries;
	private long rowCount;
	
	public TimeRecordingEntryList() {
		this(new ArrayList<TimeRecordingEntry>());
	}
	
	public TimeRecordingEntryList(List<TimeRecordingEntry> entries) {
		setEntries(entries);
	}
	
	/**
	 * Die (leere) Liste aller TimeRecordingEntry Eintr&auml;ge
	 * @return die (leere) Liste aller TimeRecordingEntry Eintr&auml;ge
	 */
	public List<TimeRecordingEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<TimeRecordingEntry> entries) {
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
