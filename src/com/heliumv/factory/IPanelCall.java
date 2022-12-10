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
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.factory.legacy.PaneldatenPair;
import com.lp.server.system.service.CreatePaneldatenResult;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.util.EJBExceptionLP;

public interface IPanelCall {

	PaneldatenDto[] paneldatenFindByPanelCNrCKey(String panelCNr,
			String cKey) throws RemoteException, NamingException, EJBExceptionLP ;
	
	/**
	 * Eine Liste aller Eigenschaften mitsamt der Beschreibung holen
	 * 
	 * @param panelCNr
	 * @param cKey
	 * @return eine (leere) Liste von Eigenschaften mit Beschreibung
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	List<PaneldatenPair> paneldatenFindByPanelCNrCKeyBeschreibung(String panelCNr, String cKey) 
			throws RemoteException, NamingException, EJBExceptionLP ;

	List<PaneldatenPair> panelbeschreibungFindByPanelCNrCKey(String panelCNr, String cKey, Integer artikelgruppeIId);

	PaneldatenDto paneldatenFindByPrimaryKeyOhneExc(Integer paneldatenId);

	void updatePaneldaten(PaneldatenDto paneldatenDto) throws RemoteException, EJBExceptionLP;

	PanelbeschreibungDto panelbeschreibungFindByPrimaryKeyOhneExc(Integer panelbeschreibungId);

	PanelbeschreibungDto[] panelbeschreibungArtikelFindByArtikelgruppeId(Integer artikelgruppeId);

	PaneldatenDto paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(String panelCNr, Integer panelbeschreibungIId,
			String cKey);

	PaneldatenDto paneldatenFindByArtikelIIdPanelbeschreibungIIdOhneExc(Integer artikelIId, Integer panelbeschreibungIId);
	
	CreatePaneldatenResult createPaneldaten(PaneldatenDto paneldatenDto);

	PaneldatenDto setupDefaultArtikelPaneldaten(PanelbeschreibungDto panelbeschreibung, Integer artikelIId);

	PaneldatenPair paneldatenFindByPrimaryKey(Integer paneldatenId) throws RemoteException, EJBExceptionLP;

	PanelbeschreibungDto[] panelbeschreibungChargenFindByArtikelgruppeId(Integer artikelgruppeId);

	/**
	 * 
	 * @param panelCnr "ARTIKELEIGENSCHAFTEN", "CHARGENEIGENSCHAFTEN", ...
	 * @param keyId der KombiKey aus ArtikelId + Seriennummer
	 * @return ein (leeres) Array aus Paneldaten f&uuml;r diesen Kombikey
	 * @throws RemoteException
	 */
	PaneldatenDto[] paneldatenFindByPanelCnrKey(String panelCnr, Integer keyId) throws RemoteException;


	/**
	 * Erzeugt die neuen Paneldaten</br>
	 * <p>&Uuml;berpr&uuml;ft dabei, ob die in der panelDto.beschreibungIId
	 * vorhandene PanelCnr mit der &uuml;bergebenen PanelCnr &uuml;bereinstimmt.
	 * F&uuml;llt im dto dann die panelCnr damit.</p>
	 * 
	 * @param panelCnr "ARTIKELEIGENSCHAFTEN", "CHARGENEIGENSCHAFTEN", ...
	 * @param paneldatenDtos
	 */
	void createPaneldaten(String panelCnr, PaneldatenDto[] paneldatenDtos);

	void updatePaneldaten(String panelCnr, PaneldatenDto[] paneldatenDtos);
}
