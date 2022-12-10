package com.heliumv.factory.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IArtikelReportCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.EJBExceptionLP;

public class ArtikelReportCall extends BaseCall<ArtikelReportFac> implements IArtikelReportCall {

	@Autowired
	private IGlobalInfo globalInfo ;
	
	public ArtikelReportCall() {
		super(ArtikelReportFac.class);
	}

	@Override
	public void printArtikelEtikett(Integer artikelIId, String kommentar, BigDecimal menge, 
			String[] cSnrChnr, Integer lagerIIdChargeSnr, String laufendeNr) throws RemoteException, EJBExceptionLP {
		getFac().printArtikeletikettOnServer(artikelIId, kommentar, menge, cSnrChnr, lagerIIdChargeSnr, laufendeNr, globalInfo.getTheClientDto());
	}

	@Override
	public void printArtikelEtikett(Integer artikelIId, String kommentar, BigDecimal menge, 
			String[] cSnrChnr, Integer lagerIIdChargeSnr, String laufendeNr, Integer exemplare) throws RemoteException, EJBExceptionLP {
		getFac().printArtikeletikettOnServer(artikelIId, kommentar, menge, cSnrChnr, lagerIIdChargeSnr, laufendeNr, exemplare, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_ARTIKEL)
	@HvJudge(rechtOder= {RechteFac.RECHT_WW_ARTIKEL_R, RechteFac.RECHT_WW_ARTIKEL_CUD})
	public CustomerPricelistReportDto vkPreislisteRaw(Integer preislisteId,
			Integer artikelgruppeId, Integer artikelklasseId, Integer shopgruppeId,
			Boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			Boolean mitVersteckten, java.sql.Date gueltigkeitAb) {
		return getFac().printVkPreislisteRaw(preislisteId, artikelgruppeId, 
				artikelklasseId, shopgruppeId, bMitInaktiven, artikelNrVon, 
				artikelNrBis, mitVersteckten, gueltigkeitAb, globalInfo.getTheClientDto());
	}
}
