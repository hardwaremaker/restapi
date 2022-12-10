package com.heliumv.factory;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.bestellung.service.RueckgabeWEPMitReelIDDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;

public interface IWareneingangCall {
	WareneingangDto[] wareneingangFindByBestellungIId(Integer bestellungId) throws RemoteException;
	Integer createWareneingang(WareneingangDto wareneingangDto) throws RemoteException;
	WareneingangDto wareneingangFindByPrimaryKeyOhneExc(Integer wareneingangId) throws RemoteException;
	WareneingangspositionDto[] wareneingangspositionFindByWareneingangIId(Integer wareneingangId)
			throws RemoteException;
	List<WareneingangDto> wareneingangFindByLieferscheinnummer(
			Integer bestellungId, String lieferscheinCnr) throws RemoteException;
	Integer createWareneingangsposition(WareneingangspositionDto posDto) throws RemoteException;
	WareneingangspositionDto wareneingangspositionFindByPrimaryKey(Integer positionId) throws RemoteException;
	WareneingangspositionDto[] wareneingangspositionFindByBestellpositionIId(Integer bestellpositionId)
			throws RemoteException;
	void updateWareneingangsposition(WareneingangspositionDto positionDto, boolean setartikelAufloesen)
			throws RemoteException;
	RueckgabeWEPMitReelIDDto wareneingangspositionMitReelIDBuchen(Integer wareneingangIId, Integer bestellpositionIId,
			BigDecimal nMenge, String datecode, String expirationDate);
}
