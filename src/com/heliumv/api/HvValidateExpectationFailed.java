package com.heliumv.api;

import java.util.Collection;

import com.lp.server.artikel.service.ArtikelDto;


public class HvValidateExpectationFailed {

	/**
	 * L&ouml;st eine ExpectionFailed Antwort mit EJB-Fehlercode <code>EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN</code> 
	 * aus, wenn das Array null oder leer ist 
	 * @param positions ist der auf null bzw. "leer" zu pr&uuml;fende Wert
	 */
	public static void noPositions(Object[] positions) {
		if(positions == null || positions.length < 1) {
			throw new HvValidationNoPositions();
		}
	}
	
	/**
	 * L&ouml;st eine ExpectionFailed Antwort mit EJB-Fehlercode <code>EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN</code> 
	 * aus, wenn die Collection leer ist 
	 * @param positions ist der auf "leer" zu pr&uuml;fende Wert
	 */
	public static void noPositions(Collection<?> positions) {
		if (positions.isEmpty()) {
			throw new HvValidationNoPositions();
		}
	}
	
	/**
	 * L&ouml;st eine ExpectationFailed Antwort aus, wenn es sich um keinen
	 * Identartikel handelt, der lagerbewirtschaftet ist.
	 * 
	 * @param artikelDto muss ein Identartikel und lagerbewirtschaftet sein
	 */
	public static void identItemRequired(ArtikelDto artikelDto) {
		if (artikelDto == null || !artikelDto.isLagerbewirtschaftet()) {
			throw new HvValidationValueIsInvalid(artikelDto.getArtgruIId().toString());
		}
	}
}
