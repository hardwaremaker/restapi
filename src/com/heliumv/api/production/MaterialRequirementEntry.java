package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MaterialRequirementEntry {
	
	private Integer productionId;
	private String productionCnr;
	
	private BigDecimal desiredQuantity;
	private Long desiredDateMs;
	private Boolean additional;
	private Boolean returned;
	private String item;
	private String itemDescription;
	private String comment;
	private Integer fromStaffId;
	private Long acquisitionTimeMs;
	private String data;

	public Integer getProductionId() {
		return productionId;
	}

	public void setProductionId(Integer productionId) {
		this.productionId = productionId;
	}

	public String getProductionCnr() {
		return productionCnr;
	}

	public void setProductionCnr(String productionCnr) {
		this.productionCnr = productionCnr;
	}

	public BigDecimal getDesiredQuantity() {
		return desiredQuantity;
	}

	public void setDesiredQuantity(BigDecimal desiredQuantity) {
		this.desiredQuantity = desiredQuantity;
	}

	public Long getDesiredDateMs() {
		return desiredDateMs;
	}

	public void setDesiredDateMs(Long desiredDateMs) {
		this.desiredDateMs = desiredDateMs;
	}

	public Boolean getAdditional() {
		return additional;
	}

	public void setAdditional(Boolean additional) {
		this.additional = additional;
	}

	public Boolean getReturned() {
		return returned;
	}

	public void setReturned(Boolean returned) {
		this.returned = returned;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getFromStaffId() {
		return fromStaffId;
	}

	public void setFromStaffId(Integer fromStaffId) {
		this.fromStaffId = fromStaffId;
	}

	public Long getAcquisitionTimeMs() {
		return acquisitionTimeMs;
	}

	public void setAcquisitionTimeMs(Long aquisitionTimeMs) {
		this.acquisitionTimeMs = aquisitionTimeMs;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String asString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MaterialRequirementEntry: [")
			.append("productionId=").append(getProductionId())
			.append(", productionCnr=").append(getProductionCnr())
			.append(", desiredQuantity=").append(getDesiredQuantity())
			.append(", desiredDateMs=").append(getDesiredDateMs())
			.append(", additional=").append(getAdditional())
			.append(", returned=").append(getReturned())
			.append(", item=").append(getItem())
			.append(", itemDescription=").append(getItemDescription())
			.append(", comment=").append(getComment())
			.append(", fromStaffId=").append(getFromStaffId())
			.append(", acquisitionTimeMs=").append(getAcquisitionTimeMs())
			.append("]");
		return builder.toString();
	}
}
