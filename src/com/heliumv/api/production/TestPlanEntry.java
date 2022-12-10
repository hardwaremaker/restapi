package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanEntry extends BaseEntryId {
	
	private TestTypeEnum testType;
	private Integer productionId;
	private Integer sortOrder;
	private String description;
	private String comment;
	
	public TestTypeEnum getTestType() {
		return testType;
	}

	public void setTestType(TestTypeEnum testType) {
		this.testType = testType;
	}

	public Integer getProductionId() {
		return productionId;
	}

	public void setProductionId(Integer productionId) {
		this.productionId = productionId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
