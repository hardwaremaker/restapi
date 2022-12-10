package com.heliumv.api.todo;

import com.heliumv.api.BaseEntryId;

/**
 * Beschreibt die Properties eines Todo-Eintrags
 * 
 * @author Gerold
 */
public class TodoEntry extends BaseEntryId {
	private TodoEntryType type ;
	private String cnr ;
	private String title ;
	private String partnerName ;
	private long dueDateMs ;
	private String comment ;
	private TodoDetailEntryList details;
	private String manufacturingPlace;
	private long startDateMs;
	private String orderCnr;
	private TodoDocumentEntryList documents;
	private String formattedDeliveryAddress;
	
	public TodoEntry() {
		this.details = new TodoDetailEntryList();
		this.documents = new TodoDocumentEntryList();
	}
	
	/**
	 * Typ des Todo-Eintrags. Es kann sich um ein Projekt, einen
	 * Auftrag, oder um ein Los handeln.
	 * 
	 * @return der Typ des Todo-Eintrags
	 */
	public TodoEntryType getType() {
		return type;
	}
	public void setType(TodoEntryType type) {
		this.type = type;
	}
	public String getCnr() {
		return cnr;
	}
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getDueDateMs() {
		return dueDateMs;
	}
	public void setDueDateMs(long dueDateMs) {
		this.dueDateMs = dueDateMs;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public TodoDetailEntryList getDetails() {
		return details;
	}
	public void setDetails(TodoDetailEntryList details) {
		this.details = details;
	}

	public String getManufacturingPlace() {
		return manufacturingPlace;
	}

	public void setManufacturingPlace(String manufacturingPlace) {
		this.manufacturingPlace = manufacturingPlace;
	}

	/**
	 * Die (fr&uuml;heste) Beginnzeit des Loses/Auftrags
	 * @return die (fr&uuml;heste) Beginnzeit des Loses/Auftrags/Projekts
	 */
	public long getStartDateMs() {
		return startDateMs;
	}

	public void setStartDateMs(long startDateMs) {
		this.startDateMs = startDateMs;
	}

	/**
	 * Liefert die Auftragsnummer, die das Los erzeugt hat
	 * @return null, bzw. die Auftragsnummer die das Los erzeugt hat
	 */
	public String getOrderCnr() {
		return orderCnr;
	}

	public void setOrderCnr(String orderCnr) {
		this.orderCnr = orderCnr;
	}

	/**
	 * Die Dokumente eines Todo-Eintrags
	 * @return die Dokumente des Todo-Eintrags
	 */
	public TodoDocumentEntryList getDocuments() {
		return documents;
	}

	public void setDocuments(TodoDocumentEntryList documents) {
		this.documents = documents;
	}

	public String getFormattedDeliveryAddress() {
		return formattedDeliveryAddress;
	}

	public void setFormattedDeliveryAddress(String formattedDeliveryAddress) {
		this.formattedDeliveryAddress = formattedDeliveryAddress;
	}
}
