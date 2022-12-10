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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.api.worktime.DayTypeEntry;
import com.heliumv.api.worktime.DocumentType;
import com.heliumv.api.worktime.MonthlyReportSelectEnum;
import com.heliumv.api.worktime.MonthlyReportSortEnum;
import com.heliumv.api.worktime.SpecialActivity;
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
import com.lp.server.personal.service.ZeitsaldoDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public interface IZeiterfassungCall {
	ZeitdatenDto getRecordedZeitdaten();
	
	void enableRecordZeitdaten();	
	void disableRecordZeitdaten();

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_R)	
	TaetigkeitDto taetigkeitFindByCNr(String cnr)  throws NamingException;
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_R)	
	TaetigkeitDto taetigkeitFindByCNrSmall(String cnr) throws NamingException ;	
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	Integer createZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen)
			throws EJBExceptionLP, NamingException, RemoteException ;

	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	Integer createAuftragZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen)
			throws EJBExceptionLP, NamingException, RemoteException ;
	
	@HvModul(modul=LocaleFac.BELEGART_PROJEKT)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	Integer createProjektZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen)
			throws EJBExceptionLP, NamingException, RemoteException;
	
	List<SpecialActivity> getAllSprSondertaetigkeitenNurBDEBuchbar(String language) throws NamingException, RemoteException ; 
	
	List<SpecialActivity> getAllSprSondertaetigkeiten(String language) throws NamingException, RemoteException ;
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	List<DocumentType> getBebuchbareBelegarten(TheClientDto theClientDto) throws NamingException ;
	List<DocumentType> getBebuchbareBelegarten() throws NamingException ;

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD) 
	void removeZeitdaten(ZeitdatenDto zeitdatenDto) throws NamingException, RemoteException, EJBExceptionLP ;
	
	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	@HvJudge(recht=RechteFac.RECHT_PERS_ZEITERFASSUNG_R)	
	ZeitdatenDto zeitdatenFindByPrimaryKey(Integer id) throws NamingException, RemoteException ;
	
	List<DayTypeEntry> getAllSprTagesarten() throws NamingException, RemoteException ;
	List<DayTypeEntry> getAllSprTagesarten(Locale locale) throws NamingException, RemoteException ;
	
	MaschineDto maschineFindByPrimaryKey(Integer maschineId) throws NamingException, RemoteException ;
	
	void stopMaschine(Integer maschineIId, Integer lossollarbeitsplanIId, 
			Timestamp tStop) throws NamingException, RemoteException;

	Integer createMaschinenzeitdaten(MaschinenzeitdatenDto mDto) throws NamingException, RemoteException;
	
	ZeitsaldoDto erstelleMonatsabrechnungZeitsaldo(Integer personalIId, Integer iJahr, Integer iMonat, 
			Date dBis) throws EJBExceptionLP, RemoteException;

	Integer createZeitdatenpruefen(ZeitdatenpruefenDto dto);
	Integer createZeitdatenpruefen(ZeitdatenDto dto, Integer fehlerCode, String fehlerText);

	String istBelegGeradeInBearbeitung(String belegartCnr, Integer belegId);
	
	MaschineDto maschineFindByCIdentifikationsnrOhneExc(String identifikationsnr);

	MaschineDto maschineFindByPrimaryKeyOhneExc(Integer maschineId);

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	Integer createSonderzeitenVonBis(SonderzeitenDto sonderzeitenDto, Timestamp von, Timestamp bis) throws RemoteException;

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	void createSonderzeitenEmail(SonderzeitenAntragEmailDto emailDto) throws RemoteException;

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	DiaetenDto[] diaetenByLandId(Integer landId) throws RemoteException;

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	void createReise(ReiseDto reiseDto) throws RemoteException;

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	ReiseDto reiseFindByPersonalIIdTZeitOhneExc(Integer personalId, Timestamp timestamp);

	@HvModul(modul=LocaleFac.BELEGART_ZEITERFASSUNG)
	JasperPrintLP printMonatsabrechnung(Integer personalId, int year, int month, MonthlyReportSelectEnum selectOption,
			MonthlyReportSortEnum sortOption, boolean toEndOfMonth, Date toDate, Double onlyIfHoursBiggerThan,
			boolean withHiddenPersonal);
}
