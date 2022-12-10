package com.heliumv.api.purchaseorder;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreatedPurchaseOrderProposalPositionEntry {

	private Integer proposalPositionId;
	
	/**
	 * 
	 * @return Id der neu erzeugten Bestellvorschlagsposition
	 */
	public Integer getProposalPositionId() {
		return proposalPositionId;
	}
	public void setProposalPositionId(Integer proposalPositionId) {
		this.proposalPositionId = proposalPositionId;
	}
}
