package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert eine zu liefernde Position
 */
@XmlRootElement
public class CreateItemCustomerDeliveryPositionEntry extends CreateItemDeliveryPositionEntry {
	private Integer customerNr ;
	private Integer customerId;
	
	public CreateItemCustomerDeliveryPositionEntry() { 
	}

	/**
	 * Die (optionale) Lieferschein-Id, auf die diese Position geliefert werden soll.</br>
	 * <p>Ab der zweiten Position m&uuml;ssen beide Properties angegeben werden. Wird nur
	 * die customerNr und nicht die deliveryId angegeben, wird ein neuer Lieferschein
	 * f&uuml;r den Kunden der durch die customerNr repr&auml;sentiert wird angelegt. 
	 * F&uuml;r diesen Kunden wird der Lieferschein erzeugt. In der Folge kann dann 
	 * die hier erzeugte Lieferschein-id angegeben werden.</p>
	 * <p>Wird eine deliveryId angegeben, wird gepr&uuml;ft, dass die customerId zugeh&ouml;rig 
	 * ist</p>
	 * @return die (optionale) Id des Lieferscheins
	 */
	public Integer getDeliveryId() {
		return super.getDeliveryId() ;
	}
	public void setDeliveryId(Integer deliveryId) {
		super.setDeliveryId(deliveryId); 
	}

	/**
	 * Die Kundennummer</br>
	 * <p>Sie ist nicht mit der customerId identisch, sondern wird
	 * fortlaufend vom ERP vergeben, bzw. kann manuell vergeben werden. In beiden
	 * F&auml;llen muss der Parameter "KUNDE_MIT_NUMMER" im ERP gesetzt werden. </p>
	 * @return die Kundennummer
	 */
	public Integer getCustomerNr() {
		return customerNr ;
	}

	public void setCustomerNr(Integer customerNr) {
		this.customerNr = customerNr;
	}

	/**
	 * Die (optionale) Id des Kunden</br>
	 * <p>Wird die Id des Kunden angegeben, hat sie Vorrang vor einer 
	 * ebenfalls angegebenen Kundennummer</p>
	 * @return die Id des Kunden
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}
