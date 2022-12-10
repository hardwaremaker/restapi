package com.heliumv.api.machine;

import com.heliumv.api.BaseEntryId;

public class MachineEntry extends BaseEntryId {
	private String inventoryNumber ;
	private String description ;
	private String identificationNumber ;
	private Integer machineGroupId ;
	private String machineGroupDescription ;
	private Integer personalIdStarter;
	private Long starttime;
	private Integer productionWorkplanId;
	private String machineGroupShortDescription;
	private Integer machineGroupISort;
	
	public MachineEntry() {
	}

	public MachineEntry(Object flrId) {
		super((Integer) flrId) ;
	}
	
	public String getInventoryNumber() {
		return inventoryNumber;
	}
	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public Integer getMachineGroupId() {
		return machineGroupId;
	}

	public void setMachineGroupId(Integer machineGroupId) {
		this.machineGroupId = machineGroupId;
	}

	public String getMachineGroupDescription() {
		return machineGroupDescription;
	}

	public void setMachineGroupDescription(String machineGroupDescription) {
		this.machineGroupDescription = machineGroupDescription;
	}

	public Integer getPersonalIdStarter() {
		return personalIdStarter;
	}

	public void setPersonalIdStarter(Integer personalIdStarter) {
		this.personalIdStarter = personalIdStarter;
	}

	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Integer getProductionWorkplanId() {
		return productionWorkplanId;
	}

	public void setProductionWorkplanId(Integer productionWorkplanId) {
		this.productionWorkplanId = productionWorkplanId;
	}	
	
	public String getMachineGroupShortDescription() {
		return machineGroupShortDescription;
	}
	
	public void setMachineGroupShortDescription(String machineGroupShortDescription) {
		this.machineGroupShortDescription = machineGroupShortDescription;
	}
	
	public Integer getMachineGroupISort() {
		return machineGroupISort;
	}
	
	public void setMachineGroupISort(Integer machineGroupISort) {
		this.machineGroupISort = machineGroupISort;
	}
}
