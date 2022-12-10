package com.heliumv.bl;

import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.util.EJBExceptionLP;

public interface IBusinessLogicValidation {
	void addFailedValidation(EJBExceptionLP e) throws ExceptionLP ; 
	void clearValidations() ;
	List<EJBExceptionLP> getValidations() ;
	boolean hasFailedValidations() ;
}
