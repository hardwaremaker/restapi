package com.heliumv.api.todo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.hvma.HvmaParamEntry;
import com.heliumv.types.JudgeEnum;
import com.lp.server.personal.service.HvmaRechtEnum;
import com.lp.util.barcode.HvBarcodeDecoder;

@XmlRootElement
public class TodoEntryList {
	private List<TodoEntry> entries ;
	private long rowCount ;
	private Integer serverId;
	private Integer barcodeLength;
	private Integer barcodeLengthOrderRelated;
	private List<HvmaRechtEnum> hvmaEnums;
	private List<HvmaParamEntry> hvmaParams;
	private List<JudgeEnum> judgeEnums;
	
	public TodoEntryList() {
		this(new ArrayList<TodoEntry>()) ;
	}
	
	public TodoEntryList(List<TodoEntry> entries) {
		setEntries(entries);
	}
	
	public TodoEntryList(List<TodoEntry> entries, Integer serverId, HvBarcodeDecoder decoder) {
		setEntries(entries);
		setServerId(serverId);
		setBarcodeLength(decoder.getCnrLaenge());
		setBarcodeLengthOrderRelated(decoder.getCnrLaengeAuftragsbezogen());
	}
	
	/**
	 * Die (leere) Liste aller Todoeintr&auml;ge
	 * @return die (leere) Liste aller Todoeintr&auml;ge
	 */
	public List<TodoEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TodoEntry> entries) {
		this.entries = entries ;
		setRowCount(this.entries.size());
	}

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public Integer getBarcodeLength() {
		return barcodeLength;
	}

	public void setBarcodeLength(Integer barcodeLength) {
		this.barcodeLength = barcodeLength;
	}

	public Integer getBarcodeLengthOrderRelated() {
		return barcodeLengthOrderRelated;
	}

	public void setBarcodeLengthOrderRelated(Integer barcodeLengthOrderRelated) {
		this.barcodeLengthOrderRelated = barcodeLengthOrderRelated;
	}

	public List<HvmaRechtEnum> getHvmaEnums() {
		return hvmaEnums;
	}

	public void setHvmaEnums(List<HvmaRechtEnum> hvmaEnums) {
		this.hvmaEnums = hvmaEnums;
	}

	public List<HvmaParamEntry> getHvmaParams() {
		return hvmaParams;
	}

	public void setHvmaParams(List<HvmaParamEntry> hvmaParams) {
		this.hvmaParams = hvmaParams;
	}

	public List<JudgeEnum> getJudgeEnums() {
		return judgeEnums;
	}

	public void setJudgeEnums(List<JudgeEnum> judgeEnums) {
		this.judgeEnums = judgeEnums;
	}
}
