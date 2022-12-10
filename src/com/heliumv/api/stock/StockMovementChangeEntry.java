package com.heliumv.api.stock;

/**
 * Die Daten der Handlagerbewegung f&uuml;r eine Umbuchung</br>
 * 
 * @author gerold
 */
public class StockMovementChangeEntry extends StockMovementEntry {

	private Integer targetStockId;

	/**
	 * Die Id des Ziellagers</br>
	 * @return Id des Ziellagers
	 */
	public Integer getTargetStockId() {
		return targetStockId;
	}

	public void setTargetStockId(Integer targetStockId) {
		this.targetStockId = targetStockId;
	}
}
