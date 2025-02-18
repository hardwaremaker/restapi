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

import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.KundenStuecklistepositionDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.util.EJBExceptionLP;

public interface IStuecklisteCall {
	StuecklisteDto stuecklisteFindByPrimaryKey(Integer stuecklisteId) throws RemoteException, NamingException ;
	StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId( 
			Integer stuecklisteIId) throws RemoteException, NamingException ;
	List<KundenStuecklistepositionDto> stuecklistepositionFindByStuecklisteIIdAllData(
			Integer stuecklisteIId) throws RemoteException, NamingException ;
	List<KundenStuecklistepositionDto> stuecklistepositionFindByStuecklisteIIdAllData(
			Integer stuecklisteIId, boolean withPrice) throws RemoteException, NamingException ;

	StuecklistepositionDto stuecklistepositionFindByPrimaryKey(
			Integer iId) throws RemoteException, NamingException, EJBExceptionLP ; 
	Integer createStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto) throws EJBExceptionLP, RemoteException, NamingException ;
	void updateStuecklisteposition(StuecklistepositionDto originalDto, StuecklistepositionDto aenderungDto)  throws EJBExceptionLP, RemoteException, NamingException ;
	void removeStuecklisteposition(StuecklistepositionDto originalDto, 
			StuecklistepositionDto removeDto) throws EJBExceptionLP, RemoteException, NamingException ;

	MontageartDto[] montageartFindByMandantCNr() throws RemoteException, NamingException, EJBExceptionLP ;
	
	StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(Integer stuecklisteIId) throws RemoteException;
	
	ApkommentarDto apkommentarFindByPrimaryKey(Integer iId);
	
	PruefartDto pruefartFindByPrimaryKey(Integer pruefartIId);
	
	PruefkombinationDto pruefkombinationFindByPrimaryKey(Integer pruefkombinationId);
	
	PruefkombinationDto pruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId(
			Integer pruefartIId, Integer artikelIIdKontakt,
			Integer artikelIIdLitze, Integer verschleissteilIId);
	
	Integer pruefeObPruefplanInPruefkombinationVorhanden(
			Integer pruefartIId, Integer artikelIIdKontakt,
			Integer artikelIIdLitze, Integer artikelIIdLitze2,
			Integer verschleissteilIId, Integer pruefkombinationIId);
	
	FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBezOhneExc(String cBez);
	
	List<Integer> getMoeglicheMaschinen(Integer lossollarbeitsplanIId);
	
	/**
	 * Liefert die Liste der erlaubten Fertigungsgruppen
	 * 
	 * @return die Liste der erlaubten Fertigungsgruppen. Ist die Liste leer, bedeutet
	 * dies, dass es keine Einschr&auml;nkungen gibt.
	 */
	List<Integer> getEingeschraenkteFertigungsgruppen();
}
