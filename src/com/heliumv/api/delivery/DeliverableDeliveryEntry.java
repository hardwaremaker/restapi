package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
/**
 * Daten eines lieferbaren Auftrags
 */
public class DeliverableDeliveryEntry extends BaseEntryId {
	private String cnr ;
	private boolean deliverable ;
	
	private DeliveryDocumentStatus status ;
	
	public DeliverableDeliveryEntry() {
		super() ;
		setStatus(DeliveryDocumentStatus.NOTINITIALIZED);
	}
	
	public DeliverableDeliveryEntry(Integer id) {
		super(id) ;
		setStatus(DeliveryDocumentStatus.NOTINITIALIZED);
	}
	
	/**
	 * Auftragsnummer
	 * @return die Auftragsnummer
	 */
	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	/**
	 * Ist der Lieferschein der noch geliefert werden darf/kann</br>
	 * <p>Das ist dann der Fall, wenn er ANGELEGT ist.</p>
	 * @return true wenn dieser Lieferschein noch geliefert werden kann/darf
	 */
	public boolean isDeliverable() {
		return deliverable;
	}

	public void setDeliverable(boolean deliverable) {
		this.deliverable = deliverable;
	}

	/**
	 * Lieferscheinstatus
	 * @return der LieferscheinStatus 
	 */
	public DeliveryDocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryDocumentStatus status) {
		this.status = status;
		setDeliverable(
				getStatus() == DeliveryDocumentStatus.NEW);
	}
}
