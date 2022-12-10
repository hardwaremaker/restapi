package com.heliumv.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lp.server.util.Validator;
import com.lp.service.BelegpositionVerkaufDto;

public class BelegpositionVerkaufHelper {
	
	/**
	 * Eine Liste von BelegpositionVerkaufto so filtern, 
	 * dass nur noch jene Positionen mit dem gesuchten
	 * Artikel enthalten sind.</br>
	 * 
	 * @param <T extends BelegpositionVerkaufDto>
	 * @param elements die Liste der Positionen
	 * @param itemId die gesuchte ArtikelId
	 * @return eine (leere) Liste aller Positionen die 
	 * den gesuchten Artikel enthalten.
	 */
	public static <T extends BelegpositionVerkaufDto> List<T> 
		filterItemId(List<T> elements, Integer itemId) {
		Validator.notNull(itemId, "itemId");
/*
		return elements.stream()
				.filter(element -> itemId.equals(element.getArtikelIId()))
				.collect(Collectors.toList());
*/
		List<T> result = new ArrayList<T>();
		for (T element : elements) {
			if(itemId.equals(element.getArtikelIId())) {
				result.add(element);
			}
		}
		return result;
	}
}
