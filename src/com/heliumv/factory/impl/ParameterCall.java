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

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.delivery.DeliveryPositionSort;
import com.heliumv.api.system.CnrFormat;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.impl.transform.DeliverySortTransformer;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParameteranwenderDto;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;

public class ParameterCall extends BaseCall<ParameterFac> implements IParameterCall {
	@Autowired
	private IGlobalInfo globalInfo ;
		
	public ParameterCall() {
		super(ParameterFac.class);
	}

	public boolean isZeitdatenAufErledigteBuchbar() throws RemoteException {
		String s = getMandantParamImpl(ParameterFac.KATEGORIE_PERSONAL, 
				ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH).getCWert();
		return "1".equals(s) ;
	}

	public boolean isZeitdatenAufAngelegteLoseBuchbar() throws RemoteException {
		String s = getMandantParamImpl(ParameterFac.KATEGORIE_PERSONAL,
				ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH).getCWert();
		return "1".equals(s) ;
	}
	
	public boolean isPartnerSucheWildcardBeidseitig() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_PARTNER, 
				ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG).asBoolean();
	}
	
	
	public boolean isKeineAutomatischeMaterialbuchung() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG).asBoolean();
	}
	
	public boolean isBeiLosErledigenMaterialNachbuchen() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN).asBoolean();
	}
	
	public int getMaximaleLaengeArtikelnummer()  throws RemoteException {
		int defaultLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER ;
		
		ParametermandantDto parameter = getFac()
				.getMandantparameter(globalInfo.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
		if (parameter.getCWertAsObject() != null) {
			defaultLaenge = ((Integer) parameter.getCWertAsObject()).intValue();
		}
		
		return defaultLaenge ;
	}
	
	public boolean isArtikelDirektfilterGruppeKlasseStattReferenznummer() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER).asBoolean();
	}
	
	public Integer getGeschaeftsjahr(String mandantCNr) throws RemoteException, EJBExceptionLP {
		return getFac().getGeschaeftsjahr(mandantCNr) ;
	}
	
	public Integer getGeschaeftsjahr() throws RemoteException, EJBExceptionLP {
		return getFac().getGeschaeftsjahr(globalInfo.getMandant()) ;
	}

	public String getMailAdresseAdmin() throws RemoteException, EJBExceptionLP {
		return getMandantParamImpl(ParameterFac.KATEGORIE_VERSANDAUFTRAG, 
				ParameterFac.PARAMETER_MAILADRESSE_ADMIN).getCWert();
	}
	
	@Override
	public AuslastungsZeitenberechnung getAuslastungszeitenBerechnung() throws 
			RemoteException, EJBExceptionLP {
		Integer v = getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_AUSLASTUNGSZEITENBERECHNUNG).asInteger();
		if(v == 0) return AuslastungsZeitenberechnung.SOLLZEIT ;
		if(v == 1) return AuslastungsZeitenberechnung.RESTZEIT ;
		return AuslastungsZeitenberechnung.UNBEKANNT ;	
	}
	
	@Override
	public ParametermandantDto getMandantParameter(String category, String cnr) throws RemoteException, EJBExceptionLP {
		return getMandantParamImpl(category, cnr);
	}
	
	@Override
	public AuslastungsAnzeigeDetailAg getAuslastungsAnzeigeDetailAg()
			throws RemoteException, EJBExceptionLP {
		Integer v = getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_AUSLASTUNGSANZEIGE_DETAIL_AG).asInteger();
		if(v == 0) return AuslastungsAnzeigeDetailAg.STANDARD ;
		if(v == 1) return AuslastungsAnzeigeDetailAg.PROJEKT_KBEZ ;
		return AuslastungsAnzeigeDetailAg.UNBEKANNT ;	
	}
	
	@Override
	public boolean getKundenPositionsKontierung() throws RemoteException {
		return isArtikelBestimmtMwstsatz() ;
	}
	
	@Override
	public boolean isArtikelBestimmtMwstsatz() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_KUNDEN, 
				ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG).asBoolean();			
	}
	
	@Override
	public ArbeitsplatzDto arbeitsplatzFindByCTypCGeraetecode(
			String cTyp, String cGeraetecode) throws RemoteException {
		return getFac().arbeitsplatzFindByCTypCGeraetecode(cTyp, cGeraetecode);
	}
	
	public ArbeitsplatzkonfigurationDto arbeitsplatzkonfigurationFindByPrimaryKey(Integer arbeitsplatzId) {
		return getFac().arbeitsplatzkonfigurationFindByPrimaryKey(arbeitsplatzId) ;
	}
	
	public ArbeitsplatzDto arbeitsplatzFindByCPcname() throws RemoteException {
		String pcname = globalInfo.getArbeitsplatzname();
		
		return pcname == null ? null
				: getFac().arbeitsplatzFindByCPcname(pcname);
	}

	public void updateArbeitsplatzkonfiguration(ArbeitsplatzkonfigurationDto konfigurationDto) {
		getFac().updateArbeitsplatzkonfiguration(konfigurationDto);
	}
	
	private ParametermandantDto getMandantParamImpl(
			String category, String cnr) throws RemoteException {
		return getFac().getMandantparameter(globalInfo.getMandant(), category, cnr) ;
	}
	
	private ParametermandantDto getMandantParamImpl(
			String category, String cnr, Timestamp timestamp) throws RemoteException {
		return getFac().getMandantparameter(globalInfo.getMandant(), category, cnr, timestamp) ;
	}

	@Override
	public int getAuslastungsAnzeigeEinlastungsRaster() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_AUSLASTUNGSANZEIGE_EINLASTUNGRASTER).asInteger();
	}
	
	@Override
	public int getAuslastungsAnzeigePufferdauer() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_AUSLASTUNGSANZEIGE_PUFFERDAUER).asInteger();
	}
	
	public String getDefaultArbeitszeitArtikelCnr() throws RemoteException {
		String value = getMandantParamImpl(ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL).getCWert();
		return StringHelper.isEmpty(value) ? null : value;
	}
	
	public boolean isArbeitszeitartikelAusPersonalverfuegbarkeit() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_PERSONAL,
				ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT).asBoolean();
	}
	
	public boolean isSollsatzgroessePruefen() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_SOLLSATZGROESSE_PRUEFEN).asBoolean();
	}
	
	public boolean isAblieferungBuchtEnde() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_ABLIEFERUNG_BUCHT_ENDE).asBoolean();
	}
	
	public boolean isAutofertigAblieferungTerminal() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_AUTOFERTIG_ABLIEFERUNG_TERMINAL).asBoolean();
	}
	
	public Double getGemeinkostenFaktor(Timestamp timestamp) throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_ALLGEMEIN, 
				ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR, timestamp).asDouble();
	}
	
	public boolean getLieferscheinPositionenMitAPIVerdichten() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_LIEFERSCHEIN,
				ParameterFac.PARAMETER_LIEFERSCHEIN_POSITIONEN_MIT_RESTAPI_VERDICHTEN).asBoolean();
	}
	
	private ParameteranwenderDto getAnwenderParamImpl(String category, String cnr) throws RemoteException {
		return getFac().getAnwenderparameter(category, cnr);
	}
	
	@Override
	public CnrFormat getBelegnummernformat() throws RemoteException {
		Integer digitsReceiptNumber = getFac().getBelegnummernformatStellenBelegnummer(globalInfo.getMandant());
		Integer digitsBusinessYear = getFac().getBelegnummernformatStellenGeschaeftsjahr(globalInfo.getMandant());
		Integer digitsRandomNumber = getMandantParamImpl(ParameterFac.KATEGORIE_ALLGEMEIN, 
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_ZUFALL).asInteger();
		String tag = getMandantParamImpl(ParameterFac.KATEGORIE_ALLGEMEIN, 
				ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN).getCWert();

		Boolean hasTenantIdentifier = getAnwenderParamImpl(ParameterFac.KATEGORIE_ALLGEMEIN, 
				ParameterFac.ANWENDERPARAMETER_BELEGNUMMER_MIT_MANDANTKENNUNG).asBoolean();
		if (Boolean.TRUE.equals(hasTenantIdentifier)) {
			Integer digitsTenantIdentifier = getAnwenderParamImpl(ParameterFac.KATEGORIE_ALLGEMEIN, 
					ParameterFac.ANWENDERPARAMETER_BELEGNUMMER_STELLEN_MANDANTKENNUNG).asInteger();
			String tenantIdentifier = getMandantParameter(ParameterFac.KATEGORIE_ALLGEMEIN, 
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
			return new CnrFormat(digitsReceiptNumber, digitsBusinessYear, digitsRandomNumber, tag, 
					digitsTenantIdentifier, tenantIdentifier);
		}
		
		return new CnrFormat(digitsReceiptNumber, digitsBusinessYear, digitsRandomNumber, tag);
	}
	
	@Override
	public boolean isWarnungWennFehlmengeEntsteht() throws RemoteException {
		return getMandantParamImpl(ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_FEHLMENGE_ENTSTEHEN_WARNUNG).asBoolean();
	}
	
	@Override
	public String getGroessenaenderungZusatzstatus() {
		return getFac().getGroessenaenderungZusatzstatus(globalInfo.getMandant());
	}
	
	private Integer getArbeitsplatzId() throws RemoteException {
		try {
			ArbeitsplatzDto platzDto = arbeitsplatzFindByCPcname();
			return platzDto == null ? null : platzDto.getIId();
		} catch(EJBExceptionLP e) {
			return null;
		}
	}
	
	@Override
	public DeliveryPositionSort getLieferscheinpositionSortierung() throws RemoteException {
		Integer arbeitsplatzId = getArbeitsplatzId();
		if(arbeitsplatzId == null) return DeliveryPositionSort.NOTINITIALIZED;
		return getLieferscheinpositionSortierung(arbeitsplatzId);
	}
	
	
	@Override
	public DeliveryPositionSort getLieferscheinpositionSortierung(
			Integer arbeitsplatzId) throws RemoteException {
		try {
			ArbeitsplatzparameterDto paramDto = getFac()
					.arbeitsplatzparameterFindByArbeitsplatzIIdParameterCNr(
					arbeitsplatzId, ParameterFac.ARBEITSPLATZPARAMETER_MOBIL_LS_SORTIERUNG);
			return DeliverySortTransformer.transform(paramDto.getCWert());			
		} catch(Exception e) {
			return DeliveryPositionSort.NOTINITIALIZED;
		}
/*		
		return StringHelper.isEmpty(paramDto.getCWert()) 
				? DeliveryPositionSort.NOTINITIALIZED 
				: DeliveryPositionSort.fromString(paramDto.getCWert());
 */				
	}
}
