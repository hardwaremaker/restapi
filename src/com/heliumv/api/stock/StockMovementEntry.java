package com.heliumv.api.stock;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemIdentityEntryList;

/**
 * Die Handlagerbewegung</br>
 * <p></p>
 * @author gerold
 *
 */
@XmlRootElement
public class StockMovementEntry extends BaseEntryId {

	private ItemIdentityEntryList identities;
	private BigDecimal price;
	private BigDecimal amount;
	private String comment;
	
	/**
	 * Der Preis des Artikels f&uuml;r die die jeweilige Lagerbuchung.</br>
	 * <p>Bei einer Zu- bzw. Umbuchung sollte das der Einstandspreis sein, bei einer
	 * Abbuchung der Verkaufspreis.</p>
	 * <p>Wird der Preis hier nicht angegeben, wird der gemittelte Gestehungspreis des 
	 * Lagers verwendet</p>
	 */
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	/**
	 * Die Menge des Artikels die bewegt werden soll</br>
	 * <p>Handelt es sich um chargen- oder seriennummernbehaftete Artikel ist
	 * hier die Gesamtmenge anzugeben, die jeweiligen Teilmengen der Identit&auml;t
	 * sind unter <code>identities</code> anzugeben</p>
	 * <p>Die Menge muss angegeben werden</p>
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Der Kommentar zu dieser Lagerbewegung</br>
	 * <p>Der Kommentar muss angegeben werden</p>
	 * @return Kommentar der Lagerbewegung
	 */
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Die Liste der Identit&auml;ten und den entsprechenden Mengen und 
	 * Chargen- bzw. Seriennummern</br>
	 * 
	 * @return eine (leere) Liste der Identit&auml;ten
	 */
	public ItemIdentityEntryList getIdentities() {
		return identities;
	}
	public void setIdentities(ItemIdentityEntryList identities) {
		this.identities = identities;
	}
}
