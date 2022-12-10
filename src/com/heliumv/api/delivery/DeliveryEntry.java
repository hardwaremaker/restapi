package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.annotation.HvFlrMapper;
import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class DeliveryEntry extends BaseEntryId {
	private String deliveryCnr ;
	private DeliveryDocumentStatus status ;
	private Integer customerId;
	private String customerName;
	private String customerCity;

	public DeliveryEntry() {
	}
	
	public DeliveryEntry(Integer id) {
		super(id);
	}
	
	/**
	 * Die Lieferscheinnummer
	 * @return die Lieferscheinnummer
	 */
	public String getDeliveryCnr() {
		return deliveryCnr;
	}
	
	@HvFlrMapper(flrName = "ls.lieferscheinnummer")
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

	/**
	 * Die Id des Kunden - an den geliefert wurde.
	 * 
	 * @return die Id des Kunden
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * Der Name des Kunden - an den geliefert wurde.
	 * 
	 * @return der Name des Kunden
	 */
	public String getCustomerName() {
		return customerName;
	}
	@HvFlrMapper(flrName = "lp.kunde")
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * Der Ort im Format "<LKZ>-<PLZ> <ORT>", also zum Beispiel "AT-5301 Eugendorf", des Kunden - an den geliefert wurde.
	 *
	 * @return der Ort des Kunden
	 */
	public String getCustomerCity() {
		return customerCity;
	}
	@HvFlrMapper(flrName = "lp.ort")
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
}
