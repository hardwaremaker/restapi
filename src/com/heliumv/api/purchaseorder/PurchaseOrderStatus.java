package com.heliumv.api.purchaseorder;

public enum PurchaseOrderStatus {
	NOTINITIALIZED("UNBEKANNT"),
	NEW("Angelegt       "), 
	OPEN("Offen          "), 
	CONFIRMED("Bestaetigt     "),
	DELIVERED("Geliefert      "),
	DONE("Erledigt       "), 
	CANCELLED("Storniert      "),
	CALLOFFDONE("Abgerufen      "),
	PARTLYDONE("Teilerledigt   ");
	
	PurchaseOrderStatus(String value) {
		this.value = value;
	}
	
	public String getText() { 
		return value;
	}
	
	public static PurchaseOrderStatus fromString(String text) {
		if (text != null) {
			for (PurchaseOrderStatus status : PurchaseOrderStatus.values()) {
				if (text.equalsIgnoreCase(status.value)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}

	private String value;
	
	public static PurchaseOrderStatus lookup(String id) {
		for (PurchaseOrderStatus status : PurchaseOrderStatus.values()) {
			if (status.toString().equalsIgnoreCase(id)) {
				return status;
			}
		}
		
		throw new IllegalArgumentException("No enum '" + id + "'");
	}
}
