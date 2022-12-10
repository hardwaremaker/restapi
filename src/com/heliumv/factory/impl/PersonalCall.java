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
package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IPersonalCall;
import com.lp.server.personal.service.AnwesenheitsbestaetigungDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.util.EJBExceptionLP;

public class PersonalCall extends BaseCall<PersonalFac> implements IPersonalCall {
	@Autowired 
	private IGlobalInfo globalInfo ;
	
	public PersonalCall() {
		super(PersonalFac.class);
	}
	
	public PersonalDto byPrimaryKeySmall(Integer personalIId) throws NamingException {
		return getFac().personalFindByPrimaryKeySmallOhneExc(personalIId) ;
	}	

	@Override
	public PersonalDto byPrimaryKey(Integer personalId) {
		return getFac().personalFindByPrimaryKey(personalId, globalInfo.getTheClientDto());
	}
	
	public PersonalDto byCPersonalnrMandantCNrOhneExc(
			String cPersonalnr) throws RemoteException, NamingException {
		return getFac().personalFindByCPersonalnrMandantCNr(cPersonalnr, globalInfo.getMandant()) ;
	}
	
	public BetriebskalenderDto[] getFeiertagsKalender()  throws RemoteException, NamingException {
		return getFac().betriebskalenderFindByMandantCNrTagesartCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, globalInfo.getTheClientDto()) ;
	}

	public BetriebskalenderDto[] getBetriebsurlaubKalender()  throws RemoteException, NamingException {
		return getFac().betriebskalenderFindByMandantCNrTagesartCNr(
				ZeiterfassungFac.TAGESART_BETRIEBSURLAUB, globalInfo.getTheClientDto()) ;
	}
	
	public Integer getArtikelIIdHoechsterWertPersonalverfuegbarkeit(Integer personalId) throws RemoteException {
		return getFac().getArtikelIIdHoechsterWertPersonalverfuegbarkeit(personalId);
	}

	@Override
	public PersonalDto byCAusweis(String cAusweis) throws EJBExceptionLP, RemoteException {
		return getFac().personalFindByCAusweis(cAusweis);
	}
	
	@Override
	public FahrzeugDto fahrzeugByPrimaryKey(Integer fahrzeugId) {
		return getFac().fahrzeugFindByPrimaryKey(fahrzeugId);		
	}
	
	@Override
	public FahrzeugDto fahrzeugByPrimaryKeyOhneExc(Integer fahrzeugId) {
		return getFac().fahrzeugFindByPrimaryKeyOhneExc(fahrzeugId);
	}
	
	@Override
	public Integer createAnwesenheitsbestaetigung(AnwesenheitsbestaetigungDto bestaetigungDto) {
		return getFac().createAnwesenheitsbestaetigung(bestaetigungDto, globalInfo.getTheClientDto());
	}
	
	@Override
	public void createAnwesenheitsbestaetigungEmail(Integer anwesenheitsbestaetigungId) throws RemoteException {
		getFac().createAnwesenheitsbestaetigungEmail(anwesenheitsbestaetigungId, globalInfo.getTheClientDto());
	}
}
