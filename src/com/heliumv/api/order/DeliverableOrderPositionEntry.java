package com.heliumv.api.order;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.item.ItemPropertyEnum;
import com.heliumv.api.stock.StockInfoEntry;

@XmlRootElement
public class DeliverableOrderPositionEntry extends BaseEntryId {
	private String itemCnr ;
	private Integer itemId ;
	private String description ;
	private BigDecimal amount ;
	private BigDecimal openAmount ;
	private String unitCnr ;
	private String statusCnr ;
	private ItemPropertyEnum itemProperty ;
	private ItemIdentityEntryList itemIdentity ;
	private StockInfoEntry stockinfoEntry;
	
	public DeliverableOrderPositionEntry() {
		setItemIdentity(new ItemIdentityEntryList()) ;
	}
	
	public DeliverableOrderPositionEntry(Integer id) {
		super(id) ;
		setItemIdentity(new ItemIdentityEntryList()) ;
	}
	
	/**
	 * Die Artikelnummer
	 * @return die Artikelnummer
	 */
	public String getItemCnr() {
		return itemCnr;
	}
	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}
	
	/**
	 * Die Id des Artikels
	 * @return die Id des Artikel
	 */
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Die Artikelbezeichnung 
	 * @return die Artikelbezeichnung
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Auftragsmenge 
	 * 
	 * @return die Menge des Artikels im Auftrag
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Die noch offene Menge des Artikels im Auftrag
	 * @return die noch offene Menge des Artikels im Auftrag
	 */
	public BigDecimal getOpenAmount() {
		return openAmount;
	}
	public void setOpenAmount(BigDecimal openAmount) {
		this.openAmount = openAmount;
	}
	
	/**
	 * Die Einheit des Artikels im Auftrag
	 * @return die Einheit des Artikels
	 */
	public String getUnitCnr() {
		return unitCnr;
	}
	public void setUnitCnr(String unitCnr) {
		this.unitCnr = unitCnr;
	}

	/**
	 * Der Positionsstatus
	 * @return der Status der Position (Offen, erledigt, teilerledigt,...)
	 */
	public String getStatus() {
		return statusCnr;
	}
	public void setStatus(String status) {
		this.statusCnr = status;
	}
	
	public ItemPropertyEnum getItemProperty() {
		return itemProperty;
	}
	public void setItemProperty(ItemPropertyEnum itemProperty) {
		this.itemProperty = itemProperty;
	}

	public ItemIdentityEntryList getItemIdentity() {
		return itemIdentity;
	}
	public void setItemIdentity(ItemIdentityEntryList itemIdentity) {
		this.itemIdentity = itemIdentity;
	}	
	
	/**
	 * Enth&auml;lt Lager- und Lagerplatzinformationen des Artikels aus dem Auftragsabbuchungslagers.<br \> 
	 * Wird nur bef&uuml;llt, wenn beim Artikel Lagerpl&auml;tze des Auftragsabbuchungslagers
	 * hinterlegt sind und der User die Lagerberechtigung auf das Auftragsabbuchungslager hat.
	 * 
	 * @return Lager- und Lagerplatzinformationen
	 */
	public StockInfoEntry getStockinfoEntry() {
		return stockinfoEntry;
	}
	public void setStockinfoEntry(StockInfoEntry stockinfoEntry) {
		this.stockinfoEntry = stockinfoEntry;
	}
}
