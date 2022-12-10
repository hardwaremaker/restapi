package com.heliumv.api;

import com.heliumv.tools.StringHelper;

public class HvValidateBadRequest {
	/**
	 * L&ouml;st eine BadRequest Antwort aus, wenn value == null
	 * @param value ist der auf null zu pr&uuml;fende Wert
	 * @param key ist Param-Key der als fehlend dokumentiert wird
	 */
	public static void notNull(Object value, String key) {
		if(value == null) {
			throw new HvValidationValueMissing(key) ;
		}
	}

	/**
	 * L&ouml;st eine Badrequest Antwort aus, wenn value entweder null oder leer ist 
	 * @param value ist der auf null bzw. "leer" zu pr&uuml;fende Wert
	 * @param key ist der Param-Key der als fehlend dokumentiert wird
	 */
	public static void notEmpty(String value, String key) {
		if(StringHelper.isEmpty(value)) {
			throw new HvValidationValueMissing(key);			
		}
	}

	/**
	 * L&ouml;st eine Badrequest Antwort aus, wenn die Bedingung false ist 
	 * 
	 * @param isValid ist die Bedingung die bei false einen Badrequest ausl&ouml;st
	 * @param key der Param-Key der als fehlend/leer dokumentiert wird
	 */
	public static void notValid(boolean isValid, String key) {
		if(!isValid) {
			throw new HvValidationValueIsInvalid(key);			
		}
	}

	/**
	 * L&ouml;st eine Badrequest Antwort aus, wenn die Bedingung false ist 
	 * 
	 * @param isValid ist die Bedingung die bei false einen Badrequest ausl&ouml;st
	 * @param key der Param-Key der als fehlend/leer dokumentiert wird
	 * @param value der Param-Value der als fehlerhaft ausgegeben werden soll 
	 */
	public static void notValid(boolean isValid, String key, String value) {
		if(!isValid) {
			throw new HvValidationValueIsInvalid(key, value);			
		}
	}
}
