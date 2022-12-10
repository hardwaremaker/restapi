/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.factory.legacy.AllArtikelgruppeEntry;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelMitVerpackungsgroessenDto;
import com.lp.server.artikel.service.ArtikelsperrenSperrenDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.ShopgruppeDto;

public interface IArtikelCall {
	/**
	 * Liefert den angeforderten ARtikel unter Beruecksichtigung des Zentralen Artikelstamm
	 * @param cNr die gewuenschte Artikelnummer
	 * @return null wenn nicht existiert, ansonsten den Artikel
	 */
	ArtikelDto artikelFindByCNrOhneExc(String cNr) throws RemoteException ;
	
	ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer itemId) throws RemoteException  ;

	/**
	 * Einen Artikel anhand seiner EAN finden
	 * @param ean die zu suchende "EAN"
	 * @return den Artikel mit dieser EAN
	 * @throws RemoteException
	 */
	ArtikelMitVerpackungsgroessenDto artikelFindByEanOhneExc(String ean) throws RemoteException ;
	
	ArtgruDto artikelgruppeFindByPrimaryKeyOhneExc(Integer artikelgruppeId) throws RemoteException ;
	
	ArtgruDto artikelgruppeFindByCnrOhneExc(String artikelgruppeCnr) throws RemoteException ;	

	List<ArtgruDto> artikelgruppeFindByMandantCNr() throws RemoteException ;
	
	ArtklaDto artikelklasseFindByPrimaryKeyOhneExc(Integer artikelklasseId) throws RemoteException ;
	
	ArtklaDto artikelklasseFindByCnrOhneExc(String artikelklasseCnr) throws RemoteException ;
	
	/**
	 * Eine Liste aller ArtikelgruppenSpr f&uuml;r den aktuellen Mandanten
	 * 
	 * @return eine (leere) Liste von Artikelgruppen
	 * @throws NamingException
	 * @throws RemoteException
	 */
	List<AllArtikelgruppeEntry> getAllArtikelgruppeSpr() throws RemoteException ;
	
	HerstellerDto herstellerFindByPrimaryKey(Integer herstellerId) ;
	
	ArtikelsprDto artikelSprFindByArtikelIIdOhneExc(Integer artikelIId) throws RemoteException;
	
	ArtikelDto artikelFindByPrimaryKeySmall(Integer artikelIId);

	ArtikelDto artikelFindByArtikelnrlieferant(String cnr, Integer lieferantId) throws RemoteException;

	List<ArtikelDto> artikelFindByArtikelnrhersteller(String cnr) throws RemoteException;

	/**
	 * Eine Liste aller Artikelsperren (inklusive SperrenDto) dieses Artikels
	 * @param artikelId
	 * @return eine (leere) Liste aller Artikelsperren des Artikels
	 * @throws RemoteException
	 */
	List<ArtikelsperrenSperrenDto> artikelsperrenSperrenFindByArtikelIId(Integer artikelId) throws RemoteException;

	List<Integer> getEingeschraenkteArtikelgruppen();

	ArtikelDto artikelFindByPrimaryKeyOhneExc(Integer itemId) throws RemoteException;

	List<ArtikelDto> artikelFindByHerstellernummerausBarcode(String herstellerBarcode);

	ShopgruppeDto shopgruppeFindByCNrMandantOhneExc(String cnr);
	ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId);
	ShopgruppeDto shopgruppeFindByPrimaryKeyOhneExc(Integer iId);
}
