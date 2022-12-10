package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.delivery.DeliveryDocumentStatus;

@XmlRootElement
public class DeliveryOrderEntry extends BaseEntryId {
	private String deliveryCnr ;
	private DeliveryDocumentStatus status ;
	
	public DeliveryOrderEntry() {
	}
	
	public DeliveryOrderEntry(Integer id) {
		super(id) ;
	}
	
	/**
	 * Die Lieferscheinnummer
	 * @return die Lieferscheinnummer
	 */
	public String getDeliveryCnr() {
		return deliveryCnr;
	}
	
	public void setDeliveryCnr(String deliveryCnr) {
		this.deliveryCnr = deliveryCnr;
	}

	/**
	 * Der Status des Lieferscheins
	 * 
	 * @return der Lieferscheinstatus
	 */
	public DeliveryDocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryDocumentStatus status) {
		this.status = status;
	}	
}
