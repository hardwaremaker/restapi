package com.heliumv.api.forecast;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class LinecallEntry extends BaseEntryId {

	private BigDecimal quantity;
	private String line;
	private String sector;
	private String sectorDescription;
	private Long productionDateMs;
	private String ordernumber;
	private List<LinecallItemEntry> linecallItemEntries;
	
	public LinecallEntry() {
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSectorDescription() {
		return sectorDescription;
	}

	public void setSectorDescription(String sectorDescription) {
		this.sectorDescription = sectorDescription;
	}

	public Long getProductionDateMs() {
		return productionDateMs;
	}

	public void setProductionDateMs(Long productionDateMs) {
		this.productionDateMs = productionDateMs;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public List<LinecallItemEntry> getLinecallItemEntries() {
		return linecallItemEntries;
	}

	public void setLinecallItemEntries(List<LinecallItemEntry> linecallItemEntries) {
		this.linecallItemEntries = linecallItemEntries;
	}

}
