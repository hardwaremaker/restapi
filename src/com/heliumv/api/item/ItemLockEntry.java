package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class ItemLockEntry extends BaseEntryId {
	private String cause;
	private Integer lockId;
	private String description;
	private boolean locked;
	private boolean lockedProduction;
	private boolean lockedPartlist;
	private boolean lockedByProduction;
	private boolean lockedSale;
	private boolean lockedPurchasing;
	
	public ItemLockEntry() {
	}
	
	public ItemLockEntry(Integer itemId) {
		super(itemId);
	}
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public Integer getLockId() {
		return lockId;
	}
	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isLockedProduction() {
		return lockedProduction;
	}
	public void setLockedProduction(boolean lockedProduction) {
		this.lockedProduction = lockedProduction;
	}
	public boolean isLockedPartlist() {
		return lockedPartlist;
	}
	public void setLockedPartlist(boolean lockedPartlist) {
		this.lockedPartlist = lockedPartlist;
	}
	public boolean isLockedByProduction() {
		return lockedByProduction;
	}
	public void setLockedByProduction(boolean lockedByProduction) {
		this.lockedByProduction = lockedByProduction;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isLockedSale() {
		return lockedSale;
	}
	public void setLockedSale(boolean lockedSale) {
		this.lockedSale = lockedSale;
	}
	public boolean isLockedPurchasing() {
		return lockedPurchasing;
	}
	public void setLockedPurchasing(boolean lockedPurchasing) {
		this.lockedPurchasing = lockedPurchasing;
	}
}
