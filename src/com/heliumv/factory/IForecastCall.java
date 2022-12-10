package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.FclieferadresseNoka;
import com.lp.server.forecast.service.FclieferadresseNokaDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionArtikelbuchungDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ForecastpositionProduktionDto;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.forecast.service.LinienabrufArtikelDto;
import com.lp.server.forecast.service.LinienabrufArtikelbuchungDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.util.EJBExceptionLP;

public interface IForecastCall {

	ForecastauftragDto forecastauftragFindByPrimaryKey(Integer iId);

	ForecastpositionDto forecastpositionFindByPrimaryKey(Integer forecastpositionid);
	
	LinienabrufDto linienabrufFindByPrimaryKeyOhneExc(Integer iId);

	List<ForecastpositionProduktionDto> getLieferbareForecastpositionByMandant() throws RemoteException, EJBExceptionLP;
	
	List<ForecastpositionProduktionDto> getLieferbareForecastpositionByFclieferadresseIId(
			Integer fcLieferadresseIId, FclieferadresseNoka kommissionierTyp) throws RemoteException, EJBExceptionLP;
	
	ForecastpositionProduktionDto getLieferbareForecastpositionByIId(
			Integer forecastpositionIId, boolean withLinienabrufArtikel) 
					throws RemoteException, EJBExceptionLP;
	
	void starteLinienabrufProduktion(Integer forecastpositionIId) throws RemoteException;
	
	LinienabrufArtikelDto produziereLinienabrufArtikel(LinienabrufArtikelbuchungDto labDto) 
			throws EJBExceptionLP, RemoteException;
	
	List<FclieferadresseNokaDto> getLieferbareFclieferadressenByMandant() 
			throws RemoteException, EJBExceptionLP;
	
	FclieferadresseDto fclieferadresseFindByPrimaryKeyOhneExc(Integer iId);
	
	boolean isLinienabrufProduktionGestartet(Integer forecastpositionIId);
	
	void bucheZeitAufForecastposition(Integer forecastpositionIId) throws EJBExceptionLP, RemoteException;
	
	void createAblieferungLinienabrufProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp) throws EJBExceptionLP, RemoteException;
	
	ForecastpositionProduktionDto produziereMaterial(ForecastpositionArtikelbuchungDto fpaDto) throws EJBExceptionLP, RemoteException;

	List<KommdruckerDto> getAllKommdrucker();

	KommdruckerDto kommdruckerFindByPrimaryKey(Integer id);
}
