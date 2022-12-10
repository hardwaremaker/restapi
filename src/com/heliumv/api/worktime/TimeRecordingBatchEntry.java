package com.heliumv.api.worktime;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.todo.TodoEntryType;

@XmlRootElement
public class TimeRecordingBatchEntry extends TimeRecordingEntry {
	private Integer hvid;
	private TimeRecordingBatchEnum recordingEnum;
	private TodoEntryType todoType;
	private Integer hvDetailId;
	private String remark;
	private String detailItemCnr;
	
	public TimeRecordingBatchEnum getRecordingEnum() {
		return recordingEnum;
	}
	public void setRecordingEnum(TimeRecordingBatchEnum recordingEnum) {
		this.recordingEnum = recordingEnum;
	}	
	
	public Integer getHVID() {
		return hvid;
	}
	public void setHVID(Integer hvid) {
		this.hvid = hvid;
	}
	
	public TodoEntryType getTodoType() {
		return todoType;
	}
	public void setTodoType(TodoEntryType todoType) {
		this.todoType = todoType;
	}
	public Integer getHvDetailId() {
		return hvDetailId;
	}
	public void setHvDetailId(Integer hvDetailId) {
		this.hvDetailId = hvDetailId;
	}
	
	/** 
	 * Der maximal 80 Zeichen lange Kommentar zur Belegzeitbuchung</br>
	 * <p>Gelangt in das kurze Bemerkungsfeld </p>
	 * @return der (kurz) Kommentar zur Belegzeitbuchung
	 */
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * Optionaler T&auml;tigkeitsartikel</br>
	 * <p>Wird der Artikel angegeben, wird dieser anstatt der 
	 * eventuell gesetzten <code>hvDetailId</code> verwendet.
	 * @return der (optionale) Taetigkeitsartikel
	 */
	public String getDetailItemCnr() {
		return detailItemCnr;
	}
	public void setDetailItemCnr(String detailItemCnr) {
		this.detailItemCnr = detailItemCnr;
	}
}
 