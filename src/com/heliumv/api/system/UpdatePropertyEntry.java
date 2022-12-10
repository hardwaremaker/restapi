package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpdatePropertyEntry implements IUpdatePropertyEntry {

	private Integer propertyId;
	private String content;
	
	public UpdatePropertyEntry() {
	}

	/**
	 * Id der Eigenschaft
	 * 
	 * @return Id
	 */
	public Integer getPropertyId() {
		return propertyId;
	}

	@Override
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Zu aktualisierender Inhalt der Eigenschaft. Kann leer oder auch "null" sein.
	 * 
	 * @return neuer Inhalt der Eigenschaft
	 */
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

}
