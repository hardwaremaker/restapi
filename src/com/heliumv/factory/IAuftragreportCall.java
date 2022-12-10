package com.heliumv.factory;

import java.rmi.RemoteException;

import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public interface IAuftragreportCall {
	JasperPrintLP printKommissionierung(Integer iIdAuftragI);
	JasperPrintLP printAuftragPackliste(Integer iIdAuftragI) throws RemoteException, EJBExceptionLP;
	JasperPrintLP printAuftragszeiten(Integer iIdAuftragI, 
			boolean bSortiertNachPerson) throws RemoteException;
	JasperPrintLP printZeitbestaetigung(Integer auftragId, boolean mitBereitsUnterschriebenen) throws RemoteException;
	JasperPrintLP printZeitbestaetigung(Integer auftragId, boolean mitBereitsUnterschriebenen, Integer varianteId,
			Integer serialNumber) throws RemoteException;
}
