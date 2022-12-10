package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
/**
 * Daten eines lieferbaren Auftrags
 */
public class DeliverableOrderEntry extends BaseEntryId {
	private String cnr ;
	private boolean deliverable ;
//	private String statusCnr ;
	
	private OrderDocumentStatus status ;
	private DeliveryOrderEntryList deliveryEntries ;
	private DeliverableOrderPositionEntryList positionEntries ;
	
	public DeliverableOrderEntry() {
		super() ;
		setDeliveryEntries(new DeliveryOrderEntryList());
		setPositionEntries(new DeliverableOrderPositionEntryList());
		setStatus(OrderDocumentStatus.NOTINITIALIZED);
	}
	
	public DeliverableOrderEntry(Integer id) {
		super(id) ;
		setDeliveryEntries(new DeliveryOrderEntryList());
		setPositionEntries(new DeliverableOrderPositionEntryList());
		setStatus(OrderDocumentStatus.NOTINITIALIZED);
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
	 * Ist das ein Auftrag der noch geliefert werden darf/kann</br>
	 * <p>Das ist dann der Fall, wenn er entweder OFFEN oder TEILGELIEFERT ist.</p>
	 * @return true wenn dieser Auftrag noch geliefert werden kann/darf
	 */
	public boolean isDeliverable() {
		return deliverable;
	}

	public void setDeliverable(boolean deliverable) {
		this.deliverable = deliverable;
	}

	/**
	 * Auftragstatus
	 * @return der AuftragStatus 
	 */
	public OrderDocumentStatus getStatus() {
		return status;
	}

	public void setStatus(OrderDocumentStatus status) {
		this.status = status;
		setDeliverable(
				getStatus() == OrderDocumentStatus.OPEN || 
				getStatus() == OrderDocumentStatus.PARTIALLYDONE);
	}

	/**
	 * Eine (leere) Liste aller Lieferscheine die f&uuml;r diesen Auftrag ausgestellt wurden 
	 * und noch bearbeitbar (lieferbar) sind.
	 * 
	 * @return eine (leere) Liste aller noch lieferbaren Lieferscheine
	 */
	public DeliveryOrderEntryList getDeliveryEntries() {
		return deliveryEntries;
	}

	public void setDeliveryEntries(DeliveryOrderEntryList deliveryEntries) {
		this.deliveryEntries = deliveryEntries;
	}

	/**
	 * Eine (leere) Liste aller Positionen die noch offene Mengen beinhalten
	 * 
	 * @return eine (leere) Liste aller Positionen mit offener Menge
	 */
	public DeliverableOrderPositionEntryList getPositionEntries() {
		return positionEntries;
	}

	public void setPositionEntries(DeliverableOrderPositionEntryList positionEntries) {
		this.positionEntries = positionEntries;
	}
}
