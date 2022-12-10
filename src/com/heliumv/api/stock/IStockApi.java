package com.heliumv.api.stock;

import java.rmi.RemoteException;

import javax.naming.NamingException;


public interface IStockApi {
	/**
	 * Eine Liste aller Lager
	 * 
	 * @param userId des bei HELIUM V angemeldeten API Benutzer
	 * @return eine (leere) Liste aller Lager die dem angemeldeten Benutzer zug&auml;nglich sind
	 */
	StockEntryList getStockList(String userId) ;

	/**
	 * Ordnet einen Artikel zu einem existierenden Lagerplatz zu.
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param stockId ist die Id des Lagers in dem sich der Lagerplatz befindet
	 * @param postEntry sind die Daten des Lagerplatzes
	 * @return die Id des Lagerplatzes
	 * @throws RemoteException
	 */
	Integer createStockPlace(
			String userId, 
			Integer stockId,
			StockPlacePostEntry postEntry) throws RemoteException;

	/**
	 * L&ouml;scht die Zuordnung eines Artikel zu einem Lagerplatz
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param stockId ist die Id des Lagers in dem sich der Lagerplatz befindet
	 * @param stockplaceid ist die Id des Lagerplatzes
	 * @param itemId ist die Id des Artikels
	 * @throws RemoteException
	 */
	void deleteStockPlace(
			String userId, 
			Integer stockId, 
			Integer stockplaceid,
			Integer itemId) throws RemoteException;

	/**
	 * Lagerplatz optional &uuml;ber seine Id oder seinen Namen finden.</br> 
	 * Der Lagerplatz enth&auml;lt auch eine Liste aller Artikel, die diesem zugeordnet sind.
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param stockId ist die Id des Lagers in dem sich der Lagerplatz befindet
	 * @param stockplaceId (optional) ist die Id des Lagerplatzes
	 * @param stockplaceName (optional) ist der Name des Lagerplatzes
	 * @param addStockAmountInfos (optional) f&uuml;gt Lagerinformation hinzu, der Lagerstand darin ist jener des Lagers der Ressource (<code>stockid</code>)
	 * @return Lagerplatz
	 * @throws RemoteException
	 */
	StockPlaceEntry findStockPlace(
			String userId, 
			Integer stockId,
			Integer stockplaceId, 
			String stockplaceName,
			Boolean addStockAmountInfos) throws RemoteException;

	/**
	 * Einen Lagerzugang durchf&uuml;hren</br>
	 * <p>Der Anwender (userId) muss das Recht haben, Handlagerbewegungen 
	 * durchzuf&uuml;hren. Ebenso muss dieser das Recht haben, auf das 
	 * zuzubuchende Lager zuzugreifen.</p>
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde.
	 *   Dieser Anwender ben&ouml;tigt das Recht Handlagerbewegungen durchf&uuml;hren zu d&uuml;rfen
	 * @param stockId  ist die Id des Lagers auf das zugebucht wird. Der Anwender muss das Recht haben auf dieses
	 *   Lager zugreifen zu d&uuml;rfen
	 * @param itemId ist die Id des zuzubuchenden Artikels. Es muss sich um einen
	 *   lagerbewirtschafteten Identartikel handeln
	 * @param postEntry die Datenstruktur, die die Zubuchung definiert
	 * @return die Id der neu erzeugten Handlagerbewegung
	 * @throws RemoteException
	 */
	Integer createStockMovementReceipt(String userId, 
			Integer stockId, Integer itemId, StockMovementEntry postEntry) throws RemoteException;

	/**
	 * Einen Lagerabgang durchf&uuml;hren</br>
	 * <p>Der Anwender (userId) muss das Recht haben, Handlagerbewegungen 
	 * durchzuf&uuml;hren. Ebenso muss dieser das Recht haben, auf das 
	 * abzubuchende Lager zuzugreifen.</p>
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde.
	 *   Dieser Anwender ben&ouml;tigt das Recht Handlagerbewegungen durchf&uuml;hren zu d&uuml;rfen
	 * @param stockId ist die Id des Lagers von dem abgebucht wird. Der Anwender muss das Recht haben auf dieses
	 *   Lager zuzugreifen
	 * @param itemId  ist die Id des zuzubuchenden Artikels. Es muss sich um einen
	 *   lagerbewirtschafteten Identartikel handeln
	 * @param postEntry die Datenstruktur die den Lagerabgang definiert
	 * @return die Id der neu erzeugten Handlagerbewegung
	 * @throws RemoteException
	 */
	Integer createStockMovementIssue(String userId,
			Integer stockId, Integer itemId, StockMovementEntry postEntry) throws RemoteException;

	/**
	 * Eine Lagerumbuchung durchf&uuml;hren</br>
	 * <p>Es wird eine Abbuchung und eine Zubuchung durchgef&auuml;hrt</p>
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde.
	 *   Dieser Anwender ben&ouml;tigt das Recht Handlagerbewegungen durchf&uuml;hren zu d&uuml;rfen
	 * @param stockId ist die Id des Lagers vom dem abgebucht wird. Der Anwender muss das Recht haben auf dieses
	 *   Lager zuzugreifen
	 * @param itemId  ist die Id des umzubuchenden Artikels. Es muss sich um einen
	 *   lagerbewirtschafteten Identartikel handeln
	 * @param changeEntry die Datenstruktur, die die Identit&auml;ten und das Ziellager
	 *    definiert
	 * @return die Id der neu erzeugten Handlagerbewegung
	 * @throws RemoteException
	 */
	Integer createStockMovementChange(String userId, Integer stockId, Integer itemId,
			StockMovementChangeEntry changeEntry) throws RemoteException;

//	/**
//	 * Eine Liste aller Handlagerbewegungen des Artikels auf diesem Lager</br>
//	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde.
//	 *   Dieser Anwender ben&ouml;tigt das Recht Handlagerbewegungen durchf&uuml;hren zu d&uuml;rfen
//	 * @param stockId ist die Id des Lagers vom dem die Handlagerbewegungen ermittelt werden sollen
//	 * @param itemId  ist die Id des Artikels. Es muss sich um einen
//	 *   lagerbewirtschafteten Identartikel handeln
//	 * @return eine (leere) Liste aller Handlagerbewegungen des Artikels auf diesem Lager
//	 * @throws RemoteException
//	 */
//	StockMovementEntryList getStockMovements(String userId, 
//			Integer stockId, Integer itemId) throws RemoteException;
}
