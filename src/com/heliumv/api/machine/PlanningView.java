package com.heliumv.api.machine;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.production.OpenWorkEntryList;
import com.heliumv.api.staff.WorkCalendarEntryList;
import com.heliumv.factory.IParameterCall.AuslastungsAnzeigeDetailAg;

@XmlRootElement
public class PlanningView {
	private MachineEntryList machineList ;
	private Map<Integer, MachineAvailabilityEntryList> machineAvailabilityMap ;
	private OpenWorkEntryList openWorkList ;
	private MachineGroupEntryList machineGroupList ;
	private WorkCalendarEntryList holidayList ;
	private WorkCalendarEntryList plantHolidayList ;
	private AuslastungsAnzeigeDetailAg viewOpenWorkDetail ;
	private boolean judgeWorkUnitChange ;
	private int dispatchingGridMinutes;
	private int dispatchingBufferMinutes;
	
	public MachineEntryList getMachineList() {
		return machineList;
	}
	public void setMachineList(MachineEntryList machineList) {
		this.machineList = machineList;
	}
	
	public OpenWorkEntryList getOpenWorkList() {
		return openWorkList;
	}
	public void setOpenWorkList(OpenWorkEntryList openWorkList) {
		this.openWorkList = openWorkList;
	}

	public Map<Integer, MachineAvailabilityEntryList> getMachineAvailabilityMap() {
		return machineAvailabilityMap;
	}
	public void setMachineAvailabilityMap(
			Map<Integer, MachineAvailabilityEntryList> machineAvailabilityMap) {
		this.machineAvailabilityMap = machineAvailabilityMap;
	}
	
	public MachineGroupEntryList getMachineGroupList() {
		return machineGroupList;
	}
	public void setMachineGroupList(MachineGroupEntryList machineGroupList) {
		this.machineGroupList = machineGroupList;
	}
	
	public WorkCalendarEntryList getHolidayList() {
		return holidayList;
	}
	public void setHolidayList(WorkCalendarEntryList holidayList) {
		this.holidayList = holidayList;
	}
	
	public WorkCalendarEntryList getPlantHolidayList() {
		return plantHolidayList;
	}
	public void setPlantHolidayList(WorkCalendarEntryList plantHolidayList) {
		this.plantHolidayList = plantHolidayList;
	}
	
	public AuslastungsAnzeigeDetailAg getViewOpenWorkDetail() {
		return viewOpenWorkDetail;
	}
	public void setViewOpenWorkDetail(AuslastungsAnzeigeDetailAg viewOpenWorkDetail) {
		this.viewOpenWorkDetail = viewOpenWorkDetail;
	}
	public boolean isJudgeWorkUnitChange() {
		return judgeWorkUnitChange;
	}
	public void setJudgeWorkUnitChange(boolean judgeWorkUnitChange) {
		this.judgeWorkUnitChange = judgeWorkUnitChange;
	}
	public int getDispatchingGridMinutes() {
		return dispatchingGridMinutes;
	}
	public void setDispatchingGridMinutes(int dispatchingGridMinutes) {
		this.dispatchingGridMinutes = dispatchingGridMinutes;
	}
	public int getDispatchingBufferMinutes() {
		return dispatchingBufferMinutes;
	}
	public void setDispatchingBufferMinutes(int dispatchingBufferMinutes) {
		this.dispatchingBufferMinutes = dispatchingBufferMinutes;
	}
}
