package com.heliumv.api.item;

import java.util.List;


public interface IItemApi {
	/**
	 * Einen Artikel anhand seiner Artikelnummer ermitteln</br>
	 * 
	 * 
	 * @param userId der bei HELIUM V angemeldete API Benutzer
	 * @param cnr (optional) die gesuchte Artikelnummer
	 * @param serialnumber (optional) die Seriennumber des Artikels</br>
	 * <p>Eineindeutige Artikel k�nnen �ber ihre Seriennummer ermittelt werden. Dabei wird
	 * zuerst im aktuellen Lagerstand gesucht, danach in den Abgangsbuchungen. Ist die <code>cnr</code>
	 * ebenfalls angegeben, muss der Artikel der �ber die Seriennummer ermittelt wurde mit der 
	 * angegebenen Artikelnummer �bereinstimmen.</p>
	 * @param addComments (optional) mit true die Artikelkommentar ebenfalls liefern
	 * @return
	 */
	ItemEntry findItemByAttributes(String userId, String cnr, String serialnumber, Boolean addComments) ;


	/**
	 * Eine Liste aller Artikel ermitteln.</br>
	 * <p>Das Ergebnis kann dabei durch Filter eingeschr�nkt werden</p>
	 * 
	 * @param userId der angemeldete HELIUM V Benutzer
	 * @param limit die maximale Anzahl von zur�ckgelieferten Datens�tzen
	 * @param startIndex die Id desjenigen Satzes mit dem begonnen werden soll
	 * @param filterCnr die (optionale) Artikelnummer nach der die Suche eingeschr�nkt werden soll
	 * @param filterTextSearch der (optionale) Text der die Suche einschr�nkt 
	 * @param filterDeliveryCnr die (optionale) Lieferantennr. bzw Bezeichnung
	 * @param filterItemGroupClass die (optionale) Artikelgruppe bzw. Artikelklasse
	 * @param filterItemReferenceNr die (optionale) Artikelreferenznummer
	 * @param filterWithHidden mit <code>true</code> werden auch versteckte Artikel in die Suche einbezogen
	 * @return
	 */
	List<ItemEntry> getItems(String userId, Integer limit, Integer startIndex, 
			String filterCnr, String filterTextSearch, String filterDeliveryCnr, String filterItemGroupClass,
			String filterItemReferenceNr,
			Boolean filterWithHidden) ;
	
	/**
	 * Liefert eine Liste aller Lagerstaende dieses Artikels</br>
	 * <p>Es werden nur Lager geliefert, die einen Lagerstand > 0 haben. Es werden nur jene
	 * Lagerst�nde geliefert, f�r die der Benutzer das Recht hat das jeweilige Lager zu benutzen.</p>
	 * 
	 * @param userId der angemeldete API Benutzer
	 * @param itemCnr die gesuchte Artikelnummer
	 * @param returnItemInfo mit <code>true</code> werden neben den Lagerst�nden auch die Daten des
	 * betreffenden Artikels zur�ckgeliefert.
	 * @return
	 */
	List<StockAmountEntry> getStockAmount(String userId, String itemCnr, Boolean returnItemInfo) ;	
}
