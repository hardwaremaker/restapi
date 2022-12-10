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
import com.heliumv.factory.IJudgeCall;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheJudgeFac;
import com.lp.util.EJBExceptionLP;

public class JudgeCall extends BaseCall<TheJudgeFac> implements IJudgeCall {
	@Autowired 
	private IGlobalInfo globalInfo ;
	
	public JudgeCall() {
		super(TheJudgeFac.class) ;
	}

	protected boolean hatRechtImpl(String rechtCnr, TheClientDto theClientDto) {
		return getFac().hatRecht(rechtCnr, theClientDto) ;
	}
	
	public boolean hatRecht(String rechtCnr) {
		return hatRechtImpl(rechtCnr, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public boolean hasPersZeiteingabeNurBuchen()  {
		return hatRechtImpl(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public boolean hasPersZeiteingabeNurBuchen(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN, theClientDto) ;
	}

	@Override
	public boolean hasFertDarfLosErledigen() {
		return hatRechtImpl(RechteFac.RECHT_FERT_DARF_LOS_ERLEDIGEN, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public boolean hasFertDarfLosErledigen(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_FERT_DARF_LOS_ERLEDIGEN, theClientDto) ;
	}
	
	public boolean hasFertLosCUD() {
		return hatRechtImpl(RechteFac.RECHT_FERT_LOS_CUD, globalInfo.getTheClientDto()) ;
	}
	
	
	@Override
	public boolean hasFertLosCUD(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_FERT_LOS_CUD, theClientDto) ;
	}

	@Override
	public boolean hasFertDarfSollmaterialCUD() {
		return hatRechtImpl(RechteFac.RECHT_FERT_DARF_SOLLMATERIAL_CUD, globalInfo.getTheClientDto()) ;
	}

	@Override
	public boolean hasFertDarfSollmaterialCUD(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_FERT_DARF_SOLLMATERIAL_CUD, theClientDto) ;
	}

	@Override
	public boolean hasFertDarfIstmaterialManuellNachbuchen() {
		return hatRechtImpl(RechteFac.RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN, globalInfo.getTheClientDto()) ;
	}

	@Override
	public boolean hasFertDarfIstmaterialManuellNachbuchen(
			TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN, theClientDto) ;
	}

	@Override
	public boolean hasPersSichtbarkeitAbteilung() {
		return hatRechtImpl(RechteFac.RECHT_PERS_SICHTBARKEIT_ABTEILUNG, globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasPersSichtbarkeitAbteilung(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_PERS_SICHTBARKEIT_ABTEILUNG, theClientDto);		
	}
	
	@Override
	public boolean hasPersSichtbarkeitAlle() {
		return hatRechtImpl(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE, globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasPersSichtbarkeitAlle(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE, theClientDto);
	}

	@Override
	public boolean hasPersDarfKommtGehtAendern() {
		return hatRechtImpl(RechteFac.RECHT_PERS_DARF_KOMMT_GEHT_AENDERN, globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasPersDarfKommtGehtAendern(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_PERS_DARF_KOMMT_GEHT_AENDERN, theClientDto) ;
	}	
	
	/**
	 * Versucht den Datensatz mit der angegebenen Id in der Tabelle zu sperren.</br>
	 * <p>Wirft Exception wenn bereits ein Lock vorhanden ist</p>
	 * 
	 * @param lockedTable HelperClient.LOCKME_***
	 * @param id
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	public void addLock(String lockedTable, Integer id) throws RemoteException, EJBExceptionLP {
		LockMeDto lockMeDto = buildLockMeDto(lockedTable, id) ;
		getFac().addLockedObject(lockMeDto, globalInfo.getTheClientDto());
	}
	
	public void removeLock(String lockedTable, Integer id) throws RemoteException, EJBExceptionLP {
		LockMeDto lockmeDto = buildLockMeDto(lockedTable, id) ;
		getFac().removeLockedObject(lockmeDto);
		
	}
	
	private LockMeDto buildLockMeDto(String lockedTable, Integer id) {
		LockMeDto lockmeDto = new LockMeDto(
				lockedTable, id.toString(), globalInfo.getTheClientDto().getIDUser()) ;
		lockmeDto.setPersonalIIdLocker(globalInfo.getTheClientDto().getIDPersonal()) ;
		return lockmeDto ;
	}
	
	@Override
	public boolean hasRechnungCRUD() {
		return hasRechnungCRUD(globalInfo.getTheClientDto()) ;
	}	
		
	public boolean hasRechnungCRUD(TheClientDto theClientDto) {
		return hasRechnungCUD(theClientDto) || hasRechnungR(theClientDto); 		
	}
	
	public boolean hasRechnungCUD() {
		return hasRechnungCUD(globalInfo.getTheClientDto()) ;
	}

	public boolean hasRechnungR() {
		return hasRechnungR(globalInfo.getTheClientDto()) ;
	}
	
	public boolean hasRechnungCUD(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_RECH_RECHNUNG_CUD, theClientDto);
	}

	public boolean hasRechnungR(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_RECH_RECHNUNG_R, theClientDto);
	}

	@Override
	public boolean hasFertDarfLosAbliefern() {
		return hasFertDarfLosAbliefern(globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasFertDarfLosAbliefern(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_FERT_LOS_DARF_ABLIEFERN, theClientDto);
	}

	@Override
	public boolean hasPersZeiterfassungDarfMonatsabrechnungDrucken() {
		return hasPersZeiterfassungDarfMonatsabrechnungDrucken(globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasPersZeiterfassungDarfMonatsabrechnungDrucken(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN, theClientDto);
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe0CU() {
		return hasDokumenteSicherheitsstufe0CU(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe0CU(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU, theClientDto);
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe1CU() {
		return hasDokumenteSicherheitsstufe1CU(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe1CU(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU, theClientDto);
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe2CU() {
		return hasDokumenteSicherheitsstufe2CU(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe2CU(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU, theClientDto);
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe3CU() {
		return hasDokumenteSicherheitsstufe3CU(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe3CU(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU, theClientDto);
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe99CU() {
		return hasDokumenteSicherheitsstufe99CU(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasDokumenteSicherheitsstufe99CU(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_99_CU, theClientDto);
	}

	@Override
	public boolean hasLieferscheinR() {
		return hasLieferscheinR(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasLieferscheinR(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_LS_LIEFERSCHEIN_R, theClientDto);
	}
	
	@Override
	public boolean hasLieferscheinCUD() {
		return hasLieferscheinCUD(globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasLieferscheinCUD(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD, theClientDto);
	}
	
	@Override
	public boolean hasLieferscheinCRUD() {
		return hasLieferscheinCRUD(globalInfo.getTheClientDto());
	}
	@Override
	public boolean hasLieferscheinCRUD(TheClientDto theClientDto) {
		return hasLieferscheinCUD(theClientDto) || hasLieferscheinR(theClientDto);
	}

	@Override
	public boolean hasBestellungR() {
		return hasBestellungR(globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasBestellungR(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_BES_BESTELLUNG_R, theClientDto);
	}

	@Override
	public boolean hasBestellungCUD() {
		return hasBestellungCUD(globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasBestellungCUD(TheClientDto theClientDto) {
		return hatRechtImpl(RechteFac.RECHT_BES_BESTELLUNG_CUD, theClientDto);
	}

	@Override
	public boolean hasBestellungCRUD() {
		return hasBestellungCRUD(globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasBestellungCRUD(TheClientDto theClientDto) {
		return hasBestellungCUD(theClientDto) || hasBestellungR(theClientDto);
	}
	
	@Override
	public boolean hasAuftragCUD() {
		return hatRechtImpl(RechteFac.RECHT_AUFT_AUFTRAG_CUD, globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasAuftragR() {
		return hatRechtImpl(RechteFac.RECHT_AUFT_AUFTRAG_R, globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasAuftragCRUD() {
		return hasAuftragCUD() || hasAuftragR();
	}
	
	@Override
	public boolean hasProjektCUD() {
		return hatRechtImpl(RechteFac.RECHT_PROJ_PROJEKT_CUD, globalInfo.getTheClientDto());
	}

	@Override
	public boolean hasProjektR() {
		return hatRechtImpl(RechteFac.RECHT_PROJ_PROJEKT_R, globalInfo.getTheClientDto());
	}
	
	@Override
	public boolean hasProjektCRUD() {
		return hasProjektCUD() || hasProjektR();
	}
}
