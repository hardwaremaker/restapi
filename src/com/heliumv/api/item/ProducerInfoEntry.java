package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * Herstellerbezogene Informationen
 * 
 * @author Gerold
 */
public class ProducerInfoEntry {
	private Integer producerId ;
	private String itemCnr ;
	private String itemDescription ;
	private String barcodeLeadIn ;
	
	public ProducerInfoEntry() {
		setBarcodeLeadIn(""); 
	}
	
	/**
	 * Die Hersteller-Id
	 * 
	 * @return null oder die Hersteller-Id - sofern es einen Hersteller gibt
	 */
	public Integer getProducerId() {
		return producerId ;
	}
	
	public void setProducerId(Integer producerId) {
		this.producerId = producerId ;
	}

	/**
	 * Die Hersteller-Artikelnummer
	 * @return die Artikelnummer des Herstellers
	 */
	public String getItemCnr() {
		return itemCnr;
	}
	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}
	
	/**
	 * Die Bezeichnung des Artikels auf Herstellerseite
	 * @return die Bezeichnung des Artikels
	 */
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	/**
	 * Der Barcode-Leadin des Herstellers
	 * 
	 * @return der Barcode-Leadin des Herstellers. Kann in Verbindung mit {@link #getItemCnr()}
	 * den gesamten Barcode ergeben
	 */
	public String getBarcodeLeadIn() {
		return barcodeLeadIn;
	}

	public void setBarcodeLeadIn(String barcodeLeadIn) {
		this.barcodeLeadIn = barcodeLeadIn;
	}
	
	
	/**
	 * Gibt es eine Hersteller-Info?
	 * @return true wenn es Hersteller-Informationen gibt, ansonsten false
	 */
	public Boolean getHasInfo() {
		return producerId != null ;
	}

	public void setHasSerialnr(Boolean serialnr) {
	}	
}
