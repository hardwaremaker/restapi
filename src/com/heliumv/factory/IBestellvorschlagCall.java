package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.CreateBestellvorschlagDto;
import com.lp.util.EJBExceptionLP;

public interface IBestellvorschlagCall {

	Integer createBestellvorschlag(CreateBestellvorschlagDto createDto);

	List<BestellvorschlagDto> bestellvorschlagFindByArtikelIdVormerkungMandantCNr(Integer artikelIId);

	void pruefeBearbeitenDesBestellvorschlagsErlaubt() throws EJBExceptionLP, RemoteException;

	void removeLockDesBestellvorschlagesWennIchIhnSperre() throws EJBExceptionLP, RemoteException;

	BestellvorschlagDto bestellvorschlagFindByPrimaryKeyOhneExc(Integer bestellvorschlagIId);

	void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws EJBExceptionLP, RemoteException;

	void removeBestellvorschlag(Integer bestellvorschlagIId) throws EJBExceptionLP, RemoteException;

}
