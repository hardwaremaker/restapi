package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemV1Entry;

@XmlRootElement
public class ProductionTargetMaterialEntry extends BaseEntryId {

	private Integer productionId;
	private BigDecimal amount;
	private String unitCnr;
	private String position;
	private String comment;
	private BigDecimal price;
	private Integer iSort;
	private ItemV1Entry itemEntry;
	private BigDecimal amountIssued;
	private Integer mountingMethodId;
	private Boolean belatedWithdrawn;
	
	public Integer getProductionId() {
		return productionId;
	}
	public void setProductionId(Integer productionId) {
		this.productionId = productionId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getUnitCnr() {
		return unitCnr;
	}
	public void setUnitCnr(String unitCnr) {
		this.unitCnr = unitCnr;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getiSort() {
		return iSort;
	}
	public void setiSort(Integer iSort) {
		this.iSort = iSort;
	}
	public ItemV1Entry getItemEntry() {
		return itemEntry;
	}
	public void setItemEntry(ItemV1Entry itemEntry) {
		this.itemEntry = itemEntry;
	}
	
	public BigDecimal getAmountIssued() {
		return amountIssued;
	}
	public void setAmountIssued(BigDecimal amountIssued) {
		this.amountIssued = amountIssued;
	}
	public Integer getMountingMethodId() {
		return mountingMethodId;
	}
	public void setMountingMethodId(Integer mountingMethodId) {
		this.mountingMethodId = mountingMethodId;
	}
	public Boolean getBelatedWithdrawn() {
		return belatedWithdrawn;
	}
	public void setBelatedWithdrawn(Boolean belatedWithdrawn) {
		this.belatedWithdrawn = belatedWithdrawn;
	}
}
