package com.heliumv.api.todo;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoDetailIdentEntry extends TodoDetailEntry {
	private Integer itemId;
	private String cnr;
	private String description;
	private String description2;
	private BigDecimal amount;
	private String unitCnr;
	private long dueDateMs ;
	private boolean recordable;
	private boolean documentObligation;
	private BigDecimal progressPercent;
	private String positionNr;
	private String subPositionNr;
	private boolean done;
	private TodoDetailTravelEnum travelInfo;
	
	public TodoDetailIdentEntry() {
		this.setDetailType(TodoDetailEntryType.IDENT);
	}
	
	public TodoDetailIdentEntry(Integer id) {
		super(id);
		this.setDetailType(TodoDetailEntryType.IDENT);
	}
		
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription2() {
		return description2;
	}
	public void setDescription2(String description2) {
		this.description2 = description2;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getUnitCnr() {
		return unitCnr;
	}
	public void setUnitCnr(String unitCnr) {
		this.unitCnr = unitCnr;
	}
	public long getDueDateMs() {
		return dueDateMs;
	}
	public void setDueDateMs(long dueDateMs) {
		this.dueDateMs = dueDateMs;
	}
	public String getItemCnr() {
		return cnr;
	}
	public void setItemCnr(String cnr) {
		this.cnr = cnr;
	}

	public boolean isRecordable() {
		return recordable;
	}

	public void setRecordable(boolean recordable) {
		this.recordable = recordable;
	}

	/**
	 * Dokumentenpflichtig?</br>
	 * <p>Handelt es sich beim zugrundeliegenden Artikel um einen 
	 * dokumentenpflichtigen Artikel?</p>
	 * @return true wenn es sich um einen dokumentenpflichtigen 
	 *   Artikel handelt
	 */
	public boolean isDocumentObligation() {
		return documentObligation;
	}

	public void setDocumentObligation(boolean documentObligation) {
		this.documentObligation = documentObligation;
	}

	public BigDecimal getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(BigDecimal progressPercent) {
		this.progressPercent = progressPercent;
	}

	public String getPositionNr() {
		return positionNr;
	}

	public void setPositionNr(String positionNr) {
		this.positionNr = positionNr;
	}

	public String getSubPositionNr() {
		return subPositionNr;
	}

	public void setSubPositionNr(String subPositionNr) {
		this.subPositionNr = subPositionNr;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public TodoDetailTravelEnum getTravelInfo() {
		return travelInfo;
	}

	public void setTravelInfo(TodoDetailTravelEnum travelInfo) {
		this.travelInfo = travelInfo;
	}
}
