package com.heliumv.api.property;

import java.rmi.RemoteException;

import com.heliumv.api.item.ItemIdentityEntry;
import com.heliumv.api.item.ItemPropertyEntry;
import com.heliumv.api.item.ItemPropertyEntryList;
import com.heliumv.api.system.PropertyLayoutEntryList;

public interface IPropertyApi {

	/**
	 * Ermittelt die komplette Beschreibung der <code>ARTIKELEIGENSCHAFTEN</code>
	 * dieses Artikels</br>
	 * 
	 * @param itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken ist der beim Logon ermittelte "token" (optional)
	 * @return eine (leere) Liste aller Eigenschaften, die f&uuml;r diesen
	 * Artikel definiert worden sind
	 * @throws RemoteException
	 */
	PropertyLayoutEntryList getItemPropertyLayout(Integer itemId,
			String userId, String headerToken) throws RemoteException;

	/**
	 * Ermittelt die komplette Beschreibung der <code>CHARGENEIGENSCHAFTEN</code>
	 * dieses Artikels</br>
	 * 
	 * @param itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken ist der beim Logon ermittelte "token" (optional)
	 * @return eine (leere) Liste aller Eigenschaften, die f&uuml;r diesen
	 * Artikel definiert worden sind
	 * @throws RemoteException
	 */
	PropertyLayoutEntryList getItemIdentityPropertyLayout(Integer itemId, 
			String userId, String headerToken) throws RemoteException;

	/**
	 * Die CHARGENEIGENSCHAFTEN des Artikels und seiner Charge / Seriennummer 
	 * ermitteln</br>
	 * 
	 * @param itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken ist der beim Logon ermittelte "token" (optional)
	 * @param identity die Datenstruktur die die betreffende Identit&auml;t (Chargennummer 
	 * bzw. Seriennummer) enth&auml;lt
	 * @return eine (leere) Liste aller bisher definierten Eigenschaften f&uuml;r den
	 * Artikel der durch  die Artikel-Id <code>itemId</code> und seine <code>identity</code> bestimmt ist.</br>
	 * Statuscodes sind:</br>
	 * <ul>
	 * <li>400 - wenn der betreffende Artikel mit <code>itemId</code> weder chargen- noch seriennummernbehaftet ist</li>
	 * <li>404 - wenn <code>layoutId</code> oder die Kombination aus <code>itemId</code> und <code>identity</code>
	 * nicht gefunden werden konnte</li>
	 * </ul>
	 * @throws RemoteException
	 */
	ItemPropertyEntryList getItemIdentityProperties(Integer itemId, String userId, String headerToken,
			ItemIdentityEntry identity) throws RemoteException;

	/**
	 * Eine bestimmte Eigenschaft der CHARGENEIGENSCHAFT des angegebenen Artikels
	 * (und der zugeh&ouml;rigen Charge/Seriennummer) ermitteln</br>
	 * @param itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken ist der beim Logon ermittelte "token" (optional)
	 * @param layoutId eine der Ids die &uuml;ber <code>.../layouts</code> ermittelt wurde
	 * @param identity die Datenstruktur die die betreffende Identit&auml;t (Chargen- 	 
	 * bzw. Seriennummer) enth&auml;lt
	 * @return eine (leere) Liste mit der angeforderten <code>layoutId</code>.</br>
	 * Statuscodes sind:</br>
	 * <ul>
	 * <li>400 - wenn der betreffende Artikel mit <code>itemId</code> weder chargen- noch seriennummernbehaftet ist</li>
	 * <li>404 - wenn <code>layoutId</code> oder die Kombination aus <code>itemId</code> und <code>identity</code>
	 * nicht gefunden werden konnte</li>
	 * </ul>
	 * 
	 * @throws RemoteException
	 */
	ItemPropertyEntry getItemIdentityProperty(Integer itemId, String userId, String headerToken, Integer layoutId,
			ItemIdentityEntry identity) throws RemoteException;

