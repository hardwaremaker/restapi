package com.heliumv.api.staff;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SynchEntry {
	private String userId;
	private String where;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Die Quelle der Synchronisation</br>
	 * <p>Dies kann ein beliebiger Text sein, der aus Anwendersicht die Zuordnung zur 
	 * Synchronisation erm&ouml;glicht.</p>
	 * @return die Quelle der Synchronisation
	 */
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
}
