package com.heliumv.api.machine;

import java.util.List;

public class MachineQueryFilter {
	private Boolean filterWithHidden;
	private Integer productiongroupId;
	private Boolean filterPlanningView;
	private Integer filterStaffId;
	private List<Integer> filterInMachineIds;
	
	public MachineQueryFilter() {
		
	}
	
	public Boolean getFilterWithHidden() {
		return filterWithHidden;
	}
	public void setFilterWithHidden(Boolean filterWithHidden) {
		this.filterWithHidden = filterWithHidden;
	}
	public Integer getProductiongroupId() {
		return productiongroupId;
	}
	public void setProductiongroupId(Integer productiongroupId) {
		this.productiongroupId = productiongroupId;
	}
	public Boolean getFilterPlanningView() {
		return filterPlanningView;
	}
	public void setFilterPlanningView(Boolean filterPlanningView) {
		this.filterPlanningView = filterPlanningView;
	}
	public Integer getFilterStaffId() {
		return filterStaffId;
	}
	public void setFilterStaffId(Integer filterStaffId) {
		this.filterStaffId = filterStaffId;
	}
	public List<Integer> getFilterInMachineIds() {
		return filterInMachineIds;
	}
	public void setFilterInMachineIds(List<Integer> filterInMachineIds) {
		this.filterInMachineIds = filterInMachineIds;
	}
	
}
