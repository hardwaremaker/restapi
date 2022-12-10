package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILieferscheinReportCall;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.PrintLieferscheinAttribute;
import com.lp.server.util.LieferscheinId;
import com.lp.server.util.report.JasperPrintLP;

public class LieferscheinReportCall extends BaseCall<LieferscheinReportFac> implements ILieferscheinReportCall {
	@Autowired
	private IGlobalInfo globalInfo ;
		
	public LieferscheinReportCall() {
		super(LieferscheinReportFac.class);
	}
	
	public void printLieferscheinOnServer(Integer deliveryId) throws RemoteException {
		getFac().printLieferscheinOnServer(deliveryId, globalInfo.getTheClientDto()) ;
	}

	@Override
	public void printVersandetikett(Integer deliveryId) throws RemoteException {
		getFac().printVersandetikettOnServer(deliveryId, globalInfo.getTheClientDto());
	}

	@Override
	public void printLieferscheinWAEtikett(Integer deliveryId) throws RemoteException {
		getFac().printLieferscheinWAEtikettOnServer(deliveryId, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP[] printLieferschein(Integer deliveryId) throws RemoteException {
		PrintLieferscheinAttribute attributs = new PrintLieferscheinAttribute(
				new LieferscheinId(deliveryId))
				.setMitLogo(true);
		return getFac().printLieferschein(attributs, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP[] printLieferschein(Integer deliveryId, 
			Integer varianteId, Integer serialnumber) throws RemoteException {
		Integer oldVarianteId = globalInfo
				.getTheClientDto().getReportvarianteIId();
		try {
			globalInfo.getTheClientDto().setReportvarianteIId(varianteId);
			PrintLieferscheinAttribute attributs = new PrintLieferscheinAttribute(
					new LieferscheinId(deliveryId))
					.setMitLogo(true)
					.setSerialNr(serialnumber);
			return getFac().printLieferschein(attributs, globalInfo.getTheClientDto());
		} finally {
			globalInfo.getTheClientDto().setReportvarianteIId(oldVarianteId);
		}
	}
}
