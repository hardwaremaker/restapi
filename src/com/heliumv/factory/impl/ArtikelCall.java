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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.legacy.AllArtikelgruppeEntry;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelMitVerpackungsgroessenDto;
import com.lp.server.artikel.service.ArtikelsperrenSperrenDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class ArtikelCall extends BaseCall<ArtikelFac> implements IArtikelCall {
	
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public ArtikelCall() {
		super(ArtikelFac.class) ;
	}

	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public ArtikelDto artikelFindByCNrOhneExc(String cNr) throws RemoteException {
		return getFac().artikelFindByCNrOhneExc(cNr, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder={RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public ArtikelDto artikelFindByPrimaryKeyOhneExc(Integer itemId) throws RemoteException {
		return getFac().artikelFindByPrimaryKeyOhneExc(itemId, globalInfo.getTheClientDto());
	}
	
	public ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer itemId) throws RemoteException {
		return getFac().artikelFindByPrimaryKeySmallOhneExc(itemId, globalInfo.getTheClientDto()) ;
	}
	

	public ArtikelMitVerpackungsgroessenDto artikelFindByEanOhneExc(String ean) throws RemoteException {
		return getFac().artikelFindByEanMandantCnr(ean, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public ArtgruDto artikelgruppeFindByPrimaryKeyOhneExc(Integer artikelgruppeId) throws RemoteException {
		try {
			return getFac().artgruFindByPrimaryKey(artikelgruppeId, globalInfo.getTheClientDto()) ;
		} catch(EJBExceptionLP e) {
			return null ;
		}
	}
	
	@Override
	public ArtgruDto artikelgruppeFindByCnrOhneExc(String artikelgruppeCnr) throws RemoteException {
		try {
			List<ArtgruDto> artikelgruppeDtos = getFac().artgruFindByMandantCNrSpr(globalInfo.getTheClientDto()) ;
			for (ArtgruDto artgruDto : artikelgruppeDtos) {
				if(artgruDto.getCNr().equals(artikelgruppeCnr)) return artgruDto ;
			}
			
			return null ;
		} catch(EJBExceptionLP e) {
			return null ;
		}
	}
	
	
	@Override
	public List<ArtgruDto> artikelgruppeFindByMandantCNr() throws RemoteException {
		return getFac().artgruEingeschraenktFindByMandantCNrSpr(
				globalInfo.getTheClientDto());
	}

	@Override
	public ArtklaDto artikelklasseFindByPrimaryKeyOhneExc(Integer artikelklasseId) throws RemoteException {
		try {
			return getFac().artklaFindByPrimaryKey(artikelklasseId, globalInfo.getTheClientDto()) ;
		} catch(EJBExceptionLP e) {
			return null ;
		}
	}
	
	@Override
	public ArtklaDto artikelklasseFindByCnrOhneExc(String artikelklasseCnr) throws RemoteException {
		try {
			ArtklaDto[] artikelklasseDtos = getFac().artklaFindByMandantCNr(globalInfo.getTheClientDto()) ;
			for (ArtklaDto artklaDto : artikelklasseDtos) {
				if(artklaDto.getCNr().equals(artikelklasseCnr)) return artklaDto ;
			}
			
			return null ;
		} catch(EJBExceptionLP e) {
			return null ;
		}
	}

	@Override
	public List<AllArtikelgruppeEntry> getAllArtikelgruppeSpr() {
		@SuppressWarnings("unchecked")
		Map<String, String> map = getFac().getAllSprArtgru(globalInfo.getTheClientDto()) ;
		List<AllArtikelgruppeEntry> entries = new ArrayList<AllArtikelgruppeEntry>() ;
		for (Map.Entry<String, String> mapEntry : map.entrySet()) {
			entries.add(new AllArtikelgruppeEntry(mapEntry.getKey(), mapEntry.getValue())) ;
		}
		
		return entries ;
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder={RechteFac.RECHT_WW_ARTIKEL_CUD, RechteFac.RECHT_WW_ARTIKEL_R})
	public HerstellerDto herstellerFindByPrimaryKey(Integer herstellerId) {
		return getFac().herstellerFindByPrimaryKey(herstellerId, globalInfo.getTheClientDto()) ;
	}

	@Override
	public ArtikelsprDto artikelSprFindByArtikelIIdOhneExc(Integer artikelIId) throws RemoteException {
		ArtikelsprDto artikelsprDto = getFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelIId, 
				globalInfo.getTheClientDto().getLocUiAsString(), globalInfo.getTheClientDto());
		if (artikelsprDto == null) {
			artikelsprDto = getFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelIId, 
					globalInfo.getTheClientDto().getLocKonzernAsString(), globalInfo.getTheClientDto());
		}
		return artikelsprDto;
	}

	@Override
	public ArtikelDto artikelFindByPrimaryKeySmall(Integer artikelIId) {
		return getFac().artikelFindByPrimaryKeySmall(artikelIId, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public ArtikelDto artikelFindByArtikelnrlieferant(String cnr, Integer lieferantId) throws RemoteException {
		return getFac().artikelFindByArtikelnrlieferant(cnr, lieferantId, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public List<ArtikelDto> artikelFindByArtikelnrhersteller(String cnr) throws RemoteException {
		return getFac().artikelFindByArtikelnrHersteller(cnr, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public List<ArtikelsperrenSperrenDto> artikelsperrenSperrenFindByArtikelIId(Integer artikelId) throws RemoteException {
		return getFac().artikelsperrenFindByArtikelIIdMitSperren(artikelId);
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public List<Integer> getEingeschraenkteArtikelgruppen() {
		return getFac().getEingeschraenkteArtikelgruppen(globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public List<ArtikelDto> artikelFindByHerstellernummerausBarcode(String herstellerBarcode) {
		return getFac().artikelFindByHerstellernummerausBarcode(herstellerBarcode, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public ShopgruppeDto shopgruppeFindByCNrMandantOhneExc(String cnr) {
		return getFac().shopgruppeFindByCNrMandantOhneExc(cnr, globalInfo.getTheClientDto());
	}

	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId) {
		return getFac().shopgruppeFindByPrimaryKey(iId, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public ShopgruppeDto shopgruppeFindByPrimaryKeyOhneExc(Integer iId) {
		try {
			return getFac().shopgruppeFindByPrimaryKey(iId, globalInfo.getTheClientDto());
		} catch(EJBExceptionLP e) {
			if(e.getCode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
				return null;
			}
			
			throw e;
		}
	}
}
