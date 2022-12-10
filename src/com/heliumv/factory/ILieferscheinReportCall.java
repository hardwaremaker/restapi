package com.heliumv.factory;

import java.rmi.RemoteException;

import com.lp.server.util.report.JasperPrintLP;

public interface ILieferscheinReportCall {
	void printLieferscheinOnServer(Integer deliveryId) throws RemoteException ;
	
	void printVersandetikett(Integer deliveryId) throws RemoteException;
	
	void printLieferscheinWAEtikett(Integer deliveryId) throws RemoteException;

	JasperPrintLP[] printLieferschein(Integer deliveryId) throws RemoteException;

	JasperPrintLP[] printLieferschein(Integer deliveryId, Integer varianteId, Integer serialnumber)
			throws RemoteException;
}
