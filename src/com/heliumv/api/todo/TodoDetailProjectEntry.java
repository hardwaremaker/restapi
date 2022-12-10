package com.heliumv.api.todo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoDetailProjectEntry extends TodoDetailEntry {
	private long timems;
	private String text;
	private boolean html;
	private String subject;

	public TodoDetailProjectEntry() {
		this.setDetailType(TodoDetailEntryType.PROJECTDETAIL);
	}
	
	public TodoDetailProjectEntry(Integer id) {
		super(id);
		this.setDetailType(TodoDetailEntryType.PROJECTDETAIL);
	}
	
	public long getTimeMs() {
		return timems;
	}
	public void setTimeMs(long timems) {
		this.timems = timems;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
