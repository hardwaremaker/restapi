package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert eine zu erzeugende Position des Bestellvorschlags
 */
@XmlRootElement
public class CreatePurchaseOrderProposalPositionEntry {
	private Integer itemId;
	private String itemCnr;
	private BigDecimal amount;
	private Boolean noted;
	private Long deliveryDateMs;

	/**
	 * Die (optionale) Artikel-Id</br>
	 * <p>Entweder die Artikel-Id oder die Artikelnummer ({@link #getItemCnr()} muss angegeben werden</p>
	 * 
	 * @return die Id des Artikels
	 */
	public Integer getItemId() {
		return itemId;
	}
	
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Die (optionale) Artikelnummer</br>
	 * <p>Entweder die Artikelnummer oder die Artikel-Id {@link #getItemId()} muss angegeben werden</p>
	 * 
	 * @return die Artikelnummer
	 */
	public String getItemCnr() {
		return itemCnr;
	}

	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}

	/**
	 * Die zu bestellende Menge der Position
	 * 
	 * @return die zu bestellende Menge
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Soll dieser Artikel vorgemerkt werden?<br>
	 * Nicht vorgemerkte Positionen desselben Artikels k&ouml;nnen beliebig oft erzeugt werden.<br>
	 * Wird die Position vorgemerkt, darf es noch keine vorgemerkte Position eines Artikels mit der
	 * <code>itemId</code> geben. Ist eine vorhanden, wird mit Status Code <code>EXPECTATION_FAILED (417)</code>
	 * geantwortet.<br>
	 * Optionale Eigenschaft, wenn nicht angegeben, dann entsteht keine Vormerkung.
	 * 
	 * @return <code>true<code>, wenn die Position vorgemerkt werden soll.
	 */
	public Boolean isNoted() {
		return noted;
	}
	public void setNoted(Boolean noted) {
		this.noted = noted;
	}
	
	/**
	 * Der optionale Liefertermin der Position
	 * 
	 * @return Liefertermin der Position
	 */
	public Long getDeliveryDateMs() {
		return deliveryDateMs;
	}
	public void setDeliveryDateMs(Long deliveryDateMs) {
		this.deliveryDateMs = deliveryDateMs;
	}
}
