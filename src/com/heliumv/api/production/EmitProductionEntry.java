package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class EmitProductionEntry extends BaseEntryId {
	private boolean emitted;
	private boolean missingPartsWarning;
	private ItemHintEntryList itemHints;
	private ItemLockEntryList itemLocks;
	private boolean locked;
	private String postMessage;
	private String requiredPositionHints;
	
	public EmitProductionEntry() {
		setEmitted(false);
		setItemHints(new ItemHintEntryList());
		setItemLocks(new ItemLockEntryList());
	}

	/**
	 * Wurde das Los ausgegeben? 
	 * @return true wenn das Los ausgegeben wurde
	 */
	public boolean isEmitted() {
		return emitted;
	}

	public void setEmitted(boolean emitted) {
		this.emitted = emitted;
	}

	/**
	 * Eine List aller textbasierten Artikelhinweise
	 * @return eine (leere) Liste von Hinweisen
	 */
	public ItemHintEntryList getItemHints() {
		return itemHints;
	}

	public void setItemHints(ItemHintEntryList itemHints) {
		this.itemHints = itemHints;
	}

	public ItemLockEntryList getItemLocks() {
		return itemLocks;
	}

	public void setItemLocks(ItemLockEntryList itemLocks) {
		this.itemLocks = itemLocks;
		this.setLocked(itemLocks != null && itemLocks.getEntries().size() > 0); 
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isMissingPartsWarning() {
		return missingPartsWarning;
	}

	public void setMissingPartsWarning(boolean hasMissingPartsWarning) {
		this.missingPartsWarning = hasMissingPartsWarning;
	}

	public String getPostMessage() {
		return postMessage;
	}

	public void setPostMessage(String postMessage) {
		this.postMessage = postMessage;
	}

	public String getRequiredPositionHints() {
		return requiredPositionHints;
	}

	public void setRequiredPositionHints(String requiredPositionHints) {
		this.requiredPositionHints = requiredPositionHints;
	}
}
