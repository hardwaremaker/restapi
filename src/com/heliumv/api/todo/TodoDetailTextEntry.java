package com.heliumv.api.todo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoDetailTextEntry extends TodoDetailEntry {
	private String text;
	private boolean html;
	
	public TodoDetailTextEntry() {
		this.setDetailType(TodoDetailEntryType.TEXT);
		this.setHtml(false);
	}
	
	public TodoDetailTextEntry(Integer id) {
		super(id);
		this.setDetailType(TodoDetailEntryType.TEXT);
		this.setHtml(false);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}		
}
