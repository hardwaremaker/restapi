package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreatePropertyEntry implements ICreatePropertyEntry {

	private Integer layoutId;
	private String content;
	
	public CreatePropertyEntry() {
	}
	
	/**
	 * Die Id der zugeh&ouml;rigen Beschreibung @see {@link PropertyLayoutEntry} zu dem die
	 * neue Eigenschaft erzeugt werden soll
	 */
	public Integer getLayoutId() {
		return layoutId;
	}
	
	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}
	
	/**
	 * Inhalt der zu erzeugenden Eigenschaft.<br>
	 */
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
