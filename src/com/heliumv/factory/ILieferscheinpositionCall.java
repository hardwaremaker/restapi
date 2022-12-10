package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;

public interface ILieferscheinpositionCall {
	Integer createLieferscheinposition(
			LieferscheinpositionDto lsposDto,
			boolean bArtikelSetAufloesen,
			List<SeriennrChargennrMitMengeDto> identities) throws RemoteException;
	
	Integer createLieferscheinpositionVerteilen(
			LieferscheinpositionDto lsposDto, 
			boolean bArtikelSetAufloesen, 
			List<SeriennrChargennrMitMengeDto> identities) throws RemoteException;

	Integer updateLieferscheinposition(LieferscheinpositionDto lsposDto, 
			List<SeriennrChargennrMitMengeDto> identities) throws RemoteException;

	/**
	 * Die angegebene Lieferscheinposition ermitteln.
	 * 
	 * @param positionId
	 * @return null wenn nicht vorhanden, ansonsten die LieferscheinpositionId
	 * @throws RemoteException
	 */
	LieferscheinpositionDto lieferscheinpositionFindByPrimaryKey(Integer positionId) throws RemoteException ;
	
	/**
	 * Eine bestehende Lieferscheinposition l&ouml;schen
	 * 
	 * @param positionId die zu l&ouml;schende Lieferscheinposition
	 * @throws RemoteException
	 */
	void removeLieferscheinposition(Integer positionId) throws RemoteException ;
	
	LieferscheinpositionDto[] lieferscheinpositionFindByAuftragpositionIId(
			Integer orderpositionId) throws RemoteException ;

	LieferscheinpositionDto[] getLieferscheinPositionenByLieferschein(Integer lieferscheinIId) throws RemoteException;

	void sortiereNachArtikelnummer(Integer lieferscheinId) throws RemoteException;
	void sortiereNachAuftragsnummer(Integer lieferscheinId) throws RemoteException;
}
