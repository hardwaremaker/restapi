package com.heliumv.api.system;

public interface ICreatePropertyEntry {

	/**
	 * Id des Layouts der Eigenschaft
	 * @return
	 */
	Integer getLayoutId();
	void setLayoutId(Integer layoutId);
	
	/**
	 * Inhalt der Eigenschaft
	 * @return
	 */
	String getContent();
	void setContent(String content);
}
