package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class OpenWorkEntry extends BaseEntryId {
	private String customerShortDescription ;
	private String abc ;
	private String productionCnr ;
	private String productionProjectNr ;
	private String partlistCnr ;
	private String partlistDescription ;
	private String partlistItemCnr ;
	private String partlistItemDescription ;
	private String partlistItemShortDescription ;
	
	private Integer workNumber ;
	private String workItemCnr ;
	private String workItemDescription ;
	private String workItemShortDescription ;
	
	private Long workItemStartDate ;
	private Integer workItemStartCW ;
	private Integer workItemStartCY ;
	private Integer machineOffsetMs ;
	
	private BigDecimal duration ;
	
	private String machineCnr ;
	private String machineDescription ;
	
	private String materialCnr ;
	private String materialDescription ;
	
	private Boolean hasWorktime ;
	private Integer machineId ;
	
	private Integer orderId ;
	private String orderCnr ;
	private boolean overdue ;
	private Long orderFinalDateMs ;
	private BigDecimal progressPercent ;
	private BigDecimal targetDuration ;
	
	private boolean starttimeMoveable;
	private boolean finishtimeMoveable;
	private BigDecimal actualTime;
	private BigDecimal openQuantity;
	
	private Long productionFinalDateMs;
	private Integer priority;
	
	public String getProductionCnr() {
		return productionCnr;
	}

	public void setProductionCnr(String productionCnr) {
		this.productionCnr = productionCnr;
	}

	public String getProductionProjectNr() {
		return productionProjectNr ;
	}
	
	public void setProductionProjectNr(String productionProjectNr) {
		this.productionProjectNr = productionProjectNr ;
	}

	public String getPartlistCnr() {
		return partlistCnr;
	}

	public void setPartlistCnr(String partlistCnr) {
		this.partlistCnr = partlistCnr;
	}

	public String getPartlistDescription() {
		return partlistDescription;
	}

	public void setPartlistDescription(String partlistDescription) {
		this.partlistDescription = partlistDescription;
	}

	public Integer getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(Integer workNumber) {
		this.workNumber = workNumber;
	}

	public String getWorkItemCnr() {
		return workItemCnr;
	}

	public void setWorkItemCnr(String workItemCnr) {
		this.workItemCnr = workItemCnr;
	}

	public String getWorkItemDescription() {
		return workItemDescription;
	}

	public void setWorkItemDescription(String workItemDescription) {
		this.workItemDescription = workItemDescription;
	}

	public String getWorkItemShortDescription() {
		return workItemShortDescription;
	}

	public void setWorkItemShortDescription(String workItemShortDescription) {
		this.workItemShortDescription = workItemShortDescription;
	}
	
	public Long getWorkItemStartDate() {
		return workItemStartDate;
	}

	public void setWorkItemStartDate(Long workItemStartDate) {
		this.workItemStartDate = workItemStartDate;
	}

	public Integer getWorkItemStartCalendarWeek() {
		return workItemStartCW;
	}

	public void setWorkItemStartCalendarWeek(Integer calendarWeek) {
		this.workItemStartCW = calendarWeek;
	}

	public Integer getWorkItemStartCalendarYear() {
		return workItemStartCY;
	}
	
	public void setWorkItemStartCalendarYear(Integer year) {
		this.workItemStartCY = year;
	}

	public Integer getMachineOffsetMs() {
		return machineOffsetMs;
	}

	public void setMachineOffsetMs(Integer machineOffsetMs) {
		this.machineOffsetMs = machineOffsetMs;
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public String getMachineCnr() {
		return machineCnr;
	}

	public void setMachineCnr(String machineCnr) {
		this.machineCnr = machineCnr;
	}

	public String getMachineDescription() {
		return machineDescription;
	}

	public void setMachineDescription(String machineDescription) {
		this.machineDescription = machineDescription;
	}

	public String getMaterialCnr() {
		return materialCnr;
	}

	public void setMaterialCnr(String materialCnr) {
		this.materialCnr = materialCnr;
	}

	public String getMaterialDescription() {
		return materialDescription;
	}

	public void setMaterialDescription(String materialDescription) {
		this.materialDescription = materialDescription;
	}

	public Boolean getHasWorktime() {
		return hasWorktime;
	}

	public void setHasWorktime(Boolean hasWorktime) {
		this.hasWorktime = hasWorktime;
	}

	public String getCustomerShortDescription() {
		return customerShortDescription;
	}

	public void setCustomerShortDescription(String customerShortDescription) {
		this.customerShortDescription = customerShortDescription;
	}

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderCnr() {
		return orderCnr;
	}

	public void setOrderCnr(String orderCnr) {
		this.orderCnr = orderCnr;
	}

	public boolean isOverdue() {
		return overdue;
	}

	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}

	public Long getOrderFinalDateMs() {
		return orderFinalDateMs;
	}

	public void setOrderFinalDateMs(Long orderFinalDateMs) {
		this.orderFinalDateMs = orderFinalDateMs;
	}

	public BigDecimal getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(BigDecimal progressPercent) {
		this.progressPercent = progressPercent;
	}

	public BigDecimal getTargetDuration() {
		return targetDuration;
	}

	public void setTargetDuration(BigDecimal targetDuration) {
		this.targetDuration = targetDuration;
	}

	public String getPartlistItemCnr() {
		return partlistItemCnr;
	}

	public void setPartlistItemCnr(String partlistItemCnr) {
		this.partlistItemCnr = partlistItemCnr;
	}

	public String getPartlistItemDescription() {
		return partlistItemDescription;
	}

	public void setPartlistItemDescription(String partlistItemDescription) {
		this.partlistItemDescription = partlistItemDescription;
	}

	public String getPartlistItemShortDescription() {
		return partlistItemShortDescription;
	}

	public void setPartlistItemShortDescription(
			String partlistItemShortDescription) {
		this.partlistItemShortDescription = partlistItemShortDescription;
	}

	/**
	 * Die bisher angefallene Ist-Zeit f&uuml;r diesen Arbeitsschritt/Arbeitsgang
	 * @return
	 */
	public BigDecimal getActualTime() {
		return actualTime;
	}

	public void setActualTime(BigDecimal actualTime) {
		this.actualTime = actualTime;
	}

	/**
	 * L&auml;sst sich die Startzeit verschieben?
	 * 
	 * @return true wenn sich die Startzeit verschieben l&auml;&szlig;t. Das ist
	 *  bei einem Arbeitsgang (eines Loses) dann der Fall, wenn es sich um den ersten
	 *  Arbeitsgang handelt, und im zugeh&ouml;rigen Los bisher keine Arbeitsg&auml;nge
	 *  als Fertig markiert sind oder einen Fortschritt haben. 
	 */
	public boolean getStarttimeMoveable() {
		return starttimeMoveable;
	}

	public void setStarttimeMoveable(boolean starttimeMoveable) {
		this.starttimeMoveable = starttimeMoveable;
	}

	/**
	 * L&auml;sst sich die Endezeit verschieben?
	 * 
	 * @return true wenn sich die Endezeit verschieben l&auml;&szlig;t. Das ist
	 *  bei einem Arbeitsgang (eines Loses) dann der Fall, wenn es sich um den letzten 
	 *  Arbeitsgang handelt, und im zugeh&ouml;rigen Los bisher keine Arbeitsg&auml;nge
	 *  als Fertig markiert sind oder einen Fortschritt haben. 
	 */
	public boolean getFinishtimeMoveable() {
		return finishtimeMoveable;
	}

	public void setFinishtimeMoveable(boolean finishtimeMoveable) {
		this.finishtimeMoveable = finishtimeMoveable;
	}

	public BigDecimal getOpenQuantity() {
		return openQuantity;
	}

	public void setOpenQuantity(BigDecimal openQuantity) {
		this.openQuantity = openQuantity;
	}

	public Long getProductionFinalDateMs() {
		return productionFinalDateMs;
	}
	
	public void setProductionFinalDateMs(Long productionFinalDateMs) {
		this.productionFinalDateMs = productionFinalDateMs;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Integer getPriority() {
		return priority;
	}
}
