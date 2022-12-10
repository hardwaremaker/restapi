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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IFertigungCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IParameterCall.AuslastungsAnzeigeDetailAg;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungResultDto;
import com.lp.server.fertigung.service.LosablieferungTerminalDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.EJBExceptionLP;

public class FertigungCall extends BaseCall<FertigungFac> implements IFertigungCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	@Autowired
	private IParameterCall parameterCall ;
	
	public FertigungCall() {
//		super(FertigungFacBean) ;
		super(FertigungFac.class);
	}

	
	@Override
	public void bucheMaterialAufLos(LosDto losDto, BigDecimal menge)
			throws RemoteException {
		bucheMaterialAufLos(losDto, menge, false, false, true, null, false) ;		
	}


	@Override
	public void bucheMaterialAufLos(LosDto losDto, BigDecimal menge,
			boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen,
			boolean bUnterstuecklistenAbbuchen, 
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) throws RemoteException {
		getFac().bucheMaterialAufLos(losDto, menge, bHandausgabe, 
				bNurFehlmengenAnlegenUndReservierungenLoeschen, bUnterstuecklistenAbbuchen, 
				globalInfo.getTheClientDto(), bucheSerienChnrAufLosDtos, throwExceptionWhenCreate) ;
	}
	
	
	@Override
	public LosablieferungDto createLosablieferung(
			LosablieferungDto losablieferungDto, boolean bErledigt) throws RemoteException, EJBExceptionLP {
		LosablieferungResultDto laResultDto = getFac().createLosablieferung(losablieferungDto, globalInfo.getTheClientDto(), bErledigt) ;
		return laResultDto.getLosablieferungDto();
	}


	@Override
	@HvModul(modul=LocaleFac.BELEGART_LOS)
	@HvJudge(rechtOder={RechteFac.RECHT_FERT_LOS_CUD, RechteFac.RECHT_FERT_LOS_R})
	public LosDto losFindByPrimaryKeyOhneExc(Integer losId) {
		LosDto losDto = getFac().losFindByPrimaryKeyOhneExc(losId) ;
		if(losDto != null) {
			if(!losDto.getMandantCNr().equals(globalInfo.getMandant())) {
				losDto = null ;
			}
		}
		return losDto ;
	}	
	
	@Override
	public LosDto losFindByCNrMandantCNrOhneExc(String cNr) {
		return losFindByCNrMandantCNrOhneExc(cNr, globalInfo.getMandant()) ;
	}

	@Override
	public LosDto losFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr) {
		return getFac().losFindByCNrMandantCNrOhneExc(cNr, mandantCNr) ;
	}
	
	@Override
	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId)
			throws RemoteException, EJBExceptionLP {
		return getFac().loslagerentnahmeFindByLosIId(losIId) ;
	}
	
	
	@Override
	public void gebeMaterialNachtraeglichAus(
			LossollmaterialDto lossollmaterialDto, LosistmaterialDto losistmaterialDto, 
			List<SeriennrChargennrMitMengeDto> listSnrChnr, boolean reduzierFehlmenge)
		throws RemoteException, EJBExceptionLP {
		getFac().gebeMaterialNachtraeglichAus(lossollmaterialDto,
				losistmaterialDto, listSnrChnr, reduzierFehlmenge, globalInfo.getTheClientDto());		
	}
	
	@Override
	public LossollmaterialDto[] lossollmaterialFindByLosIIdOrderByISort(
			Integer losIId) throws RemoteException, EJBExceptionLP {
		return getFac().lossollmaterialFindByLosIIdOrderByISort(losIId) ;
	}
	
	@Override
	public LosistmaterialDto[] losistmaterialFindByLossollmaterialIId(
			Integer lossollmaterialIId) throws RemoteException, EJBExceptionLP {
		return getFac().losistmaterialFindByLossollmaterialIId(lossollmaterialIId) ;
	}
	
	@Override
	public void updateLosistmaterialMenge(Integer losistmaterialIId,
			BigDecimal bdMengeNeu) throws RemoteException, EJBExceptionLP {
		getFac().updateLosistmaterialMenge(losistmaterialIId, bdMengeNeu, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public LosistmaterialDto createLosistmaterial(
			LosistmaterialDto losistmaterialDto, String sSerienChargennummer) throws RemoteException,
			EJBExceptionLP {
		return getFac().createLosistmaterial(losistmaterialDto, sSerienChargennummer, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public LossollmaterialDto lossollmaterialFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP {
		return getFac().lossollmaterialFindByPrimaryKeyOhneExc(iId) ;
	}
	
	@Override
	public void updateLossollmaterialMenge(Integer lossollmaterialId, BigDecimal mengeNeu) 
			throws RemoteException, EJBExceptionLP {
		LossollmaterialDto dto = getFac().lossollmaterialFindByPrimaryKey(lossollmaterialId) ;
		dto.setNMenge(mengeNeu);
		getFac().updateLossollmaterial(dto, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP {
		return getFac().lossollarbeitsplanFindByPrimaryKey(iId)	;	
	}
	
	@Override
	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKeyOhneExc(Integer iId) throws RemoteException, EJBExceptionLP {
		return getFac().lossollarbeitsplanFindByPrimaryKeyOhneExc(iId)	;	
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LOS)
	@HvJudge(recht=RechteFac.RECHT_FERT_LOS_CUD)
	public LossollarbeitsplanDto updateLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto) throws RemoteException, EJBExceptionLP {
		return getFac().updateLossollarbeitsplan(lossollarbeitsplanDto, globalInfo.getTheClientDto()) ;
	}

	public LossollarbeitsplanDto updateLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto, boolean verschiebeNachfolger) throws RemoteException, EJBExceptionLP {
		return getFac().updateLossollarbeitsplan(lossollarbeitsplanDto, verschiebeNachfolger, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public LosDto updateLos(LosDto losDto) throws RemoteException, EJBExceptionLP {
		if(parameterCall.getAuslastungsAnzeigeDetailAg() == AuslastungsAnzeigeDetailAg.PROJEKT_KBEZ) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "Endtermin darf durch API nicht veraendert werden!") ;
		}
		return getFac().updateLos(losDto, globalInfo.getTheClientDto());
	}


	@Override
	public LoszusatzstatusDto[] loszusatzstatusFindByLosIIdOhneExc(Integer losIId) throws RemoteException {
		return getFac().loszusatzstatusFindByLosIIdOhneExc(losIId);
	}


	@Override
	public ZusatzstatusDto zusatzstatusFindByPrimaryKey(Integer iId) throws RemoteException {
		return getFac().zusatzstatusFindByPrimaryKey(iId);
	}
	
	public BigDecimal getAusgegebeneMenge(Integer lossollmaterialId) throws RemoteException {
		return getFac().getAusgegebeneMenge(lossollmaterialId, null, globalInfo.getTheClientDto());
	}
	
	public LossollmaterialDto[] lossollmaterialFindyByLosIIdArtikelIId(
			Integer losIId, Integer artikelIId) throws RemoteException {
		return getFac().lossollmaterialFindyByLosIIdArtikelIId(losIId, artikelIId, globalInfo.getTheClientDto());
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LOS)
	@HvJudge(recht=RechteFac.RECHT_FERT_LOS_CUD)
	public void terminverschieben(Integer losId, Timestamp start, Timestamp finish) {
		getFac().terminVerschieben(losId, start, finish, globalInfo.getTheClientDto());
	}


	@Override
	public LosablieferungDto[] losablieferungFindByLosIId(Integer losId) throws RemoteException {
		return getFac().losablieferungFindByLosIId(losId, false, globalInfo.getTheClientDto());
	}


	@Override
	public LosablieferungDto losablieferungFindByPrimaryKeyOhneExc(Integer losablieferungIId) {
		return getFac().losablieferungFindByPrimaryKeyOhneExc(losablieferungIId);
	}


	@Override
	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIId(Integer losIId) throws EJBExceptionLP, RemoteException {
		return getFac().lossollarbeitsplanFindByLosIId(losIId);
	}


	@Override
	public void setzeVPEtikettGedruckt(Integer losIId) {
		getFac().setzeVPEtikettGedruckt(losIId, globalInfo.getTheClientDto());
	}
	
	public BigDecimal getErledigteMenge(Integer losIId) throws EJBExceptionLP, RemoteException {
		return getFac().getErledigteMenge(losIId, globalInfo.getTheClientDto());
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LOS)
	@HvJudge(recht=RechteFac.RECHT_FERT_LOS_CUD)
	public void aendereLosgroesse(Integer losIId, BigDecimal neueLosgroesse) throws RemoteException {
		getFac().aendereLosgroesse(losIId, neueLosgroesse.intValue(), true, globalInfo.getTheClientDto());
	}

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(Integer losIId, BigDecimal menge) throws EJBExceptionLP, RemoteException {
		getFac().pruefePositionenMitSollsatzgroesseUnterschreitung(losIId, menge, true, globalInfo.getTheClientDto());
	}
	
	public LosablieferungDto createLosablieferungFuerTerminal(LosablieferungTerminalDto laDto) throws RemoteException, EJBExceptionLP {
		return getFac().createLosablieferungFuerTerminal(laDto, globalInfo.getTheClientDto());
	}

	@Override
	public LosDto losFindByForecastpositionIIdOhneExc(Integer forecastpositionIId) {
		return getFac().losFindByForecastpositionIIdOhneExc(forecastpositionIId);
	}
	
	@Override
	public List<LosDto> losFindOffeneByMe(List<String> stati, Integer tageInZukunft) {
		return getFac().losFindOffeneByMe(stati, tageInZukunft, globalInfo.getTheClientDto());
	}
	
	@Override
	public EJBExceptionLP gebeLosAus(Integer losId, boolean handausgabe, boolean throwWhenCreate) throws RemoteException {
		return getFac().gebeLosAus(losId, handausgabe, throwWhenCreate, globalInfo.getTheClientDto(), null);
	}
	
	@Override
	public String getArtikelhinweiseAllerLossollpositionen(Integer losId) throws RemoteException {
		return getFac().getArtikelhinweiseAllerLossollpositionen(losId, globalInfo.getTheClientDto());
	}
	
	@Override
	public void setzeLosInProduktion(Integer losId) throws RemoteException {
		getFac().setzeLosInProduktion(losId, globalInfo.getTheClientDto());
	}
	
	@Override
	public EJBExceptionLP manuellErledigen(Integer losId, boolean datumDerLetztenAblieferungVerwenden) throws RemoteException {
		return getFac().manuellErledigen(losId, datumDerLetztenAblieferungVerwenden, globalInfo.getTheClientDto());
	}

	@Override
	public ZusatzstatusDto zusatzstatusFindByMandantCNrCBezOhneExc(String zusatzstatus, String mandantCnr) {
		return getFac().zusatzstatusFindByMandantCNrCBezOhneExc(mandantCnr, zusatzstatus);
	}

	@Override
	public ZusatzstatusDto zusatzstatusFindByMandantCNrCBezOhneExc(String zusatzstatus) {
		return zusatzstatusFindByMandantCNrCBezOhneExc(zusatzstatus, globalInfo.getMandant());
	}
	
	@Override
	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(Integer losIId, Integer zusatzstatusIId) throws RemoteException {
		return getFac().loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(losIId, zusatzstatusIId);
	}
	
	@Override
	public Integer createLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto) throws RemoteException {
		return getFac().createLoszusatzstatus(loszusatzstatusDto, globalInfo.getTheClientDto());
	}
	
	@Override
	public Integer createBedarfsuebernahme(BedarfsuebernahmeDto bedarfsuebernahmeDto) {
		return getFac().createBedarfsuebernahme(bedarfsuebernahmeDto, globalInfo.getTheClientDto());
	}
	
	@Override
	public BigDecimal getMengeDerJuengstenLosablieferung(Integer losId) throws RemoteException {
		return getFac().getMengeDerJuengstenLosablieferung(losId, globalInfo.getTheClientDto());
	}
	
	@Override
	public LossollarbeitsplanDto lossollarbeitsplanFindByLosIIdArbeitsgangnummerSingleResult(Integer losId, Integer arbeitsgang) {
		List<LossollarbeitsplanDto> arbeitsplanList = getFac().lossollarbeitsplanFindByLosIIdArbeitsgangnummer(losId, arbeitsgang);
		return arbeitsplanList.isEmpty() ? null : arbeitsplanList.get(0);
	}
	
	@Override
	public LossollarbeitsplanDto lossollarbeitsplanFindByLosIIdArbeitsgangUnterarbeitsgangSingleResult(
			Integer losId, Integer arbeitsgang, Integer unterarbeitsgang) {
		if (unterarbeitsgang == null) {
			return lossollarbeitsplanFindByLosIIdArbeitsgangnummerSingleResult(losId, arbeitsgang);
		} 

		List<LossollarbeitsplanDto> arbeitsplanList = getFac().lossollarbeitsplanFindByLosIIdArbeitsgangnummerUnterarbeitsgang(
					losId, arbeitsgang, unterarbeitsgang);
		return arbeitsplanList.isEmpty() ? null : arbeitsplanList.get(0);
	}
	
	@Override
	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(Integer losId, Integer artikelId) throws EJBExceptionLP, RemoteException {
		return getFac().lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(losId, artikelId);
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LOS)
	@HvJudge(recht=RechteFac.RECHT_FERT_LOS_CUD)
	public void stoppeProduktion(Integer losId) throws EJBExceptionLP, RemoteException {
		getFac().stoppeProduktion(losId, globalInfo.getTheClientDto());
	}
}
