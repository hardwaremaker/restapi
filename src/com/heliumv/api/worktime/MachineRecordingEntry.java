package com.heliumv.api.worktime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MachineRecordingEntry extends TimeRecordingEntry {

	private Integer machineId;
	private Integer productionWorkplanId;
	private MachineRecordingType machineRecordingType;
	private String remark;
	
	public MachineRecordingEntry() {
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public Integer getProductionWorkplanId() {
		return productionWorkplanId;
	}

	public void setProductionWorkplanId(Integer productionWorkplanId) {
		this.productionWorkplanId = productionWorkplanId;
	}

	public MachineRecordingType getMachineRecordingType() {
		return machineRecordingType;
	}

	public void setMachineRecordingType(MachineRecordingType machineRecordingType) {
		this.machineRecordingType = machineRecordingType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
