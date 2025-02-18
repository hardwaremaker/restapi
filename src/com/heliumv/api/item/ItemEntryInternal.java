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
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.annotation.HvFlrMapper;
import com.heliumv.api.BaseEntryId;
import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.stock.StockInfoEntryList;
import com.heliumv.tools.StringHelper;

@XmlRootElement
/**
 * Beschreibt die Properties eines Artikels
 * 
 * @author Gerold
 */
public class ItemEntryInternal extends BaseEntryId {
	private String cnr ;
	private String description ;
	private String description2 ;
	private String name ;
	private String shortName ;
	private BigDecimal stockAmount ;
	private BigDecimal costs ;
	private String billOfMaterialType ;
	private Boolean available ;
	private List<String> comments ;
	private Boolean hidden ;
	private String unitCnr ;
	private String typeCnr ;
	private String itemgroupCnr ;
	private String itemclassCnr ;
	private String revision ;
	private String referenceNumber ;
	private String index ;
	private Boolean serialnr ;
	private Boolean chargenr ;
	
	private StockAmountInfoEntry stockAmountInfo ;
	private ProducerInfoEntry producerInfo ;
	private PackagingInfoEntryList packagingEntries ;
	private StockInfoEntryList stockplaceInfoEntries;
	private DocumentInfoEntryList documentInfoEntries;
	private ItemCommentMediaInfoEntryList itemCommentMediaInfoEntries;
	
	/**
	 * Die Kennung des Artikels (Artikelnummer)
	 * @return
	 */
	public String getCnr() {
		return cnr;
	}
	@HvFlrMapper(flrName="artikel.artikelnummerlang")
	public void setCnr(String cnr) {
		this.cnr = StringHelper.trim(cnr) ;
	}
	
	/**
	 * Die Zusatzbezeichnung des Artikels
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	@HvFlrMapper(flrName="bes.artikelbezeichnung") 
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Die Zusatzbezeichnung2 des Artikels
	 * @return
	 */
	public String getDescription2() {
		return description2;
	}
	@HvFlrMapper(flrName = "artikel.zusatzbez")
	public void setDescription2(String description2) {
		this.description2 = description2;
	}
	
	/**
	 * Der Name des Artikels
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Die Kurzbezeichnung des Artikels
	 * @return
	 */
	public String getShortName() {
		return shortName;
	}
	@HvFlrMapper(flrName = "lp.kurzbezeichnung")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/**
	 * Der "verf&uuml;gbare" Lagerstand
	 * @return
	 */
	public BigDecimal getStockAmount() {
		return stockAmount;
	}
	@HvFlrMapper(flrName = "lp.lagerstand")
	public void setStockAmount(BigDecimal stockAmount) {
		this.stockAmount = stockAmount;
	}
	
	/**
	 * Der Gestehungspreis
	 * @return
	 */
	public BigDecimal getCosts() {
		return costs;
	}
	@HvFlrMapper(flrName = "lp.preis")
	public void setCosts(BigDecimal costs) {
		this.costs = costs;
	}
	
	/**
	 * Die St&uuml;cklistenart
	 * @return
	 */
	public String getBillOfMaterialType() {
		return billOfMaterialType;
	}
	@HvFlrMapper(flrName = "lp.stuecklistenart")
	public void setBillOfMaterialType(String billOfMaterialType) {
		this.billOfMaterialType = StringHelper.trim(billOfMaterialType) ;
	}

	/**
	 * Ist der Artikel verfügbar bzw. gesperrt?
	 * @return
	 */
	public Boolean isAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
	@HvFlrMapper(flrName="Icon")
	public void setAvailable(Object[] iconInfo) {
		available = iconInfo == null ;
	}

	@HvFlrMapper(flrName="Icon")
	public void setAvailable(String value) {
		available = value == null ;
	}
	
	/**
	 * Die Liste aller Artikelkommentare im Format "text/html"
	 * @return null, oder eine Liste der für den Artikel vorhandenen Artikelkommentar im Format "text/html"
	 */
	public List<String> getComments() {
		return comments;
	}
	
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	
	/**
	 * Ist es ein versteckter Artikel?
	 * 
	 * @return true wenn es ein versteckter Artikel ist
	 */
	public Boolean getHidden() {
		return hidden ;
	}
	
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
	/**
	 * Die Einheit des Artikels
	 * @return
	 */
	public String getUnitCnr() {
		return unitCnr;
	}
	
	public void setUnitCnr(String unitCnr) {
		this.unitCnr = StringHelper.trim(unitCnr) ;
	}
	
