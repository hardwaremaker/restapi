package com.heliumv.factory;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public interface IFertigungReportCall {

	JasperPrintLP printFertigungsbegleitschein(Integer losIId, Boolean bStammtVonSchnellanlage)  throws RemoteException;
	
	JasperPrintLP printLosverpackungsetiketten(Integer losIId, Integer iAnzahl)
			throws RemoteException;
	
	JasperPrintLP printLosetikett(Integer losIId, BigDecimal bdMenge, Integer iExemplare, 
			String cReportnamevariante) throws EJBExceptionLP, RemoteException;
	
	JasperPrintLP printVersandetikettVorbereitung(Integer losIId);

	JasperPrintLP printVersandetikettVorbereitungForecast(Integer forecastpositionIId);

	void printAblieferEtikett(Integer losablieferungIId, BigDecimal menge);

	JasperPrintLP printFertigungsbegleitschein(Integer losId, String printerName);

	JasperPrintLP printAusgabeListe(Integer losId, Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId, String alternativerReport,
			String printerName);

	JasperPrintLP printBedarfsuebernahmeSynchronisierungOnServer();

	JasperPrintLP printBedarfsuebernahmeSynchronisierungOnServer(Integer personalIId);

	JasperPrintLP printLosEtikettOnServer(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, String printerName) throws RemoteException, EJBExceptionLP;
}
