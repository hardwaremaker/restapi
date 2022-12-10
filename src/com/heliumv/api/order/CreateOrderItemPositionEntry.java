package com.heliumv.api.order;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateOrderItemPositionEntry {
    private Integer itemId;
    private String itemCnr;
    private BigDecimal amount;
    private Integer costBearingUnitId;
    
    public CreateOrderItemPositionEntry() {
    }

    /**
     * Die (optionale) Artikel-Id</br>
     * <p>Entweder {@link #getItemId()} oder {@link #getItemCnr()} muss 
     * angegeben werden</p>
     */
    public Integer getItemId() {
    	return itemId;
    }

    public void setItemId(Integer itemId) {
    	this.itemId = itemId;
    }

    /**
     * Die zu lieferende Menge</br>
     * <p>Bei identit&auml;tsbehafteten Artikeln (also jene mit Serien- oder 
     * Chargennummer) ist die Gesamtmenge aus den einzelnen Serien- oder Chargennummern
     * trotzdem anzugeben</p>
     * @return die zu liefernde Menge
     */
    public BigDecimal getAmount() {
    	return amount;
    }

    public void setAmount(BigDecimal amount) {
    	this.amount = amount;
    }

	public String getItemCnr() {
		return itemCnr;
	}

	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
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
}
