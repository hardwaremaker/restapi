package com.heliumv.api.order;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Die Datenstruktur zum Anlegen einer Handeingabe-Position
 * 
 */
@XmlRootElement
public class CreateOrderManualItemPositionEntry {
	private String description;
	private String description2;
	private String unitCnr;
	
	private BigDecimal amount;
	private BigDecimal nettoPrice;
	private Integer taxDescriptionId;
	private Integer costBearingUnitId;

	/**
	 * Die Bezeichnung des Handartikels in der Position
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Die Zusatzbezeichnung des Handartikels
	 * @return
	 */
	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	/**
	 * Die Einheit in der Artikel geliefert wird
	 * @return
	 */
	public String getUnitCnr() {
		return unitCnr;
	}

	public void setUnitCnr(String unitCnr) {
		this.unitCnr = unitCnr;
	}

	/**
	 * Die zu lieferende Menge
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Der Nettoeinzelpreis einer Mengeneinheit (Beispiel: "St&uuml;ck")
	 * 
	 * @return
	 */
	public BigDecimal getNettoPrice() {
		return nettoPrice;
	}

	public void setNettoPrice(BigDecimal nettoPrice) {
		this.nettoPrice = nettoPrice;
	}

	/**
	 * Die Id des Kostentr&auml;gers</br>
	 * <p>Die Id des Kostentr&auml;gers ist dann und nur dann anzugeben, 
	 * wenn die Zusatzfunktion "KOSTENTR&Auml;GER" f&uuml;r den
	 * jeweiligen Mandanten freigeschaltet ist. </p>
	 * 
	 * @return Id des Kostentr&auml;gers
	 */
	public Integer getCostBearingUnitId() {
		return costBearingUnitId;
	}

	public void setCostBearingUnitId(Integer costBearingUnitId) {
		this.costBearingUnitId = costBearingUnitId;
	}

	/**
	 * Die Id der Mehrwertsteuersatzbezeichnung</br>
	 * <p>Die Satzbezeichnung (Beispiel: Allgemeine Waren, 
	 * Lebensmittel und B&uuml;cher, reduzierte Steuer, ...)
	 * wird im Zusammenhang mit dem Belegdatum des Auftrags
	 * verwendet, um den korrekten Steuersatz zu ermitteln</p>
	 * <p>Die SystemApi stellt die verf&uuml;gbaren Satzbezeichnungen
	 * zur Verf&uuml;gung</p>
	 * 
	 * <p>Diese Property muss gesetzt werden</p>
	 * @return
	 */
	public Integer getTaxDescriptionId() {
		return taxDescriptionId;
	}

	public void setTaxDescriptionId(Integer taxDescriptionId) {
		this.taxDescriptionId = taxDescriptionId;
	}
}
