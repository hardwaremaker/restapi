package com.heliumv.api.traveltime;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TravelTimeRecordingBatchEntryList {
	private List<TravelTimeRecordingBatchEntry> entries;
	private long rowCount;
	
	public TravelTimeRecordingBatchEntryList() {
		this(new ArrayList<TravelTimeRecordingBatchEntry>());
	}
	
	public TravelTimeRecordingBatchEntryList(List<TravelTimeRecordingBatchEntry> entries) {
		setEntries(entries);
	}

	public List<TravelTimeRecordingBatchEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TravelTimeRecordingBatchEntry> entries) {
		this.entries = entries;
		setRowCount(entries == null ? 0 : entries.size());
	}

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
