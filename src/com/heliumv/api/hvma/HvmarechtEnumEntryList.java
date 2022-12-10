package com.heliumv.api.hvma;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.lp.server.personal.service.HvmaRechtEnum;

@XmlRootElement
public class HvmarechtEnumEntryList {
	private List<HvmaRechtEnum> entries;
	private long rowCount;
	
	public HvmarechtEnumEntryList() {
		this(new ArrayList<HvmaRechtEnum>());
	}
	
	public HvmarechtEnumEntryList(List<HvmaRechtEnum> enumEntries) {
		setEntries(enumEntries);
	}
	
	public void setEntries(List<HvmaRechtEnum> enumEntries) {
		this.entries = enumEntries;
		setRowCount(enumEntries == null ? 0l : enumEntries.size());
	}

	public List<HvmaRechtEnum> getEntries() {
		return this.entries;
	}
	
	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
}
