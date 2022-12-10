package com.heliumv.api.forecast;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import com.lp.util.EJBExceptionLP;

public interface IForecastApi {

	/**
	 * Liefert eine Liste aller Forecastlieferadressen
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param filterDeliverable mit <code>true</code> nur jene mit lieferbaren Positionen 
	 * @return eine (leere) Liste von Forecastlieferadressen entsprechend den Filterkriterien 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	ForecastDeliveryAddressEntryList getDeliveryAddresses(
			String userId,
			Boolean filterDeliverable) throws RemoteException, EJBExceptionLP;
	
	/**
	 * Liefert eine Liste aller Forecastpositionen zur gegebenen Lieferadresse
	 * 
	 * @param deliveryAddressId Id der Forecastlieferadresse, gibt es die Forecastlieferadresse nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param filterDeliverable mit <code>true</code> nur lieferbare Positionen
	 * @param pickingType der gew&uuml;nschte Kommissioniertyp
	 * @return eine (leere) Liste von Forecastpositionen entsprechend den Filterkriterien
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws NamingException 
	 */
	ForecastPositionEntryList getPositionsByDeliveryAddress(
			Integer deliveryAddressId,
			String userId,
			Boolean filterDeliverable,
			PickingType filterPickingType) throws RemoteException, EJBExceptionLP, NamingException;
	
	/**
	 * Liefert eine Liste aller Forecastpositionen
	 *  
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param filterDeliverable mit <code>true</code> nur lieferbare Positionen
	 * @return eine (leere) Liste von Forecastpositionen entsprechend den Filterkriterien
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws NamingException 
	 */
	ForecastPositionEntryList getPositions(
			String userId,
			Boolean filterDeliverable) throws RemoteException, EJBExceptionLP, NamingException;
	
	/**
	 * Eine Forecastposition anhand seiner Id ermitteln
	 * 
	 * @param positionid Id der gesuchten Forecastposition
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param filterDeliverable
	 * @return die Forecastposition sofern vorhanden, gibt es die Forecastposition nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @throws RemoteException
	 * @throws NamingException
	 */
	ForecastPositionEntry findForecastposition(
			Integer positionid,
			String userId,
			Boolean filterDeliverable) throws RemoteException, NamingException;
	
	/**
	 * Einen Linenabruf anhand seiner Id ermitteln
	 * 
	 * @param linecallid Id des gesuchten Linienabrufs
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return den Linienabruf sofern vorhanden, gibt es den Linienabruf nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 */
	LinecallEntry findLinecall(
			Integer linecallid,
			String userId);
	
	/**
	 * Erzeugt eine Ausgabe eines Artikels des Linienabrufs
	 * <p>Wurde die Produktion &uuml;ber die Resource /material/{positionid}/delivery noch nicht gestartet,
	 * wird mit StatusCode <code>BAD_REQUEST (400)</code> geantwortet
	 * </p>
	 * @param linecallid Id des Linienabrufs, gibt es den Linienabruf nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @param deliveryEntry
	 * @return ein ResultEntry mit der noch offenen Menge
	 * @throws RemoteException
	 */
	LinecallDeliveryResultEntry createDelivery(
			Integer linecallid,
			LinecallDeliveryPostEntry deliveryEntry) throws RemoteException;
	
	/**
	 * Startet die Produktion eines Positionsartikels
	 * 
	 * @param positionid Id der Forecastposition, gibt es die Forecastposition nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return Forecastposition mit den zu liefernden Materialien
	 * @throws RemoteException
	 * @throws NamingException
	 */
	ForecastPositionEntry startDelivery(
			Integer positionid,
			String userId) throws RemoteException, NamingException;
	
	/**
	 * Liefert die zur Forecastposition zugeh&ouml;rigen Linienabrufe, mitsamt den zu liefernden Materialien
	 * 
	 * @param positionid Id der Forecastposition, gibt es die Forecastposition nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return Liste der Linienabrufe
	 * @throws RemoteException
	 * @throws NamingException
	 */
	LinecallEntryList getDelivery(
			Integer positionid,
			String userId) throws RemoteException, NamingException;
	
	/**
	 * Ein Versandetikett zu einer sich in Produktion befindlichen Forecastposition als JRPrint erhalten.
	 * 
	 * @param positionid Id der Forecastposition, gibt es die Forecastposition nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return Versandetikett als JRPrint
	 * @throws NamingException
	 * @throws IOException
	 */
	Response printDispatchLabel(
			Integer positionid,
			String userId) throws NamingException, IOException;
	
	/**
	 * Erzeugt eine Ausgabe eines Artikels der Forecastposition
	 * <p>Wurde die Produktion &uuml;ber die Resource /material/{positionid}/delivery noch nicht gestartet,
	 * wird mit StatusCode <code>BAD_REQUEST (400)</code> geantwortet
	 * </p>
	 * @param positionid Id der Forecastposition, gibt es die Forecastposition nicht, wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @param deliveryEntry
	 * @return die Forecastposition mit den ver&auml;nderten Mengen
	 * @throws RemoteException
	 * @throws NamingException 
	 * @throws EJBExceptionLP 
	 */
	ForecastPositionEntry createForecastpositionDelivery(
			Integer positionid,
			LinecallDeliveryPostEntry deliveryEntry) throws RemoteException, EJBExceptionLP, NamingException;
	
	void createDelivery(
			Integer positionid,
			String userId,
			PickingType pickingType) throws RemoteException, NamingException;

	/**
	 * Liefert eine Liste aller definierten Drucker der Kommissionierung
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return eine (leere) Liste aller Kommissionierdrucker
	 */
	PickingPrinterEntryList getPickingPrinter(
			String userId);
}
