package com.heliumv.api.stock;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockPlacePostEntry {

	private String stockplaceName;
	private Integer stockplaceId;
	private Integer itemId;

	/**
	 * Id des Lagerplatzes, optional wenn Name des Lagerplatzes &uuml;bergeben wird.
	 * Werden Id und Name &uuml;bergeben, wird die Id vorrangig behandelt.
	 * @return Id des Lagerplatzes
	 */
	public Integer getStockplaceId() {
		return stockplaceId;
	}
	
	public void setStockplaceId(Integer stockplaceId) {
		this.stockplaceId = stockplaceId;
	}
	
	/**
	 * Name des Lagerplatzes, optional wenn Id des Lagerplatzes &uuml;bergeben wird.
	 * Werden Id und Name &uuml;bergeben, wird die Id vorrangig behandelt.
	 * 
	 * @return Name des Lagerplatzes
	 */
	public String getStockplaceName() {
		return stockplaceName;
	}
	
	public void setStockplaceName(String stockplaceName) {
		this.stockplaceName = stockplaceName;
	}
	
	/**
	 * Id des Artikels, dem der Lagerplatz zugewiesen werden soll
	 * 
	 * @return Id des Artikels
	 */
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
}
