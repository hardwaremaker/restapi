/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.factory;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import com.lp.server.lieferschein.service.BestaetigterLieferscheinDto;
import com.lp.server.lieferschein.service.ILieferscheinAviso;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.TheClientDto;

public interface ILieferscheinCall {
	LieferscheinDto lieferscheinFindByPrimaryKey(Integer lieferscheinId) throws RemoteException ;
	
	LieferscheinDto lieferscheinFindByPrimaryKey(
			Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException ;

	LieferscheinDto lieferscheinFindByCNr(String cnr) throws RemoteException ;

	LieferscheinDto lieferscheinFindByCNr(String cnr, String clientCnr) throws RemoteException ;
	
	/**
	 * Ein LieferscheinAviso erzeugen
	 *
	 * @param lieferscheinDto
	 * @param theClientDto
	 * @return das erzeugte LieferscheinAviso
	 * @throws NamingException
	 * @throws RemoteException
	 */
	ILieferscheinAviso createLieferscheinAviso(
			Integer lieferscheinId, TheClientDto theClientDto) throws NamingException, RemoteException ;

	/**
	 * Ein Aviso zu einem String-Content transformieren
	 *
	 * @param lieferscheinDto
	 * @param lieferscheinAviso
	 * @param theClientDto
	 * @return
	 * @throws NamingException
	 * @throws RemoteException
	 */
	String getLieferscheinAvisoAsString(
			LieferscheinDto lieferscheinDto, ILieferscheinAviso lieferscheinAviso,
			TheClientDto theClientDto) throws NamingException, RemoteException ;

	/**
	 * Ein Lieferscheinaviso erzeugen und als transformierten String zurueckliefern
	 * 
	 * @param lieferscheinDto
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 */
	String createLieferscheinAvisoToString(
			Integer lieferscheinId, TheClientDto theClientDto) throws NamingException, RemoteException ;
	
	/**
	 * Ein LieferscheinAviso erzeugen und versenden 
	 * 
	 * @param lieferscheinDto
	 * @param theClientDto
	 * @return den Aviso-Inhalt als String
	 * @throws RemoteException
	 * @throws NamingException
	 */
	String createLieferscheinAvisoPost(
			Integer lieferscheinId, TheClientDto theClientDto) throws NamingException, RemoteException ;	
	
	/**
	 * Zu einem Auftrag zugehoerige Lieferscheine (egal welcher Status) ermitteln
	 * @param auftragId die Auftrag-IId zu der die Lieferscheine ermittelt werden sollen
	 * @return alle zu diesem Auftrag zugehoerige Lieferscheine, kann auch leer sein, aber nicht null
	 * @throws RemoteException
	 */
	LieferscheinDto[] lieferscheinFindByAuftrag(Integer auftragId) throws RemoteException ;

	LieferscheinDto[] lieferscheinFindByAuftrag(Integer auftragId, TheClientDto theClientDto) throws RemoteException ;
	
	LieferscheinpositionDto getLieferscheinpositionByLieferscheinAuftragposition(
			Integer lieferscheinId, Integer auftragPositionId) throws RemoteException ;	
	
	void berechneAktiviereBelegControlled(Integer deliveryId) throws RemoteException ;
	
	Integer createLieferschein(LieferscheinDto lieferscheinDto) throws RemoteException ;
	
	LieferscheinDto setupDefaultLieferschein(KundeDto kundeDto) throws RemoteException ;

	LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNr(Integer kundeId) throws RemoteException ;

	LieferscheinDto lieferscheinFindByPrimaryKeyOhneExc(Integer lieferscheinIId);

	void archiveSignedResponse(BestaetigterLieferscheinDto bestaetigungDto) throws RemoteException;
}
