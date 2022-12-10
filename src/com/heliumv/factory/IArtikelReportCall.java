package com.heliumv.factory;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.util.EJBExceptionLP;

public interface IArtikelReportCall {

	void printArtikelEtikett(Integer artikelIId, String kommentar, BigDecimal menge, String[] cSnrChnr,
			Integer lagerIIdChargeSnr, String laufendeNr) throws RemoteException, EJBExceptionLP;

	void printArtikelEtikett(Integer artikelIId, String kommentar, BigDecimal menge, String[] cSnrChnr,
			Integer lagerIIdChargeSnr, String laufendeNr, Integer exemplare) throws RemoteException, EJBExceptionLP;

	CustomerPricelistReportDto vkPreislisteRaw(Integer preislisteId, Integer artikelgruppeId, Integer artikelklasseId,
			Integer shopgruppeId, Boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			Boolean mitVersteckten, Date gueltigkeitAb);

}
