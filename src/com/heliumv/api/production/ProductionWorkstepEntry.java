package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemV1Entry;

@XmlRootElement
public class ProductionWorkstepEntry extends BaseEntryId {

	private Integer workstepNumber;
	private Long setupTimeMs;
	private Long jobTimeMs;
	private String comment;
	private ItemV1Entry itemEntry;
	private Integer machineId;
	private String text;
	private String workstepType;
	private Boolean hasFinished;
	private BigDecimal duration;
	
	public Integer getWorkstepNumber() {
		return workstepNumber;
	}
	public void setWorkstepNumber(Integer workstepNumber) {
		this.workstepNumber = workstepNumber;
	}
	public Long getSetupTimeMs() {
		return setupTimeMs;
	}
	public void setSetupTimeMs(Long setupTimeMs) {
		this.setupTimeMs = setupTimeMs;
	}
	public Long getJobTimeMs() {
		return jobTimeMs;
	}
	public void setJobTimeMs(Long jobTimeMs) {
		this.jobTimeMs = jobTimeMs;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ItemV1Entry getItemEntry() {
		return itemEntry;
	}
	public void setItemEntry(ItemV1Entry itemEntry) {
		this.itemEntry = itemEntry;
	}
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getWorkstepType() {
		return workstepType;
	}
	public void setWorkstepType(String workstepType) {
		this.workstepType = workstepType;
	}
	public Boolean getHasFinished() {
		return hasFinished;
	}
	public void setHasFinished(Boolean hasFinished) {
		this.hasFinished = hasFinished;
	}
	public BigDecimal getDuration() {
		return duration;
	}
	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}
	
}
