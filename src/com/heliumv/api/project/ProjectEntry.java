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
package com.heliumv.api.project;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class ProjectEntry extends BaseEntryId {
	private String cnr ;
	private String customerName ;
	private String category ;
	private String title ;
	private Integer customerPartnerId ;
	private String customerAddress ;
	private Integer priority ;
	private long deadlineMs ;
	private Boolean internalDone ;
	private String internalComment;
	private ProjectDocumentStatus status ;
	private String statusCnr ;
	
	public String getCnr() {
		return cnr;
	}
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCustomerPartnerId() {
		return customerPartnerId;
	}
	public void setCustomerPartnerId(Integer customerPartnerId) {
		this.customerPartnerId = customerPartnerId;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public long getDeadlineMs() {
		return deadlineMs;
	}
	public void setDeadlineMs(long deadlineMs) {
		this.deadlineMs = deadlineMs;
	}
	public Boolean getInternalDone() {
		return internalDone;
	}
	public void setInternalDone(Boolean internalDone) {
		this.internalDone = internalDone;
	}
	public String getInternalComment() {
		return internalComment;
	}
	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}
//	public ProjectDocumentStatus getStatus() {
//		return status;
//	}
//	public void setStatus(ProjectDocumentStatus status) {
//		this.status = status;
//	}
	public String getStatusCnr() {
		return statusCnr;
	}
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}
}
