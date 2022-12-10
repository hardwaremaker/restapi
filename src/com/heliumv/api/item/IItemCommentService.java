package com.heliumv.api.item;

import java.rmi.RemoteException;

import javax.naming.NamingException;

public interface IItemCommentService {

	/**
	 * Liefert Infos aller Artikelkommentare eines Artikels, aller in <code>MimeTypeEnum</code> definierten Mime-Types
	 * 
	 * @param itemId Id des Artikels
	 * @return (leere) Liste von Artikelkommentaren (ohne den Daten selbst)
	 * @throws NamingException 
	 * @throws RemoteException 
	 */
	ItemCommentMediaInfoEntryList getCommentsMedia(Integer itemId) throws RemoteException, NamingException;

	/**
	 * Liefert Infos aller Artikelkommentare eines Artikels, nach den Filterkriterien, die im <code>ItemCommentFilter</code> definiert sind.
	 * 
	 * @param itemId Id des Artikels
	 * @param filter Filter f&uuml;r die Auswahl von Artikelkommentaren f&uuml;r die Ergebnisliste
	 * @return (leere) Liste von Artikelkommentaren (ohne den Daten selbst)
	 * @throws NamingException 
	 * @throws RemoteException 
	 */
	ItemCommentMediaInfoEntryList getCommentsMedia(Integer itemId, ItemCommentFilter filter) throws RemoteException, NamingException;

	/**
	 * Erstellt den Default-Filter f&uuml;r die Artikelkommentarsuche
	 * 
	 * @return Default-Filter
	 */
	ItemCommentFilter createDefaultCommentFilter();

	ItemCommentMediaInfoEntryList getCommentsMediaContent(Integer itemId, ItemCommentFilter filter)
			throws RemoteException, NamingException;
}
