package com.heliumv.api.item;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IFehlmengeCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.IReservierungCall;

public class StockAmountInfoEntryMapper {

	@Autowired
	private ILagerCall lagerCall ;
	@Autowired
	private IReservierungCall reservierungCall ;
	@Autowired
	private IFehlmengeCall fehlmengeCall ;

	public StockAmountInfoEntry mapEntry(Integer artikelIId) {
		StockAmountInfoEntry infoEntry = new StockAmountInfoEntry() ;
		try {
			infoEntry.setStockAmount(lagerCall.getLagerstandAllerLagerEinesMandanten(artikelIId, false));
			infoEntry.setReservedAmount(reservierungCall.getAnzahlReservierungen(artikelIId));
			infoEntry.setMissingAmount(fehlmengeCall.getAnzahlFehlmengeEinesArtikels(artikelIId));
			infoEntry.setAvailableAmount(infoEntry.getStockAmount().subtract(infoEntry.getReservedAmount())
					.subtract(infoEntry.getMissingAmount())) ;
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		return infoEntry;
	}
	
	public StockAmountInfoEntry mapEntry(Integer artikelIId, Integer lagerIId) {
		StockAmountInfoEntry infoEntry = new StockAmountInfoEntry() ;
		try {
			infoEntry.setStockAmount(lagerCall.getLagerstandOhneExc(artikelIId, lagerIId));
			infoEntry.setReservedAmount(reservierungCall.getAnzahlReservierungen(artikelIId));
			infoEntry.setMissingAmount(fehlmengeCall.getAnzahlFehlmengeEinesArtikels(artikelIId));
			infoEntry.setAvailableAmount(infoEntry.getStockAmount().subtract(infoEntry.getReservedAmount())
					.subtract(infoEntry.getMissingAmount())) ;
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		return infoEntry;
	}
}
