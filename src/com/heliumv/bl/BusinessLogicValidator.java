package com.heliumv.bl;

import java.util.ArrayList;
import java.util.List;

import com.lp.util.EJBExceptionLP;

public class BusinessLogicValidator implements IBusinessLogicValidation {
	private List<EJBExceptionLP> validations ;
	private boolean validationThrows ;

	public BusinessLogicValidator() {
		this(true) ;
	}

	public BusinessLogicValidator(boolean validationThrows) {
		this.validationThrows = validationThrows ;
		clearValidations() ;
	}
	
	/**
	 * Soll eine fehlerhafte Pr&uuml;fung zu einer Exception f&uuml;hren?
	 * 
	 * @param enable true wenn eine fehlgeschlagene Pr&uuml;fung der Daten zu einer
	 *  Exception f&uuml;hren soll
	 */
	public void setValidationThrowsException(boolean enable) {
		validationThrows = enable ;
	}


	/**
	 * Nur Validieren, keine Exceptions.</br>
	 * <p>Die Fehlgeschlagenenen Pr&uuml;fungen k&ouml;nnen mittels {@link #getValidations()}
	 * ermittelt werden</p>
	 */
	public void beValidationOnly() {
		validationThrows = false ;
	}


	@Override
	public void addFailedValidation(EJBExceptionLP e) {
		validations.add(e) ;
		if(validationThrows) throw e ;
	}

	@Override
	public void clearValidations() {
		validations = new ArrayList<EJBExceptionLP>() ;
	}

	@Override
	public List<EJBExceptionLP> getValidations() {
		return validations ;
	}

	@Override
	public boolean hasFailedValidations() {
		return validations.size() > 0 ;
	}
}
