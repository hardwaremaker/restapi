/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.api.item;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.stock.StockPlaceEntryList;

@XmlRootElement
public class StockAmountEntry {
	private BigDecimal amount ;
	private StockEntry stock ;
	private ItemEntry  item ;
	private ItemIdentityEntryList itemIdentityList ;
	private StockPlaceEntryList stockplaceList;
	
	public StockAmountEntry() {
		// es gibt bewusst keine Initialisierung der itemIdentyList um 
		// Kompatibilitaet zu vorherigen Versionen zu gewaehrleisten
	}
	
	public StockAmountEntry(ItemEntry item, StockEntry stock, BigDecimal amount) {
		this.item = item ;
		this.stock = stock ;
		this.amount = amount ;
	}
	
	/**
	 * Die gesamte Menge des Artikels auf diesem Lager
	 * @return die Menge des Artikels gesamt
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Die Lagerinformation
	 * @return die Info &uuml;ber das Lager
	 */
	public StockEntry getStock() {
		return stock;
	}
	public void setStock(StockEntry stock) {
		this.stock = stock;
	}

	/**
	 * Die Info &uuml;ber den Artikel
	 * @return
	 */
	public ItemEntry getItem() {
		return item;
	}

	public void setItem(ItemEntry item) {
		this.item = item;
	}

	/**
	 * Die Liste aller Chargen- bzw. Seriennummern die sich f&uuml;r
	 * diesen Artikel auf dem Lager befindet
	 * @return null wenn es ein Artikel ohne Identit&auml;tsinformatino ist,
	 * bzw die Liste aller Chargen- bzw. Seriennummern
	 */
	public ItemIdentityEntryList getItemIdentityList() {
		return itemIdentityList;
	}

	public void setItemIdentityList(ItemIdentityEntryList itemIdentityList) {
		this.itemIdentityList = itemIdentityList;
	}
	
	/** 
	 * Die Liste aller Lagerpl&auml;tze, die diesem Artikel zugewiesen sind.
	 * 
	 * @return Liste aller Lagerpl&auml;tze
	 */
	public StockPlaceEntryList getStockplaceList() {
		return stockplaceList;
	}
	
	public void setStockplaceList(StockPlaceEntryList stockplaceList) {
		this.stockplaceList = stockplaceList;
	}
}
