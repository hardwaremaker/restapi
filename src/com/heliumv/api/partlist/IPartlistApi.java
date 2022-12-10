package com.heliumv.api.partlist;

import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import com.lp.util.EJBExceptionLP;

public interface IPartlistApi {
	PartlistEntryList getPartlists(
			String userId,
			Integer limit,
			Integer startIndex, 
			String filterCnr, 
			String filterTextSearch,
			Boolean filterWithHidden) throws RemoteException, NamingException, EJBExceptionLP ;

	PartlistPositionEntryList getBrowsePositions(
			Integer partlistId,
			String userId,
			Integer limit,
			Integer startIndex) throws RemoteException, NamingException, EJBExceptionLP ;

	PartlistPositionEntryList getPositions(
			Integer partlistId,
			String userId, Boolean withPrice) throws RemoteException, NamingException, EJBExceptionLP ;
	
	PartlistPositionEntry createPosition(Integer partlistId, String userId,
			PartlistPositionPostEntry positionEntry) throws RemoteException, NamingException, EJBExceptionLP ;
	PartlistPositionEntry updatePosition(Integer partlistId, String userId,
			PartlistPositionPostEntry positionEntry) throws RemoteException, NamingException, EJBExceptionLP ;
	
	/**
	 * Eine St&uuml;cklistenposition entfernen
	 * 
	 * @param partlistId die St&uuml;cklisten-Id die die Position enth&auml;lt
	 * @param userId der angemeldete Benutzer
	 * @param partlistPositionId die Position die entfernt werden soll
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	void removePosition(
			Integer partlistId,
			String userId,
			Integer partlistPositionId) throws RemoteException, NamingException, EJBExceptionLP ;	
	
	/**
	 * Eine St&uuml;cliste als PDF 'drucken', bzw. empfangen
	 * 
	 * @param partlistId die zu druckende St&uuml;ckliste
	 * @param userId der angemeldete Benutzer
	 * @return das PDF zur St&uuml;ckliste, sofern diese erfolgreich ausgedruckt werden kann, ansonsten
	 * ein allgemeiner HTML-Text zum "Fehler"
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	Response printPartlist(
			Integer partlistId,
			String userId) throws RemoteException, NamingException, EJBExceptionLP ;
	
	/**
	 * Eine Email eines Kunden zu einer Stueckliste erstellen
	 * 
	 * @param partlistId die betreffende St&uuml;ckliste
	 * @param userId der angemeldete Benutzer
	 * @param emailEntry die zu versendende Email
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 * @throws Exception
	 */
	void sendEmail(
			Integer partlistId,
			String userId,
			PartlistEmailEntry emailEntry) throws RemoteException, NamingException, EJBExceptionLP, Exception ;
	
	/**
	 * Die Liste der Arbeitsg&auml;nge des Arbeitsplans
	 * 
	 * @param partlistId die betreffende St&uuml;ckliste
	 * @param userId ist der beim Logon ermittelte "token"
	 * @return eine (leere) Liste von Arbeitsg&auml;ngen der betreffenden St&uuml;ckliste
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	PartlistWorkstepEntryList getWorksteps(Integer partlistId, String userId) 
			throws RemoteException, NamingException, EJBExceptionLP;

	/**
	 * Die Liste aller Fertigungsgruppen
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param limit
	 * @param startIndex
	 * @return eine (leere) Liste aller Fertigungsgruppen
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	ProductionGroupEntryList getProductionGroups(
			String userId,
			Integer limit,
			Integer startIndex) throws NamingException, RemoteException, EJBExceptionLP ;
	
	/**
	 * Die Liste aller Montagearten
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @return eine (leere) Liste aller Montagearten
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws NamingException
	 */
	MountingMethodEntryList getMountingMethods(
			String userId) throws RemoteException, EJBExceptionLP, NamingException;
}
