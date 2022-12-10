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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.legacy.AllLagerEntry;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.LagerplatzInfoDto;
import com.lp.server.artikel.service.LagerstandInfoDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.util.EJBExceptionLP;

public class LagerCall extends BaseCall<LagerFac> implements ILagerCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public LagerCall() {
		super(LagerFac.class);
	}
	
	public Integer artikelIdFindBySeriennummerOhneExc(String serialnumber) throws RemoteException {
		Integer itemId = getFac().getArtikelIIdUeberSeriennummer(serialnumber, globalInfo.getTheClientDto()) ;
		if(itemId == null) {
			itemId = getFac().getArtikelIIdUeberSeriennummerAbgang(serialnumber, globalInfo.getTheClientDto()) ;
		}

		return itemId ;
	}

	@Override
	public BigDecimal  getGemittelterGestehungspreisEinesLagers(
			Integer itemId, Integer lagerId) throws  RemoteException {
		return getFac().getGemittelterGestehungspreisEinesLagers(itemId, lagerId, globalInfo.getTheClientDto()) ;
	}

	@Override
	public LagerDto lagerFindByPrimaryKeyOhneExc(Integer lagerIId) {
		LagerDto lagerDto = getFac().lagerFindByPrimaryKeyOhneExc(lagerIId) ;
		if(lagerDto != null) {
			if(!globalInfo.getTheClientDto().getMandant().equals(lagerDto.getMandantCNr())) {
				lagerDto = null ;
			}
		}

		return lagerDto ;
	}

	@Override
	public LagerDto lagerFindByCnrOhnExc(String lagerCnr) throws RemoteException {
		LagerDto lagerDto = getFac().lagerFindByCNrByMandantCNrOhneExc(lagerCnr, globalInfo.getMandant()) ;
		return lagerDto ;
	}
	
	@Override
	public BigDecimal getLagerstandOhneExc(Integer itemId, Integer lagerIId)
			throws RemoteException {
		return getFac().getLagerstandOhneExc(itemId, lagerIId, globalInfo.getTheClientDto());
	}

	@Override
	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer itemId, Boolean mitKonsignationsLager) throws RemoteException {
		return getFac().getLagerstandAllerLagerEinesMandanten(itemId, mitKonsignationsLager, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public BigDecimal getPaternosterLagerstand(Integer itemId) throws RemoteException {
		return getFac().getPaternosterLagerstand(itemId) ;
	}
	
	@Override
	public boolean hatRolleBerechtigungAufLager(Integer lagerIId) {
		return getFac().hatRolleBerechtigungAufLager(lagerIId, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public boolean hatRolleBerechtigungAufLager(Collection<Integer> lagerIIds) {
		for (Integer iId : lagerIIds) {
			if (!hatRolleBerechtigungAufLager(iId))
				return false;
		}
		return true;
	}

	@Override
	public List<AllLagerEntry> getAllLager() throws RemoteException, EJBExceptionLP {
		List<AllLagerEntry> stocks = new ArrayList<AllLagerEntry>() ;
		@SuppressWarnings("unchecked")
		Map<Integer,String> m = (Map<Integer,String>) getFac().getAllLager(globalInfo.getTheClientDto()) ;
		for (Entry<Integer, String> entry : m.entrySet()) {
			stocks.add(new AllLagerEntry(entry.getKey(), entry.getValue())) ;
		}
		return stocks ;
	}

	@Override
	public BigDecimal getLagerstandsVeraenderungOhneInventurbuchungen(
			Integer artikelIId, Integer lagerIId, Timestamp tVon, Timestamp tBis)
			throws RemoteException {
		return getFac().getLagerstandsVeraenderungOhneInventurbuchungen(
				artikelIId, lagerIId, tVon, tBis,null, globalInfo.getTheClientDto());
	}

	@Override
	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartposition(
			String belegartCNr, Integer belegartpositionIId) throws RemoteException {
		return getFac().getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(belegartCNr, belegartpositionIId) ;
	}
	
	@Override
	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
			String belegartCNr, Integer belegartpositionIId) throws RemoteException {
		return getFac().getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(belegartCNr, belegartpositionIId) ;
	}
	
	@Override
	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum) throws RemoteException {
		getFac().bucheZu(belegartCNr, belegartIId, belegartpositionIId, artikelIId,
				fMengeAbsolut, nEinstansdpreis, lagerIId, alSeriennrchargennr,
				tBelegdatum, globalInfo.getTheClientDto(), null, null);
	}

	@Override
	public BigDecimal getEinstandspreis(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrChargennr)
			throws RemoteException, EJBExceptionLP {
		return getFac().getEinstandspreis(belegartCNr, belegartpositionIId, cSeriennrChargennr) ;
	}
	
	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerIId(
			Integer artikelIId, Integer lagerIId) throws RemoteException {
		return getFac().artikellagerplaetzeFindByArtikelIIdLagerIId(artikelIId, lagerIId) ;
	}
	
	public LagerDto getHauptlager() throws RemoteException {
		return getFac().getHauptlagerDesMandanten(globalInfo.getTheClientDto()) ;
	}
	
	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId) {
		return getFac().getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId,
				null, true, null, globalInfo.getTheClientDto());
	}	

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, java.sql.Timestamp tStichtag) {
		return getFac().getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId,
				null, true, tStichtag, globalInfo.getTheClientDto());
	}	

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, String serienChargenNr) {
		return getFac().getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId,
				serienChargenNr, true, null, globalInfo.getTheClientDto());
	}	
	
	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, String serienChargenNr, java.sql.Timestamp tStichtag) {
		return getFac().getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId,
				serienChargenNr, true, tStichtag, globalInfo.getTheClientDto());
	}

	@Override
	public LagerDto lagerFindByMandantCNrLagerartCNrOhneExc(String lagerartCnr) throws RemoteException {
		return getFac().lagerFindByMandantCNrLagerartCNrOhneExc(
				globalInfo.getTheClientDto().getMandant(), lagerartCnr);
	}	
	
	@Override
	public List<LagerplatzDto> lagerplatzFindByArtikelIIdOhneExc(Integer artikelIId) {
		return getFac().lagerplatzFindByArtikelIIdLagerIIdOhneExc(artikelIId, null);
	}

	@Override
	@HvJudge(recht=RechteFac.RECHT_WW_ARTIKEL_LAGERPLATZ_CUD)
	public Integer createArtikellagerplaetze(ArtikellagerplaetzeDto artikellagerplatzDto) 
			throws RemoteException {
		return getFac().createArtikellagerplaetze(artikellagerplatzDto, globalInfo.getTheClientDto());
	}

	@Override
	public LagerplatzDto lagerplatzFindByPrimaryKeyOhneExc(Integer lagerplatzIId) {
		return getFac().lagerplatzFindByPrimaryKeyOhneExc(lagerplatzIId);
	}

	@Override
	public LagerplatzDto lagerplatzFindByCLagerplatzLagerIIdOhneExc(String cLagerplatz, Integer lagerIId) {
		return getFac().lagerplatzFindByCLagerplatzLagerIIdOhneExc(cLagerplatz, lagerIId);
	}

	@Override
	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(
			Integer artikelIId, Integer lagerplatzIId) throws RemoteException {
		return getFac().artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(artikelIId, lagerplatzIId);
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_WW_ARTIKEL_LAGERPLATZ_CUD)
	public void removeArtikellagerplaetze(Integer artikellagerplaetzeIId) throws RemoteException {
		getFac().removeArtikellagerplaetze(artikellagerplaetzeIId, globalInfo.getTheClientDto());
	}
	
	@Override
	public List<LagerplatzDto> lagerplatzFindByArtikelIIdLagerIIdOhneExc(Integer artikelIId, Integer lagerIId) {
		return getFac().lagerplatzFindByArtikelIIdLagerIIdOhneExc(artikelIId, lagerIId);
	}
	
	@Override
	public List<LosDto> getAlleBetroffenenLose(Integer artikelIId, String chargennummer) throws RemoteException {
		return getFac().getAlleBetroffenenLoseEinerArtikelIIdUndCharge(artikelIId, chargennummer);
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_WW_HANDLAGERBEWEGUNG_CUD)
	public List<LosDto> chargennummerWegwerfen(Integer artikelIId, String chargennummer, List<LosDto> losDtos) throws RemoteException {
		return getFac().chargennummerWegwerfen(artikelIId, chargennummer, losDtos, true, globalInfo.getTheClientDto());
	}
	
	@Override
	public LagerbewegungDto[] lagerbewegungFindByArtikelIIdCSeriennrChargennr(
			Integer artikelIId, String cSnrChnr) throws RemoteException {
		return getFac().lagerbewegungFindByArtikelIIdCSeriennrChargennr(artikelIId, cSnrChnr);
	}
	
	@Override
	public List<ArtikellagerplaetzeDto> artikellagerplaetzeFindByLagerplatzIId(Integer lagerplatzIId) {
		return getFac().artikellagerplaetzeFindByLagerplatzIId(lagerplatzIId);
	}
	
	@Override
	public List<LagerplatzInfoDto> lagerplatzInfoFindByArtikelIIdOhneExc(Integer artikelIId) {
		return getFac().lagerplatzInfoFindByArtikelIIdOhneExc(artikelIId);
	}
	
	@Override
	public Collection<LagerstandInfoDto> getLagerstandAllerArtikelEinesMandanten(
			String[] lagerartCnr) {
		return getFac().getLagerstandAllerArtikelEinesMandanten(
				lagerartCnr, globalInfo.getTheClientDto());
	}
	
	@Override
	public Collection<LagerstandInfoDto> getLagerstandAllerArtikelEinerGruppe(
			Integer lagerId, Integer artikelgruppeId) {
		return getFac().getLagerstandAllerArtikelEinerGruppe(lagerId, 
				artikelgruppeId, globalInfo.getTheClientDto());	
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_WW_HANDLAGERBEWEGUNG_CUD)
	public Integer createHandlagerbewegung(HandlagerbewegungDto handlagerbewegungDto) throws RemoteException {
		return getFac().createHandlagerbewegung(handlagerbewegungDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_WW_HANDLAGERBEWEGUNG_CUD)
	public Integer bucheUm(Integer artikelIId, Integer sourceLagerId,
			Integer targetLagerId, BigDecimal amount, 
			List<SeriennrChargennrMitMengeDto> identities, 
			String comment, BigDecimal price) throws RemoteException {
		return getFac().bucheUm(artikelIId, sourceLagerId, artikelIId,
				targetLagerId, amount, identities, 
				comment, price, globalInfo.getTheClientDto());
	}
	
	@Override
	public Integer artikelSerienChargenNrFindBy(Integer artikelId, String identity) {
		return getFac().artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr(artikelId, identity);
	}
	
	@Override
	public PaneldatenDto[] getLetzteChargeninfosEinesArtikels(Integer artikelIId, String identity) {
		return getFac().getLetzteChargeninfosEinesArtikels(artikelIId, null, null, null,
						identity, globalInfo.getTheClientDto());
	}
}
