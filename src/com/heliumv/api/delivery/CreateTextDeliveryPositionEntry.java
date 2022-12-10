package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert eine Textposition im Lieferschein
 */
@XmlRootElement
public class CreateTextDeliveryPositionEntry {
	private Integer deliveryId;
	private String text;
	
	public CreateTextDeliveryPositionEntry() {
	}
	
	/**
	 * Die Id des Lieferscheins
	 * @return die Id des Lieferscheins
	 */
	public Integer getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	/**
	 * Der Text der Position. Auf HELIUM V Seite handelt es sich um eine
	 * sogenannte "Texteingabe". D.h. es stehen bis zu 3000 Zeichen an 
	 * (optional) styled Text zur Verf&uuml;gung.
	 * 
	 * Der Text wird auf 3000 Zeichen abgeschnitten, sofern mehr Zeichen 
	 * angegeben werden.
	 *  
	 * @return der Text dieser Position
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
