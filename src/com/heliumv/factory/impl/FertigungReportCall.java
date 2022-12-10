package com.heliumv.factory.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IFertigungReportCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public class FertigungReportCall extends BaseCall<FertigungReportFac> implements IFertigungReportCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	protected FertigungReportCall() {
		super(FertigungReportFac.class);
	}

	@Override
	public JasperPrintLP printFertigungsbegleitschein(Integer losIId, Boolean bStammtVonSchnellanlage) throws RemoteException {
		return getFac().printFertigungsbegleitschein(losIId, bStammtVonSchnellanlage, globalInfo.getTheClientDto());
	}

	@Override
	public JasperPrintLP printLosverpackungsetiketten(Integer losIId, Integer iAnzahl)
			throws RemoteException {
		return getFac().printLosVerpackungsetiketten(losIId, null, iAnzahl, globalInfo.getTheClientDto());
	}
	
	public JasperPrintLP printLosetikett(Integer losIId, BigDecimal bdMenge, Integer iExemplare, 
			String cReportnamevariante) throws EJBExceptionLP, RemoteException {
		if (cReportnamevariante != null) {
			return getFac().printLosEtikett(losIId, bdMenge, "", false, iExemplare, cReportnamevariante, globalInfo.getTheClientDto());
		} else {
			return getFac().printLosEtikett(losIId, bdMenge, "", false, iExemplare, globalInfo.getTheClientDto());
		}
	}

	@Override
	public JasperPrintLP printVersandetikettVorbereitung(Integer losIId) {
		return getFac().printVersandetikettVorbereitung(losIId, globalInfo.getTheClientDto());
	}

	@Override
	public JasperPrintLP printVersandetikettVorbereitungForecast(Integer forecastpositionIId) {
		return getFac().printVersandetikettVorbereitungForecast(forecastpositionIId, globalInfo.getTheClientDto());
	}
	
	@Override
	public void printAblieferEtikett(Integer losablieferungIId, BigDecimal menge) {
		getFac().printAblieferEtikettOnServer(losablieferungIId, menge, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP printFertigungsbegleitschein(Integer losId, String printerName) {
		return getFac().printFertigungsbegleitscheinOnServer(losId, printerName, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP printAusgabeListe(Integer losId,
			Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId,
			String alternativerReport, String printerName) {
		return getFac().printAusgabeListeOnServer(losId, iSortierung, 
				bVerdichtetNachIdent, bVorrangigNachFarbcodeSortiert, 
				artikelklasseIId, alternativerReport, printerName,
				globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP printBedarfsuebernahmeSynchronisierungOnServer() {
		return printBedarfsuebernahmeSynchronisierungOnServer(globalInfo.getTheClientDto().getIDPersonal());
	}

	@Override
	public JasperPrintLP printBedarfsuebernahmeSynchronisierungOnServer(Integer personalIId) {
		return getFac().printBedarfsuebernahmeSynchronisierungOnServer(
				personalIId, true, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP printLosEtikettOnServer(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare, String printerName) throws RemoteException, EJBExceptionLP {
		return getFac().printLosEtikettOnServer(losIId, bdMenge, sKommentar, bMitInhalten, iExemplare, printerName, globalInfo.getTheClientDto());
	}
}
