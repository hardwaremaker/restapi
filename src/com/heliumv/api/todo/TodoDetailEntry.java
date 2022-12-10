package com.heliumv.api.todo;

import com.heliumv.api.BaseEntryId;

public class TodoDetailEntry extends BaseEntryId {
	private TodoDetailEntryType detailType;

	public TodoDetailEntry() {
	}
	
	public TodoDetailEntry(Integer id) {
		super(id); 
	}
	
	public TodoDetailEntryType getDetailType() {
		return detailType;
	}

	public void setDetailType(TodoDetailEntryType detailType) {
		this.detailType = detailType;
	}
}
