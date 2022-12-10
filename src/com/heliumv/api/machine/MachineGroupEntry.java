package com.heliumv.api.machine;

import com.heliumv.api.BaseEntryId;

public class MachineGroupEntry extends BaseEntryId {
	private String description ;
	private Boolean showPlanningView;
	private String productionGroupDescription;
	
	public MachineGroupEntry() {
	}

	public MachineGroupEntry(Object flrId) {
		super((Integer) flrId) ;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getShowPlanningView() {
		return showPlanningView;
	}

	public void setShowPlanningView(Boolean showPlanningView) {
		this.showPlanningView = showPlanningView;
	}

	public String getProductionGroupDescription() {
		return productionGroupDescription;
	}

	public void setProductionGroupDescription(String productionGroupDescription) {
		this.productionGroupDescription = productionGroupDescription;
	}

}
