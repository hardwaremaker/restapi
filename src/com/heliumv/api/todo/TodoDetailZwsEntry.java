package com.heliumv.api.todo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoDetailZwsEntry extends TodoDetailEntry {
	private String text;

	public TodoDetailZwsEntry() {
		setDetailType(TodoDetailEntryType.ZWS);
	}
	
	public TodoDetailZwsEntry(Integer id) {
		super(id);
		setDetailType(TodoDetailEntryType.ZWS);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public TodoDetailTextEntry asTextEntry() {
		TodoDetailTextEntry textEntry = new TodoDetailTextEntry(getId());
		textEntry.setText(getText());
		return textEntry;
	}
}
