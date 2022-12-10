package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IForecastCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.FclieferadresseNoka;
import com.lp.server.forecast.service.FclieferadresseNokaDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionArtikelbuchungDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ForecastpositionProduktionDto;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.forecast.service.LinienabrufArtikelDto;
import com.lp.server.forecast.service.LinienabrufArtikelbuchungDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.util.EJBExceptionLP;

public class ForecastCall extends BaseCall<ForecastFac> implements IForecastCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	public ForecastCall() {
		super(ForecastFac.class);
	}

	@Override
	public ForecastauftragDto forecastauftragFindByPrimaryKey(Integer iId) {
		return getFac().forecastauftragFindByPrimaryKey(iId);
	}

	@Override
	public ForecastpositionDto forecastpositionFindByPrimaryKey(Integer iId) {
		return getFac().forecastpositonFindByPrimaryKeyOhneExc(iId);
	}

	@Override
	public LinienabrufDto linienabrufFindByPrimaryKeyOhneExc(Integer iId) {
		return getFac().linienabrufFindByPrimaryKeyOhneExc(iId);
	}

	@Override
	public List<ForecastpositionProduktionDto> getLieferbareForecastpositionByMandant() 
			throws RemoteException, EJBExceptionLP {
		return getFac().getLieferbareForecastpositionByMandant(globalInfo.getTheClientDto());
	}

	@Override
	public List<ForecastpositionProduktionDto> getLieferbareForecastpositionByFclieferadresseIId(
			Integer fcLieferadresseIId, FclieferadresseNoka kommissionierTyp) throws RemoteException, EJBExceptionLP {
		return getFac().getLieferbareForecastpositionByFclieferadresseIId(fcLieferadresseIId, kommissionierTyp, globalInfo.getTheClientDto());
	}

	@Override
	public ForecastpositionProduktionDto getLieferbareForecastpositionByIId(
			Integer forecastpositionIId, boolean withLinienabrufArtikel) throws RemoteException, EJBExceptionLP {
		return getFac().getLieferbareForecastpositionByIId(forecastpositionIId, withLinienabrufArtikel, globalInfo.getTheClientDto());
	}

	@Override
	public void starteLinienabrufProduktion(Integer forecastpositionIId) throws RemoteException {
		getFac().starteLinienabrufProduktion(forecastpositionIId, globalInfo.getTheClientDto());
	}

	@Override
	public LinienabrufArtikelDto produziereLinienabrufArtikel(
			LinienabrufArtikelbuchungDto labDto) throws EJBExceptionLP, RemoteException {
		return getFac().produziereLinienabrufArtikel(labDto, globalInfo.getTheClientDto());
	}

//	@Override
//	public void beendeLinienabrufProduktion(Integer forecastpositionIId) throws RemoteException, EJBExceptionLP {
//		getFac().beendeLinienabrufProduktion(forecastpositionIId, globalInfo.getTheClientDto());
//	}

	@Override
	public List<FclieferadresseNokaDto> getLieferbareFclieferadressenByMandant()
			throws RemoteException, EJBExceptionLP {
		return getFac().getLieferbareFclieferadressenByMandant(globalInfo.getTheClientDto());
	}

	@Override
	public FclieferadresseDto fclieferadresseFindByPrimaryKeyOhneExc(Integer iId) {
		return getFac().fclieferadresseFindByPrimaryKeyOhneExc(iId);
	}

	@Override
	public boolean isLinienabrufProduktionGestartet(Integer forecastpositionIId) {
		return getFac().isLinienabrufProduktionGestartet(forecastpositionIId);
	}

	@Override
	public void bucheZeitAufForecastposition(Integer forecastpositionIId) throws EJBExceptionLP, RemoteException {
		getFac().bucheZeitAufForecastposition(forecastpositionIId, globalInfo.getTheClientDto());
	}
	
//	@Override
//	public LinienabrufArtikelDto produziereMaterialEinerForecastposition(
//			ForecastpositionArtikelbuchungDto fpaDto) throws EJBExceptionLP, RemoteException {
//		return getFac().produziereMaterialEinerForecastposition(fpaDto, globalInfo.getTheClientDto());
//	}

	@Override
	public void createAblieferungLinienabrufProduktion(
			Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp) throws EJBExceptionLP, RemoteException {
		getFac().createAblieferungLinienabrufProduktion(forecastpositionIId, kommissioniertyp, globalInfo.getTheClientDto());
	}
	
	public ForecastpositionProduktionDto produziereMaterial(ForecastpositionArtikelbuchungDto fpaDto) throws EJBExceptionLP, RemoteException {
		return getFac().produziereMaterial(fpaDto, globalInfo.getTheClientDto());
	}
	
	@Override
	public List<KommdruckerDto> getAllKommdrucker() {
		List<KommdruckerDto> printer = new ArrayList<KommdruckerDto>();
		@SuppressWarnings("unchecked")
		Map<Integer, String> printerMap = getFac().getAllKommdrucker(globalInfo.getTheClientDto());
		for (Entry<Integer, String> entry : printerMap.entrySet()) {
			printer.add(kommdruckerFindByPrimaryKey(entry.getKey()));
		}
		return printer;
	}
	
	@Override
	public KommdruckerDto kommdruckerFindByPrimaryKey(Integer id) {
		return getFac().kommdruckerFindByPrimaryKey(id);
	}
}
