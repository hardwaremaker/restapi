package com.heliumv.api.order;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert die Information &uuml;ber eine erzeugte Auftragspositon
 */
@XmlRootElement
public class CreatedOrderPositionEntry {
    private Integer orderId;
    private String orderCnr;
    private Integer orderPositionId;
    private BigDecimal amount;

    // Nur wegen CXF Kompatibilitaet
    public CreatedOrderPositionEntry() {
    }
    
    public CreatedOrderPositionEntry(final Integer orderId, final String orderCnr) {
    	this.orderId = orderId;
		this.orderCnr = orderCnr;
    }
	
    /**
     * Die Id des Auftrags
     * @return
     */
    public Integer getOrderId() {
    	return orderId;
    }

    /**
     * Die Cnr des Auftrags
     * @return
     */
	public String getOrderCnr() {
		return orderCnr;
	}
	
	/**
	 * Die Id der Auftragsposition
	 * @return
	 */
	public Integer getOrderPositionId() {
		return orderPositionId;
	}

	public void setOrderPositionId(Integer orderPositionId) {
		this.orderPositionId = orderPositionId;
	}

	/**
	 * Die Menge der Auftragsposition
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
