package com.heliumv.api;

public class HvValidateNotFound {
	public static void notNull(Object value, String key, Integer valueId) {
		if(value == null) {
			throw new HvValidationNotFound(key, valueId) ;
		}
	}

	public static void notNull(Object value, String key, String valueCnr) {
		if(value == null) {
			throw new HvValidationNotFound(key, valueCnr) ;
		}
	}
	
	/**
	 * L&ouml;st eine NOT-FOUND Antwort aus, wenn die Bedingung false ist 
	 * 
	 * @param isValid ist die Bedingung die bei false einen Badrequest ausl&ouml;st
	 * @param key der Param-Key der als fehlend/leer dokumentiert wird
	 * @param value der Param-Value der als fehlerhaft ausgegeben werden soll 
	 */
	public static void notValid(boolean isValid, String key, String value) {
		if(!isValid) {
			throw new HvValidationNotFound(key, value);			
		}
	}

}
