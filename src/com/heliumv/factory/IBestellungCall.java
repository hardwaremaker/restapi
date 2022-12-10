package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.bestellung.service.BestellungDto;

public interface IBestellungCall {
	BestellungDto bestellungFindByCNrMandantCNr(String cnr);

	BestellungDto bestellungFindByCNrMandantCNr(String cnr, String mandantCnr);

	BestellungDto bestellungFindByPrimaryKeyOhneExc(Integer id);
	
	List<BestellungDto> bestellungFindByLieferantIIdBestelladresseMandantCNrFilter(
			Integer lieferantId, boolean receiptPossible) throws RemoteException;
}
