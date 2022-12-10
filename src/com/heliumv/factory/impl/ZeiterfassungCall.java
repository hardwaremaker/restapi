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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.api.worktime.DayTypeEntry;
import com.heliumv.api.worktime.DocumentType;
import com.heliumv.api.worktime.MonthlyReportSelectEnum;
import com.heliumv.api.worktime.MonthlyReportSortEnum;
import com.heliumv.api.worktime.SpecialActivity;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IZeiterfassungCall;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.SonderzeitenAntragEmailDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeitdatenpruefenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.personal.service.ZeitsaldoDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Component
public class ZeiterfassungCall extends BaseCall<ZeiterfassungFac> implements IZeiterfassungCall {

	@Autowired
	private IGlobalInfo globalInfo ;
	
	@Autowired
	private IJudgeCall judgeCall ;
	
	private Map<String, Integer> cachedTaetigkeitIds = new HashMap<String, Integer>() ;
	private ZeitdatenDto lastZeitdatenDto = null;
	private boolean recordingEnabled = false;
	
	public ZeiterfassungCall()  {
		super(ZeiterfassungFac.class);
	}
	
	private void recordZeitdaten(ZeitdatenDto dto) {
		if(recordingEnabled) {
			lastZeitdatenDto = dto;
		}
	}
	
	public ZeitdatenDto getRecordedZeitdaten() {
		return lastZeitdatenDto;
	}
	
	public void enableRecordZeitdaten() { 
		recordingEnabled = true;
	}
	
