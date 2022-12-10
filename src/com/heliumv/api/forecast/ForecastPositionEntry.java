package com.heliumv.api.forecast;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemV1Entry;

@XmlRootElement
public class ForecastPositionEntry extends BaseEntryId {

	private ItemV1Entry itemEntry;
	private BigDecimal quantity;
	private Long dateMs;
	private String ordernumber;
	private List<LinecallEntry> linecallEntries;
	private Integer staffId;
	private String staffDescription;
	private String productionCnr;
	private ProductType productType;

	public ForecastPositionEntry() {
	}

	public ItemV1Entry getItemEntry() {
		return itemEntry;
	}

	public void setItemEntry(ItemV1Entry itemEntry) {
		this.itemEntry = itemEntry;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Long getDateMs() {
		return dateMs;
	}

	public void setDateMs(Long dateMs) {
		this.dateMs = dateMs;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public List<LinecallEntry> getLinecallEntries() {
		return linecallEntries;
	}

	public void setLinecallEntries(List<LinecallEntry> linecallEntries) {
		this.linecallEntries = linecallEntries;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public String getStaffDescription() {
		return staffDescription;
	}

	public void setStaffDescription(String staffDescription) {
		this.staffDescription = staffDescription;
	}

	public String getProductionCnr() {
		return productionCnr;
	}
	
	public void setProductionCnr(String productionCnr) {
		this.productionCnr = productionCnr;
	}
	
	public ProductType getProductType() {
		return productType;
	}
	
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
}
