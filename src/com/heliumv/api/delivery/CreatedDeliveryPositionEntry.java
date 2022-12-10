package com.heliumv.api.delivery;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemIdentityEntryList;

/**
 * Repr&auml;sentiert die Information &uuml;ber die gelieferte Position
 */
@XmlRootElement
public class CreatedDeliveryPositionEntry {
	private Integer deliveryId ;
	private String deliveryCnr ;
	private Integer deliveryPositionId ;
	private Integer orderId ;
	private Integer orderPositionId ;
	private ItemIdentityEntryList itemIdentity ;	
	private BigDecimal amount ;
	private BigDecimal openAmount ;
	private BigDecimal deliveredAmount ;
	
	public CreatedDeliveryPositionEntry() {
		setItemIdentity(new ItemIdentityEntryList());
	}
	
	/**
	 * Die Id des Lieferscheins
	 * 
	 * @return die Id des Lieferscheins
	 */
	public Integer getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	/**
	 * Die Lieferscheinnummer</br>
	 * <p>Wurde beim Erzeugen der Lieferscheinposition kein Lieferschein angegeben, so
	 * ist hier die neu erstellte Lieferscheinnummer enthalten. Ansonsten diejenige, 
	 * auf die geliefert werden sollte.  
	 * @return die (bei Bedarf) neu erstellte Lieferscheinnummer
	 */
	public String getDeliveryCnr() {
		return deliveryCnr;
	}
	public void setDeliveryCnr(String deliveryCnr) {
		this.deliveryCnr = deliveryCnr;
	}
	
	/**
	 * Die PositionId der Lieferscheinposition
	 * @return die (bei Bedarf) neu erstellte Lieferscheinposition-Id
	 */
	public Integer getDeliveryPositionId() {
		return deliveryPositionId;
	}
	public void setDeliveryPositionId(Integer deliveryPositionId) {
		this.deliveryPositionId = deliveryPositionId;
	}
	
	/**
	 * Die Id des Auftrags auf den sich die Lieferscheinposition, bzw. die Auftragposition bezieht.
	 * @return die Id des Auftrags
	 */
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * Die Auftragsposition die (teil)geliefert werden soll
	 * @return die Id der Auftragsposition
	 */
	public Integer getOrderPositionId() {
		return orderPositionId;
	}
	public void setOrderPositionId(Integer orderPositionId) {
		this.orderPositionId = orderPositionId;
	}
	
	/**
	 * Die insgesamt zu liefernde Menge
	 * @return die zu liefernde Menge
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Die Serien- bzw. Chargennummern die in der Lieferung dieser Position enthalten sind
	 * @return die (leere) Liste der gerade gelieferten Serien- und Chargennummern
	 */
	public ItemIdentityEntryList getItemIdentity() {
		return itemIdentity;
	}
	public void setItemIdentity(ItemIdentityEntryList itemIdentity) {
		this.itemIdentity = itemIdentity;
	}

	/**
	 * Die noch offene Menge im Auftrag
	 * @return die noch offene Menge im Auftrag
	 */
	public BigDecimal getOpenAmount() {
		return openAmount;
	}
	public void setOpenAmount(BigDecimal openAmount) {
		this.openAmount = openAmount;
	}

	/**
	 * Die f&uuml;r den Auftrag gelieferte Menge in diesem Lieferschein
	 * @return die bereits gelieferte Menge
	 */
	public BigDecimal getDeliveredAmount() {
		return deliveredAmount;
	}

	public void setDeliveredAmount(BigDecimal deliveredAmount) {
		this.deliveredAmount = deliveredAmount;
	}
}
