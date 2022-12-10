package com.heliumv.api.staff;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class WorkCalendarEntry extends BaseEntryId {
	private long date ;
	private Integer dayTypeId ;
	private String dayTypeDescription ;
	private String description ;
	
	public WorkCalendarEntry() {
	}
	
	public WorkCalendarEntry(Integer id) {
		super(id) ;
	}
	
	/**
	 * Das Datum (ms seit 1.1.1970) 
	 * 
	 * @return das Datum in ms (seit 1.1.1970)
	 */
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * Die Id der Tagesart (Montag, Dienstag, ..., Feiertag, Betriebsurlaub)
	 * @return die Id der Tagesart
	 */
	public Integer getDayTypeId() {
		return dayTypeId;
	}

	public void setDayTypeId(Integer dayTypeId) {
		this.dayTypeId = dayTypeId;
	}

	/**
	 * Die Bezeichnung der Tagesart
	 * 
	 * @return die Bezeichnung der Tagesart in der Sprache des angemeldeten Benutzers
	 */
	public String getDayTypeDescription() {
		return dayTypeDescription;
	}

	public void setDayTypeDescription(String dayTypeDescription) {
		this.dayTypeDescription = dayTypeDescription;
	}

	/**
	 * Die Bezeichnung/Name des Feiertags 
	 * 
	 * @return der Name des Feiertags
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
