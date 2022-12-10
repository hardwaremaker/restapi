package com.heliumv.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IKundeCall;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.util.Validator;

public class AuftragItemPriceCalculator extends ItemPriceCalculator {
	@Autowired
	private IKundeCall kundeCall ;

	private AuftragDto abDto ;
	
	public AuftragItemPriceCalculator() {
	}
	
	public AuftragItemPriceCalculator(IItemPriceCalculatorValidation validator) {
		super(validator) ;
	}
	
	public AuftragItemPriceCalculator(IItemPriceCalculatorValidation validator, AuftragDto lsDto) {
		super(validator) ;
		setAuftragDto(abDto); 
	}
	
	@Override
	public ItemPriceCalculationResult calculate(BigDecimal amount)
			throws RemoteException {
		getValidator().setKundeDto(
				kundeCall.kundeFindByPrimaryKeyOhneExc(
						getAuftragDto().getKundeIIdRechnungsadresse())) ;
		return super.calculate(amount);
	}
	
	public void setAuftragDto(AuftragDto abDto) {
		Validator.notNull(abDto, "abDto");

		this.abDto = abDto ;
		setWaehrungCnr(abDto.getCAuftragswaehrung());
		setTimestamp(abDto.getTBelegdatum());
	}
	
	public AuftragDto getAuftragDto() {
		return abDto ;
	}
}
