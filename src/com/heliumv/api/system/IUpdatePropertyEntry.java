package com.heliumv.api.system;

public interface IUpdatePropertyEntry {

	/**
	 * Id der Eigenschaft
	 * @return
	 */
	Integer getPropertyId();
	void setPropertyId(Integer propertyId);
	
	/**
	 * Inhalt der Eigenschaft
	 * @return
	 */
	String getContent();
	void setContent(String content);
	
}
