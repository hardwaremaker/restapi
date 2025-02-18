package com.heliumv.factory.legacy;

import com.heliumv.tools.StringHelper;

public class AllArtikelgruppeEntry {
	private String key ;
	private String description ;
	
	public AllArtikelgruppeEntry(String key, String description) {
		setKey(key) ;
		setDescription(description);  
	}	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Die Artikelgruppen-Id</br>
	 * <p>Achtung: Die Id ist in der Form "(id,id,...)" gespeichert,
	 * wobei die erste Id die Id dieser Gruppe ist, alle nachfolgenden
	 * sind die Ids der nächsten Ebene der Untergruppen</p>
	 * @return der ArtikelgruppenKey
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public Integer getId() {
		if(StringHelper.isEmpty(key)) return null ;

		if((key.charAt(0) == '(') && key.charAt(key.length() - 1) == ')') {
			String keys = key.substring(1, key.length() - 1);
			String[] tokens = keys.split(",");
			if(tokens.length != 0) {
				return Integer.parseInt(tokens[0]);				
			}
//			return Integer.parseInt(key.substring(1, key.length() - 1)) ;
		}
		
		throw new IllegalArgumentException("key should be '(key)', but is '" + key + "'.") ;
	}
}
