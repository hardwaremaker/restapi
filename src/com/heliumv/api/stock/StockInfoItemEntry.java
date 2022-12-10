package com.heliumv.api.stock;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
/**
 * Die Artikel-Id samt der Menge
 * 
 * Die Menge auf dem Lager bezieht sich auf jenes 
 * Lager, in dem sich dieser Eintrag befindet.
 * 
 * @author gerold
 */
public class StockInfoItemEntry extends BaseEntryId {
	private BigDecimal amount;
	private BigDecimal minimum;
	private BigDecimal nominal;
	
	/**
	 * Die auf dem Lager befindliche Menge 
	 * @return die Menge auf dem Lager
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Der Lagermindestmenge
	 * 
	 * @return null oder die Mindestmenge
	 */
	public BigDecimal getMinimum() {
		return minimum;
	}

	public void setMinimum(BigDecimal minimum) {
		this.minimum = minimum;
	}

	/**
	 * Die Lagersollmenge
	 * @return null oder die Sollmenge
	 */
	public BigDecimal getNominal() {
		return nominal;
	}

	public void setNominal(BigDecimal nominal) {
		this.nominal = nominal;
	}
}
