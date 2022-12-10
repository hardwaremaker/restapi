package com.heliumv.factory;

import java.rmi.RemoteException;
import java.sql.Date;

import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.system.jcr.service.JCRDocDto;

public interface IEingangsrechnungCall {

	EingangsrechnungDto eingangsrechnungFindByPrimaryKeyOhneExc(
			Integer eingangsrechnungIId) throws RemoteException;

	EingangsrechnungDto eingangsrechnungFindByCNrMandantCNr(String cnr);

	Date getDefaultFreigabeDatum() throws RemoteException;

	EingangsrechnungDto createEingangsrechnung(EingangsrechnungDto erDto) throws RemoteException;

	EingangsrechnungAuftragszuordnungDto createEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto zuordnungDto) throws RemoteException;

	EingangsrechnungDto createEingangsrechnungMitDokument(
			EingangsrechnungDto erDto, Integer auftragId,
			JCRDocDto jcrDto) throws RemoteException;
}
