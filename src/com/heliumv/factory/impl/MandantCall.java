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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IMandantCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.util.EJBExceptionLP;

public class MandantCall extends BaseCall<MandantFac> implements IMandantCall {
	
	@Autowired
	private IGlobalInfo globalInfo ;
	
	private HashMap<String, String> moduls ;
	private HashMap<String, String> functions ;
	
	public MandantCall() {
		super(MandantFac.class);
	}

	public ModulberechtigungDto[] modulberechtigungFindByMandantCnr(String mandantCnr) {
		try {
			return getFac().modulberechtigungFindByMandantCNr(mandantCnr) ;
		} catch(RemoteException e) {			
		}
		
		return new ModulberechtigungDto[0] ;
	}

	public ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungFindByMandantCnr(String mandantCnr) {
		try {
			return getFac().zusatzfunktionberechtigungFindByMandantCNr(mandantCnr) ;
		} catch(RemoteException e) {			
		}
		
		return new ZusatzfunktionberechtigungDto[0] ;
	}
	
	public void setModulBerechtigung(ModulberechtigungDto[] modulBerechtigungen) {
		moduls = new HashMap<String, String>() ;
		if(null == modulBerechtigungen) return ;
		
		for (int i = 0; i < modulBerechtigungen.length; i++) {
			moduls.put(modulBerechtigungen[i].getBelegartCNr().trim(),
					modulBerechtigungen[i].getBelegartCNr().trim()) ;
		}
	}

	public void setZusatzFunktionen(ZusatzfunktionberechtigungDto[] zusatzFunktionen) {		
		functions = new HashMap<String, String>() ;
		if(null == zusatzFunktionen) return ;
		
		for (int i = 0; i < zusatzFunktionen.length; i++) {
			functions.put(zusatzFunktionen[i].getZusatzfunktionCNr().trim(),
					zusatzFunktionen[i].getZusatzfunktionCNr().trim()) ;
		}
	}

	public boolean hasNamedModul(String moduleName) {
		return hasModul(moduleName, globalInfo.getTheClientDto().getMandant()) ;		
	}
	
