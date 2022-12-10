package com.heliumv.api.document;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;

@XmlRootElement
public class DocumentInfoEntry extends BaseEntryCnr {

	private String name;
	private String filename;
	private Long size;
	
	public DocumentInfoEntry() {
	}

	/**
	 * Name des Dokuments in der Dokumentenablage
	 * 
	 * @return Name des Dokuments
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Dateiname des Dokuments
	 * 
	 * @return Dateiname
	 */
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Gr&ouml;&szlig;e des Dokuments in Bytes
	 * 
	 * @return Gr&ouml;&szlig;e
	 */
	public Long getSize() {
		return size;
	}
	
	public void setSize(Long size) {
		this.size = size;
	}
}
