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
package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.customer.CustomerEntry;
import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.item.ItemCommentMediaInfoEntryList;
import com.heliumv.api.item.ItemV1Entry;
import com.heliumv.api.partlist.PartlistWorkstepEntryList;

@XmlRootElement
public class ProductionEntry extends BaseEntryId {
	private String cnr ;
	private BigDecimal amount ;
	private String orderOrItemCnr ;
	private Integer partlistId;
	private Long startDateMs;
	private Long endDateMs;
	private String project;
	private String comment;
	private ProductionStatus status;
	private ItemV1Entry itemEntry;
	private CustomerEntry customer;
	private ProductionAdditionalStatusEntryList additionalStatuses;
	private PartlistWorkstepEntryList worksteps;
	private ProductionTargetMaterialEntryList targetMaterials;
	private BigDecimal deliveredAmount;
	private ProductionWorkstepEntryList productionWorksteps;
	private Integer targetStockId;
	private String customerName;
	private String itemDescription;
	private String itemDescription2;
	private DocumentInfoEntryList documentInfoEntries;
	private ItemCommentMediaInfoEntryList itemCommentMediaInfoEntries;
	private String itemCnr;
	private String orderCnr;
	private String manufactoringPlace;
	private TestPlanEntryList testPlanEntries;
		
	public String getCnr() {
		return cnr;
	}
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getOrderOrItemCnr() {
		return orderOrItemCnr;
	}
	public void setOrderOrItemCnr(String orderOrItemCnr) {
		this.orderOrItemCnr = orderOrItemCnr;
	}
	public Long getStartDateMs() {
		return startDateMs;
	}
	public void setStartDateMs(Long startDateMs) {
		this.startDateMs = startDateMs;
	}
	public Long getEndDateMs() {
		return endDateMs;
	}
	public void setEndDateMs(Long endDateMs) {
		this.endDateMs = endDateMs;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ItemV1Entry getItemEntry() {
		return itemEntry;
	}
	public void setItemEntry(ItemV1Entry itemEntry) {
		this.itemEntry = itemEntry;
	}
	public CustomerEntry getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerEntry customer) {
		this.customer = customer;
	}
	public ProductionAdditionalStatusEntryList getAdditionalStatuses() {
		return additionalStatuses;
	}
	public void setAdditionalStatuses(ProductionAdditionalStatusEntryList additionalStatuses) {
		this.additionalStatuses = additionalStatuses;
	}
	public ProductionStatus getStatus() {
		return status;
	}
	public void setStatus(ProductionStatus status) {
		this.status = status;
	}
	public Integer getPartlistId() {
		return partlistId;
	}
	public void setPartlistId(Integer partlistId) {
		this.partlistId = partlistId;
	}
	public PartlistWorkstepEntryList getWorksteps() {
		return worksteps;
	}
	public void setWorksteps(PartlistWorkstepEntryList worksteps) {
		this.worksteps = worksteps;
	}
	public ProductionTargetMaterialEntryList getTargetMaterials() {
		return targetMaterials;
	}
	public void setTargetMaterials(ProductionTargetMaterialEntryList targetMaterials) {
		this.targetMaterials = targetMaterials;
	}
	public BigDecimal getDeliveredAmount() {
		return deliveredAmount;
	}
	public void setDeliveredAmount(BigDecimal deliveredAmount) {
		this.deliveredAmount = deliveredAmount;
	}
	public ProductionWorkstepEntryList getProductionWorksteps() {
		return productionWorksteps;
	}
	public void setProductionWorksteps(ProductionWorkstepEntryList productionWorksteps) {
		this.productionWorksteps = productionWorksteps;
	}
	public Integer getTargetStockId() {
		return targetStockId;
	}
	public void setTargetStockId(Integer targetStockId) {
		this.targetStockId = targetStockId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemDescription2() {
		return itemDescription2;
	}
	public void setItemDescription2(String itemDescription2) {
		this.itemDescription2 = itemDescription2;
	}
	public DocumentInfoEntryList getDocumentInfoEntries() {
		return documentInfoEntries;
	}
	public void setDocumentInfoEntries(DocumentInfoEntryList documentInfoEntries) {
		this.documentInfoEntries = documentInfoEntries;
	}
	public ItemCommentMediaInfoEntryList getItemCommentMediaInfoEntries() {
		return itemCommentMediaInfoEntries;
	}
	public void setItemCommentMediaInfoEntries(ItemCommentMediaInfoEntryList itemCommentMediaInfoEntries) {
		this.itemCommentMediaInfoEntries = itemCommentMediaInfoEntries;
	}
	public String getItemCnr() {
		return itemCnr;
	}
	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}
	public String getOrderCnr() {
		return orderCnr;
	}
	public void setOrderCnr(String orderCnr) {
		this.orderCnr = orderCnr;
	}
	public String getManufactoringPlace() {
		return manufactoringPlace;
	}
	public void setManufactoringPlace(String manufactoringPlace) {
		this.manufactoringPlace = manufactoringPlace;
	}
	public TestPlanEntryList getTestPlanEntries() {
		return testPlanEntries;
	}
	public void setTestPlanEntries(TestPlanEntryList testPlanEntries) {
		this.testPlanEntries = testPlanEntries;
	}
}
