package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IAuftragreportCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public class AuftragreportCall extends BaseCall<AuftragReportFac> implements IAuftragreportCall {
	@Autowired
	private IGlobalInfo globalInfo;
	
	public AuftragreportCall() {
		super(AuftragReportFac.class);
	}

	@Override
	public JasperPrintLP printAuftragPackliste(Integer iIdAuftragI)
			throws RemoteException, EJBExceptionLP {
		return getFac().printAuftragPackliste(iIdAuftragI,
				AuftragReportFac.REPORT_AUFTRAG_PACKLISTE,
				globalInfo.getTheClientDto());
	}

	@Override
	public JasperPrintLP printKommissionierung(Integer iIdAuftragI) {
		return getFac().printKommissionierung(
				iIdAuftragI, globalInfo.getTheClientDto());
	}

	@Override
	public JasperPrintLP printAuftragszeiten(Integer iIdAuftragI, boolean bSortiertNachPerson) throws RemoteException {
		return getFac().printAuftragszeiten(iIdAuftragI, bSortiertNachPerson, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP printZeitbestaetigung(Integer auftragId,
			boolean mitBereitsUnterschriebenen) throws RemoteException {
		return getFac().printZeitbestaetigung(
				globalInfo.getTheClientDto().getIDPersonal(), auftragId,
				null, mitBereitsUnterschriebenen, null, globalInfo.getTheClientDto());
	}
	
	@Override
	public JasperPrintLP printZeitbestaetigung(Integer auftragId,
			boolean mitBereitsUnterschriebenen, Integer varianteId, Integer serialNumber) throws RemoteException {
		Integer oldVarianteId = globalInfo.getTheClientDto().getReportvarianteIId();
		try {
			globalInfo.getTheClientDto().setReportvarianteIId(varianteId);
			return getFac().printZeitbestaetigung(
					globalInfo.getTheClientDto().getIDPersonal(), auftragId,
					null, mitBereitsUnterschriebenen, serialNumber, globalInfo.getTheClientDto());
		} finally {
			globalInfo.getTheClientDto().setReportvarianteIId(oldVarianteId);
		}
	}
}