	public void disableRecordZeitdaten() { 
		recordingEnabled = false;
		lastZeitdatenDto = null;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_R)
	public TaetigkeitDto taetigkeitFindByCNr(String cNr) throws NamingException {
		return getFac().taetigkeitFindByCNr(cNr, globalInfo.getTheClientDto()) ;
	}

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_R)	
	public TaetigkeitDto taetigkeitFindByCNrSmall(String cnr) throws NamingException {
	 	return getFac().taetigkeitFindByCNrSmallOhneExc(cnr) ;
	}
	
	private Timestamp updateTimeWithNow(Timestamp theirsTs) {
		if(theirsTs == null) return null ;
		
		Calendar theirs = Calendar.getInstance() ;
		theirs.setTimeInMillis(theirsTs.getTime()) ;
		
		Calendar mine = Calendar.getInstance() ;
		theirs.set(Calendar.HOUR, mine.get(Calendar.HOUR)) ;
		theirs.set(Calendar.MINUTE, mine.get(Calendar.MINUTE)) ;
		theirs.set(Calendar.SECOND, 0) ;
		
		return new Timestamp(theirs.getTimeInMillis()) ;		
	}
	
	private Integer getCachedTaetigkeitId(String taetigkeitCnr) throws NamingException {
		Integer foundId = cachedTaetigkeitIds.get(taetigkeitCnr) ;
		if(foundId == null) {
			TaetigkeitDto tDto = taetigkeitFindByCNrSmall(ZeiterfassungFac.TAETIGKEIT_KOMMT) ;
			if(tDto != null) {
				foundId = tDto.getIId() ;
				cachedTaetigkeitIds.put(taetigkeitCnr, foundId) ;
			}
		}
		return foundId ;
	}
	

	private void modifyKommtGehtToNow(ZeitdatenDto zDto) throws RemoteException, NamingException, EJBExceptionLP {
		Integer taetigkeitId = zDto.getTaetigkeitIId() ;
		if(taetigkeitId == null) return ;
		
		if(taetigkeitId.equals(getCachedTaetigkeitId(ZeiterfassungFac.TAETIGKEIT_KOMMT)) ||
		   taetigkeitId.equals(getCachedTaetigkeitId(ZeiterfassungFac.TAETIGKEIT_GEHT))  ||
		   taetigkeitId.equals(getCachedTaetigkeitId(ZeiterfassungFac.TAETIGKEIT_UNTER))) {

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			c.set(Calendar.MILLISECOND, 0);
			c.set(Calendar.SECOND, 0);
			zDto.setTZeit(new Timestamp(c.getTimeInMillis())) ;
		}
	}
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	public Integer createZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen)
			throws EJBExceptionLP, NamingException, RemoteException {
		if(judgeCall.hasPersZeiteingabeNurBuchen()) {
			zeitdatenDto.setTZeit(updateTimeWithNow(zeitdatenDto.getTZeit())) ;
		}
		if(!judgeCall.hasPersDarfKommtGehtAendern()) {
			modifyKommtGehtToNow(zeitdatenDto) ;
		}
		
		recordZeitdaten(zeitdatenDto);
		return getFac().createZeitdaten(zeitdatenDto, bBucheAutoPausen,
				bBucheMitternachtssprung, bZeitverteilen, false, globalInfo.getTheClientDto()) ;
	}

	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG) 
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	public Integer createAuftragZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen)
			throws EJBExceptionLP, NamingException, RemoteException {
		zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG) ;
		return createZeitdaten(zeitdatenDto, bBucheAutoPausen, bBucheMitternachtssprung, bZeitverteilen) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_PROJEKT) 
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	public Integer createProjektZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen)
			throws EJBExceptionLP, NamingException, RemoteException {
		zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_PROJEKT) ;
		return createZeitdaten(zeitdatenDto, bBucheAutoPausen, bBucheMitternachtssprung, bZeitverteilen) ;
	}
	
	public List<SpecialActivity> getAllSprSondertaetigkeitenNurBDEBuchbar(String language) throws NamingException, RemoteException {
		Map<?, ?> m =  getFac().getAllSprSondertaetigkeitenNurBDEBuchbar(language) ;
		return convertFromActivities(m) ;
	}
	
	public List<SpecialActivity> getAllSprSondertaetigkeiten(String language) throws NamingException, RemoteException {
		Map<?, ?> m =  getFac().getAllSprSondertaetigkeiten(language) ;
		return convertFromActivities(m) ;
	}
	
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	public List<DocumentType> getBebuchbareBelegarten(TheClientDto theClientDto) throws NamingException {
		Map<String, String> m = getFac().getBebuchbareBelegarten(theClientDto) ;
		return convertFromBelegarten(m) ;
	}

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	public List<DocumentType> getBebuchbareBelegarten() throws NamingException {
		return getBebuchbareBelegarten(globalInfo.getTheClientDto()) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_R)
	public ZeitdatenDto zeitdatenFindByPrimaryKey(Integer id) throws NamingException, RemoteException {
//		return getFac().zeitdatenFindByPrimaryKeyOhneExc(id) ;
		return getFac().zeitdatenFindByPrimaryKey(id, globalInfo.getTheClientDto()) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	public void removeZeitdaten(ZeitdatenDto zeitdatenDto) throws NamingException, RemoteException, EJBExceptionLP {
		getFac().removeZeitdaten(zeitdatenDto, globalInfo.getTheClientDto()) ;
	}
	
	
	public List<DayTypeEntry> getAllSprTagesarten() throws NamingException, RemoteException {
		return getAllSprTagesarten(globalInfo.getTheClientDto().getLocUi()) ;
	}
	
	public List<DayTypeEntry> getAllSprTagesarten(Locale locale) throws NamingException, RemoteException {
		return convertFromDayTypes(getFac().getAllSprTagesarten(Helper.locale2String(locale))) ;		
	}

	
	private List<DayTypeEntry> convertFromDayTypes(Map<?, ?> allDayTypes) {
		List<DayTypeEntry> daytypes = new ArrayList<DayTypeEntry>() ;
		for (Entry<?,?> entry : allDayTypes.entrySet()) {
			daytypes.add(new DayTypeEntry((Integer) entry.getKey(), (String) entry.getValue())) ;
		}
		return daytypes ;
 	}
	
	private List<SpecialActivity> convertFromActivities(Map<?, ?> allActivities) {
		List<SpecialActivity> activities = new ArrayList<SpecialActivity>() ;
		for (Entry<?, ?> entry : allActivities.entrySet()) {
			activities.add(new SpecialActivity((Integer)entry.getKey(), (String) entry.getValue())) ;
		}
		
		return activities ;
	}

	private List<DocumentType> convertFromBelegarten(Map<String, String> allBelegarten) {
		List<DocumentType> documents = new ArrayList<DocumentType>() ;
		for (Entry<String, String> entry : allBelegarten.entrySet()) {
			documents.add(new DocumentType(entry.getKey(), entry.getValue())) ;
		}
		
		return documents ;
	}

	public MaschineDto maschineFindByPrimaryKey(Integer maschineId) throws NamingException, RemoteException {
		MaschineDto maschineDto = getFac().maschineFindByPrimaryKey(maschineId) ;
		if(!globalInfo.getMandant().equals(maschineDto.getMandantCNr())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, maschineId.toString());
		}
		
		return maschineDto ;
	}
	
	public void stopMaschine(Integer maschineIId, Integer lossollarbeitsplanIId, 
			Timestamp tStop) throws NamingException, RemoteException {
		getFac().maschineStop(maschineIId, lossollarbeitsplanIId, tStop, globalInfo.getTheClientDto());
	}


	@Override
	public Integer createMaschinenzeitdaten(MaschinenzeitdatenDto maschinenzeitdatenDto) throws NamingException, RemoteException {
		return getFac().createMaschinenzeitdaten(maschinenzeitdatenDto, globalInfo.getTheClientDto());
	}

	public ZeitsaldoDto erstelleMonatsabrechnungZeitsaldo(Integer personalIId, Integer iJahr, Integer iMonat, 
			Date dBis) throws EJBExceptionLP, RemoteException {
		ZeitsaldoDto zeitsaldoDto = getFac().erstelleMonatsabrechnungZeitsaldo(personalIId, iJahr, iMonat, false, dBis, 
				globalInfo.getTheClientDto(), true, ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER);
		return zeitsaldoDto;
	}
	
	@Override
	public Integer createZeitdatenpruefen(ZeitdatenpruefenDto dto) {
		return getFac().createZeitdatenpruefen(dto, globalInfo.getTheClientDto());
	}
	
	@Override
	public Integer createZeitdatenpruefen(ZeitdatenDto dto, Integer fehlerCode, String fehlerText) {
		return getFac().createZeitdatenpruefen(dto, fehlerCode, fehlerText, globalInfo.getTheClientDto());
	}
	
	@Override
	public String istBelegGeradeInBearbeitung(String belegartCnr, Integer belegId) {
		return getFac().istBelegGeradeInBearbeitung(belegartCnr, belegId, globalInfo.getTheClientDto());
	}
	
	@Override
	public MaschineDto maschineFindByCIdentifikationsnrOhneExc(String identifikationsnr) {
		return getFac().maschineFindByCIdentifikationsnrOhneExc(identifikationsnr);
	}
	
	@Override
	public MaschineDto maschineFindByPrimaryKeyOhneExc(Integer maschineId) {
		MaschineDto maschineDto = getFac().maschineFindByPrimaryKeyOhneExc(maschineId) ;
		if(maschineDto == null 
				|| !globalInfo.getMandant().equals(maschineDto.getMandantCNr())) {
			return null;
		}
		
		return maschineDto ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	public Integer createSonderzeitenVonBis(SonderzeitenDto sonderzeitenDto, Timestamp von, Timestamp bis) throws RemoteException {
//		Timestamp[] buchungenVorhanden = getFac()
//				.sindIstZeitenVorhandenWennUrlaubGebuchtWird(
//						sonderzeitenDto, von, bis, globalInfo.getTheClientDto());
		Timestamp[] buchungenVorhanden = new Timestamp[] {};
		return getFac().createSonderzeitenVonBis(
				sonderzeitenDto, von, bis, buchungenVorhanden, true, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	public void createSonderzeitenEmail(SonderzeitenAntragEmailDto emailDto) throws RemoteException {
		getFac().createSonderzeitenEmail(emailDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	public DiaetenDto[] diaetenByLandId(Integer landId) throws RemoteException {
		return getFac().diaetenFindByLandIId(landId);
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)	
	public void createReise(ReiseDto reiseDto) throws RemoteException {
		getFac().createReise(reiseDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)	
	public ReiseDto reiseFindByPersonalIIdTZeitOhneExc(Integer personalId, Timestamp timestamp) {
		return getFac().reiseFindByPersonalIIdTZeitOhneExc(personalId, timestamp);
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN)
	public JasperPrintLP printMonatsabrechnung(Integer personalId, int year, int month, 
			MonthlyReportSelectEnum selectOption, MonthlyReportSortEnum sortOption,
			boolean toEndOfMonth, Date toDate, Double onlyIfHoursBiggerThan, boolean withHiddenPersonal) {
		return getFac().printMonatsAbrechnung(personalId, year, month,
				toEndOfMonth, toDate, globalInfo.getTheClientDto(), new Integer(selectOption.hvConst()), null, 
				new Integer(sortOption.hvConst()), onlyIfHoursBiggerThan, withHiddenPersonal);
	}
}