	public boolean hasModulAngebot(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_ANGEBOT, mandantCnr) ;
	}

	@Override
	public boolean hasModulAngebot() {
		return hasModulAngebot(globalInfo.getTheClientDto().getMandant());
	}	
	
	@Override
	public boolean hasModulArtikel() {
		return hasModulArtikel(globalInfo.getTheClientDto().getMandant());
	}

	@Override
	public boolean hasModulArtikel(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_ARTIKEL, mandantCnr) ;
	}

	@Override
	public boolean hasModulAuftrag(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_AUFTRAG, mandantCnr) ;
	}

	public boolean hasModulAuftrag() {
		return hasModulAuftrag(globalInfo.getTheClientDto().getMandant()) ;
	}
	
	public boolean hasModulProjekt(String mandantCnr)  {
		return hasModul(LocaleFac.BELEGART_PROJEKT, mandantCnr) ;
	}

	@Override
	public boolean hasModulProjekt() {
		return hasModulProjekt(globalInfo.getTheClientDto().getMandant()) ;
	}

	@Override
	public boolean hasModulLieferschein(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_LIEFERSCHEIN, mandantCnr) ;
	}	
	
	@Override
	public boolean hasModulLieferschein() {
		return hasModulLieferschein(globalInfo.getTheClientDto().getMandant());
	}
	
	public boolean hasModulLos(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_LOS, mandantCnr) ;
	}	
	
	@Override
	public boolean hasModulLos()  {
		return hasModulLos(globalInfo.getTheClientDto().getMandant());
	}
	
	public boolean hasFunctionProjektZeiterfassung(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG, mandantCnr) ;
	}

	@Override
	public boolean hasFunctionProjektZeiterfassung() {
		return hasFunctionProjektZeiterfassung(globalInfo.getTheClientDto().getMandant());
	}
	
	public boolean hasFunctionAngebotsZeiterfassung(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG, mandantCnr) ;		
	}

	@Override
	public boolean hasFunctionAngebotsZeiterfassung() {
		return hasFunctionAngebotsZeiterfassung(globalInfo.getTheClientDto().getMandant());
	}
	
	public boolean hasFunctionZentralerArtikelstamm(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, mandantCnr) ;
	}

	public boolean hasFunctionZentralerArtikelstamm()  {
		return hasFunction(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, globalInfo.getTheClientDto().getMandant()) ;
	}
	
	public Locale getLocaleDesHauptmandanten() throws EJBExceptionLP {
		return getFac().getLocaleDesHauptmandanten() ;
	}
	
	public String getMandantEmailAddress() throws RemoteException, EJBExceptionLP {
		MandantDto mandantDto = mandantFindByPrimaryKey() ;
		if(mandantDto.getPartnerDto() != null && 
			!StringHelper.isEmpty(mandantDto.getPartnerDto().getCEmail())) {
			return mandantDto.getPartnerDto().getCEmail() ;
		}			
	
		return null ;
	}
	
	public MandantDto mandantFindByPrimaryKey(String mandantCnr) throws RemoteException, EJBExceptionLP {
		return getFac().mandantFindByPrimaryKey(mandantCnr, globalInfo.getTheClientDto()) ;
 	}
	
	public MandantDto mandantFindByPrimaryKey() throws RemoteException, EJBExceptionLP {
		return getFac().mandantFindByPrimaryKey(globalInfo.getMandant(), globalInfo.getTheClientDto()) ;
	}
	
	private boolean hasModul(String whichModul, String mandantCnr) {
		if(moduls == null) {
			setModulBerechtigung(modulberechtigungFindByMandantCnr(mandantCnr)) ;
		}

		return moduls.containsKey(whichModul.trim()) ;
	}
	
	private boolean hasFunction(String whichFunction, String mandantCnr) {
		if(functions == null) {
			setZusatzFunktionen(zusatzfunktionberechtigungFindByMandantCnr(mandantCnr)) ;
		}
		
		return functions.containsKey(whichFunction.trim()) ;
	}
	
	public boolean hasFunctionKostentraeger(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_KOSTENTRAEGER, mandantCnr) ;
	}

	public boolean hasFunctionKostentraeger() {
		return hasFunction(MandantFac.ZUSATZFUNKTION_KOSTENTRAEGER, globalInfo.getTheClientDto().getMandant()) ;
	}
	
	public boolean hasFunctionHvmaZeiterfassung(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_HVMA_ZEITERFASSUNG, mandantCnr) ;
	}

	public boolean hasFunctionHvmaZeiterfassung() {
		return hasFunction(MandantFac.ZUSATZFUNKTION_HVMA_ZEITERFASSUNG, globalInfo.getTheClientDto().getMandant()) ;
	}

	public MwstsatzDto mwstSatzDtoFindByPrimaryKey(Integer mwstsatzId) throws RemoteException {
		return getFac().mwstsatzFindByPrimaryKey(mwstsatzId, globalInfo.getTheClientDto()) ;
	}
	
	public MwstsatzbezDto mwstsatzbezDtoFindByPrimaryKey(Integer mwstsatzBezId) throws RemoteException {
		return getFac().mwstsatzbezFindByPrimaryKey(mwstsatzBezId, globalInfo.getTheClientDto()) ;
	}

	/**
	 * Liefert den zum Datum (Belegdatum) gueltigen Mwstsatz einer MwstsatzBezeichnung
	 * 
	 * @param mwstsatzbezId die MwstsatzBezeichnung zu der der Mwstsatz ermittelt werden soll
	 * @param datum das Datum zu dem der Mwstsatz ermittelt werden soll 
	 * @return null wenn es keinen gibt, oder den Mwstsatz
	 * @throws RemoteException
	 */
	public MwstsatzDto mwstsatzDtoZuDatum(Integer mwstsatzbezId, Timestamp datum) throws RemoteException {
		return getFac().mwstsatzFindZuDatum(mwstsatzbezId, datum) ;
	}

	@Override
	public boolean hasFunctionPruefplan() {
		return hasFunctionPruefplan(globalInfo.getTheClientDto().getMandant());
	}

	@Override
	public boolean hasFunctionPruefplan(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1, mandantCnr);
	}

	@Override
	public boolean hasModulZeiterfassung() {
		return hasModulZeiterfassung(globalInfo.getTheClientDto().getMandant());
	}

	@Override
	public boolean hasModulZeiterfassung(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_ZEITERFASSUNG, mandantCnr);
	}

	@Override
	public boolean hasModulForecast() {
		return hasModulForecast(globalInfo.getTheClientDto().getMandant());
	}

	@Override
	public boolean hasModulForecast(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_FORECAST, mandantCnr);
	}
	

	@Override
	public boolean hasModulBestellung() {
		return hasModulBestellung(globalInfo.getTheClientDto().getMandant());
	}
	
	@Override
	public boolean hasModulBestellung(String mandantCnr) {
		return hasModul(LocaleFac.BELEGART_BESTELLUNG, mandantCnr) ;
	}
	
	public boolean hasFunctionHvma2(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_HVMA2, mandantCnr) ;
	}
 
	@Override
	public boolean hasFunctionHvma2() {
		return hasFunction(MandantFac.ZUSATZFUNKTION_HVMA2, globalInfo.getTheClientDto().getMandant()) ;
	}
	
	@Override
	public boolean hasFunctionReisezeiten() {
		return hasFunction(MandantFac.ZUSATZFUNKTION_REISEZEITEN,
				globalInfo.getTheClientDto().getMandant()) ;
	}
	
	@Override
	public boolean hasFunctionReisezeiten(String mandantCnr) {
		return hasFunction(MandantFac.ZUSATZFUNKTION_REISEZEITEN, mandantCnr) ;
	}
	
	@Override
	public ZahlungszielDto zahlungszielFindByCnr(String cnr) {
		return getFac().zahlungszielFindByCBezMandantNull(cnr, globalInfo.getMandant(), globalInfo.getTheClientDto());
	}

}
