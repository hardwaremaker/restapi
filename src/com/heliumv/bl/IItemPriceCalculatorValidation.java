package com.heliumv.bl;

import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;


public interface IItemPriceCalculatorValidation  extends IBusinessLogicValidation {
	void setKundeDto(KundeDto kundeDto) ;
	KundeDto getKundeDto() ;
	
	void setArtikelDto(ArtikelDto artikelDto) ;
	ArtikelDto getArtikelDto() ;
	
	boolean isValidKundenPreislisteId(Integer preislisteId) throws RemoteException  ;
}