	/**
	 * Die CHARGENEIGENSCHAFT des angegebenen Artikels
	 * (und der zugeh&ouml;rigen Charge/Seriennummer) neu setzen</br>
	 * <p>Sollte es bereits Eigenschaften gegeben haben, werden diese zuvor entfernt
	 * und dann jene Eigenschaften gesetzt, die mit diesem Aufruf angegeben worden sind.</p>
	 * @param itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken ist der beim Logon ermittelte "token" (optional)
	 * @param properties die Datenstruktur, die die betreffende Identit&auml;t (Chargennummer 
	 * bzw. Seriennummer) und Eigenschaften enth&auml;lt
	 * @return eine (leere) Liste aller neu gesetzten Eigenschaften</br>
	 * Statuscodes sind:</br>
	 * <ul>
	 * <li>400 - wenn der betreffende Artikel mit <code>itemId</code> weder chargen- noch seriennummernbehaftet ist</li>
	 * <li>404 - wenn eine <code>layoutId</code> oder die Kombination aus <code>itemId</code> und <code>identity</code>
	 * nicht gefunden werden konnte</li>
	 * </ul>
	 * 
	 * @throws RemoteException
	 */
	ItemPropertyEntryList postItemIdentityProperties(Integer itemId, String userId, String headerToken,
			CreateItemIdentityPropertyEntryList properties) throws RemoteException;

	/**
	 * Die CHARGENEIGENSCHAFT des angegebenen Artikels (und der zugeh&ouml;rigen Chargen/Seriennummer)
	 * neu setzen</br>
	 * <p>Es werden dabei nur jene Eigenschaften neu gesetzt, die in diesem Aufruf mit ihrer 
	 * <code>layoutId</code> angegeben worden sind.</p>
	 * @param itemId  itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken  ist der beim Logon ermittelte "token" (optional)
	 * @param properties die Datenstruktur, die die betreffende Identit&auml;t (Chargennummer 
	 * bzw. Seriennummer) und Eigenschaften enth&auml;lt
	 * @return eine (leere) Liste aller neu gesetzten Eigenschaften</br>
	 * Statuscodes sind:</br>
	 * <ul>
	 * <li>400 - wenn der betreffende Artikel mit <code>itemId</code> weder chargen- noch seriennummernbehaftet ist</li>
	 * <li>404 - wenn eine <code>layoutId</code> oder die Kombination aus <code>itemId</code> und <code>identity</code>
	 * nicht gefunden werden konnte</li>
	 * </ul>
	 * @throws RemoteException
	 */
	ItemPropertyEntryList putItemIdentityProperties(Integer itemId, String userId, String headerToken,
			CreateItemIdentityPropertyEntryList properties) throws RemoteException;

	/**
	 * Die CHARGENEIGENSCHAFT des angegebenen Artikels (und der zugeh&ouml;rigen Chargen/Seriennummer)
	 * neu setzen</br>
	 * <p>Es wird dabei nur die angegebene Eigenschaften neu gesetzt, die in diesem Aufruf mit ihrer 
	 * <code>layoutId</code> angegeben worden ist.</p>
	 * 
	 * @param itemId  itemId die Id des betreffenden Artikels
	 * @param userId ist der beim Logon ermittelte "token" (optional)
	 * @param headerToken  ist der beim Logon ermittelte "token" (optional)
	 * @param properties die Datenstruktur, die die betreffende Identit&auml;t (Chargennummer 
	 * bzw. Seriennummer) und Eigenschaften enth&auml;lt
	 * @param layoutId die Id der Beschreibung des Datenfeldes dessen Wert neu gesetzt werden soll
	 * @param property die Datenstruktur die die Chargen/Seriennummer enth&auml;lt und die Daten
	 * der neue zu setzende Eigenschaft
	 * @return eine (leere) Liste der neu gesetzten Eigenschaften</br>
	 * Statuscodes sind:</br>
	 * <ul>
	 * <li>400 - wenn der betreffende Artikel mit <code>itemId</code> weder chargen- noch seriennummernbehaftet ist</li>
	 * <li>404 - wenn eine <code>layoutId</code> oder die Kombination aus <code>itemId</code> und <code>identity</code>
	 * nicht gefunden werden konnte</li>
	 * </ul>
	 * @throws RemoteException
	 */
	ItemPropertyEntryList putItemIdentityProperty(Integer itemId, String userId, String headerToken, Integer layoutId,
			CreateItemIdentityPropertyEntry property) throws RemoteException;
}
