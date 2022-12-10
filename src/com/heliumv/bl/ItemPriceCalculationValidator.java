package com.heliumv.bl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.impl.VkPreisfindungCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.EJBExceptionLP;



public class ItemPriceCalculationValidator extends BusinessLogicValidator implements
		IItemPriceCalculatorValidation {
	@Autowired
	private VkPreisfindungCall vkPreisfindungCall ;

	private KundeDto kundeDto ;
	private ArtikelDto artikelDto ;

	@Override
	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto ;
	}
	
	@Override
	public KundeDto getKundeDto() {
		return kundeDto ;
	}

	@Override
	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto ; 		
	}
	
	@Override
	public ArtikelDto getArtikelDto() {
		return artikelDto ;
	}
	
	@Override	
	public boolean isValidKundenPreislisteId(Integer preislisteId) throws RemoteException {
		if(preislisteId == null) {
			addFailedValidation(
					new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KUNDE_HAT_KEINE_STANDARDPREISLISTE_HINTERLEGT,
							new Exception("kundeStandardpreisliste == null")));
			return false ;
		}
		
		String vkpfWaehrung = vkPreisfindungCall
				.vkpfartikelpreislisteFindByPrimaryKey(preislisteId)
				.getWaehrungCNr();
		if(kundeDto.getCWaehrung().compareTo(vkpfWaehrung) != 0) {
			addFailedValidation(
					new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KUNDE_STANDARDPREISLISTE_HAT_FALSCHE_WAEHRUNG,
							new Exception(
									"kundeStandardpreisliste falsche Waehrung ("
											+ vkpfWaehrung + ")")));
			return false ;
		}
		
		return true ;
	}
}