	/**
	 * Die Artikelart
	 * @return
	 */
	public String getTypeCnr() {
		return typeCnr;
	}
	public void setTypeCnr(String typeCnr) {
		this.typeCnr = StringHelper.trim(typeCnr);
	}
	
	/**
	 * Die Artikelgruppe
	 * @return
	 */
	public String getItemgroupCnr() {
		return itemgroupCnr;
	}
	@HvFlrMapper(flrNames= {"lp.artikelgruppeInAbmessung", "lp.artikelgruppe" })
	public void setItemgroupCnr(String itemgroupCnr) {
		this.itemgroupCnr = StringHelper.trim(itemgroupCnr);
	}
	
	/**
	 * Die Artikelklasse
	 */
	public String getItemclassCnr() {
		return itemclassCnr;
	}
	@HvFlrMapper(flrName="lp.artikelklasse")
	public void setItemclassCnr(String itemclassCnr) {
		this.itemclassCnr = StringHelper.trim(itemclassCnr) ;
	}
	
	/**
	 * Die Revisions"nummer"
	 * @return
	 */
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}

	/**
	 * Lagerstandsinformationen
	 * 
	 * @return
	 */
	public StockAmountInfoEntry getStockAmountInfo() {
		return stockAmountInfo;
	}
	public void setStockAmountInfo(StockAmountInfoEntry stockAmountInfo) {
		this.stockAmountInfo = stockAmountInfo;
	}
	
	/**
	 * Die Referenznummer
	 * @return
	 */
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	/**
	 * Die Indexnummer
	 * @return
	 */
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	
	/**
	 * Handelt es sich um einen seriennummerntragenden Artikel?
	 * 
	 * @return true wenn es ein seriennummerntragender Artikel ist
	 */
	public Boolean getHasSerialnr() {
		return serialnr;
	}
	public void setHasSerialnr(Boolean serialnr) {
		this.serialnr = serialnr;
	}
	
	/**
	 * Handelt es sich um einen chargennummerntragenden Artikel?
	 * 
	 * @return true wenn es sich um einen chargennummerntragenden Artikel handelt
	 */
	public Boolean getHasChargenr() {
		return chargenr;
	}
	public void setHasChargenr(Boolean chargenr) {
		this.chargenr = chargenr;
	}
	
	/**
	 * Herstellerspezifische Informationen
	 * 
	 * @return die Infos des Herstellers (Artikelnummer, Bezeichnung, ...)
	 */
	public ProducerInfoEntry getProducerInfo() {
		return producerInfo;
	}
	public void setProducerInfo(ProducerInfoEntry producerInfo) {
		this.producerInfo = producerInfo;
	}

	/**
	 * Die Liste der Verpackungsinformation
	 * <p>null wird aus Kompatibilit&auml;tsgr&uuml;nden zur&uuml;ckgeliefert</p>
	 * 
	 * @return null(!) oder die Liste der Verpackungsinfos
	 */
	public PackagingInfoEntryList getPackagingEntries() {
		return packagingEntries;
	}
	public void setPackagingEntries(PackagingInfoEntryList packagingEntries) {
		this.packagingEntries = packagingEntries;
	}	

	/**
	 * Enth&auml;lt alle Lagerpl&auml;tze, die dem Artikel zugewiesen sind<br \> 
	 * Es werden nur jene Lager ber&uuml;cksichtigt auf die der User die Lagerberechtigung hat.
	 * 
	 * @return 
	 */
	public StockInfoEntryList getStockplaceInfoEntries() {
		return stockplaceInfoEntries;
	}
	public void setStockplaceInfoEntries(StockInfoEntryList stockplaceInfoEntries) {
		this.stockplaceInfoEntries = stockplaceInfoEntries;
	}
	
	/**
	 * Liste mit Info &uuml;ber die (f&uuml;r den Benutzer sichtbaren) Dokuments des Artikels
	 * 
	 * @return
	 */
	public DocumentInfoEntryList getDocumentInfoEntries() {
		return documentInfoEntries;
	}
	public void setDocumentInfoEntries(DocumentInfoEntryList documentInfoEntries) {
		this.documentInfoEntries = documentInfoEntries;
	}
	
	/**
	 * Liste mit Info &uuml;ber die Artikelkommentare liefern
	 * 
	 * @return
	 */
	public ItemCommentMediaInfoEntryList getItemCommentMediaInfoEntries() {
		return itemCommentMediaInfoEntries;
	}
	public void setItemCommentMediaInfoEntries(ItemCommentMediaInfoEntryList itemCommentMediaInfoEntries) {
		this.itemCommentMediaInfoEntries = itemCommentMediaInfoEntries;
	}
}
