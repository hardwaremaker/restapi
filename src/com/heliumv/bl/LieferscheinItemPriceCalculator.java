package com.heliumv.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IKundeCall;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.util.Validator;

public class LieferscheinItemPriceCalculator extends ItemPriceCalculator {
	@Autowired
	private IKundeCall kundeCall ;

	private LieferscheinDto lsDto ;
	
	public LieferscheinItemPriceCalculator() {
	}
	
	public LieferscheinItemPriceCalculator(IItemPriceCalculatorValidation validator) {
		super(validator) ;
	}
	
	public LieferscheinItemPriceCalculator(IItemPriceCalculatorValidation validator, LieferscheinDto lsDto) {
		super(validator) ;
		setLieferscheinDto(lsDto); 
	}
	
	@Override
	public ItemPriceCalculationResult calculate(BigDecimal amount)
			throws RemoteException {
		getValidator().setKundeDto(
				kundeCall.kundeFindByPrimaryKeyOhneExc(
						getLieferscheinDto().getKundeIIdLieferadresse())) ;
		return super.calculate(amount);
	}
	
	public void setLieferscheinDto(LieferscheinDto lsDto) {
		Validator.notNull(lsDto, "lsDto");

		this.lsDto = lsDto ;
		setWaehrungCnr(lsDto.getWaehrungCNr());
		setTimestamp(lsDto.getTBelegdatum());
	}
	
	public LieferscheinDto getLieferscheinDto() {
		return lsDto ;
	}
}
