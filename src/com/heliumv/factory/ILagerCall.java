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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.factory.legacy.AllLagerEntry;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.LagerplatzInfoDto;
import com.lp.server.artikel.service.LagerstandInfoDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.util.EJBExceptionLP;

public interface ILagerCall {
	/**
	 * Sucht im Lager nach der Seriennummer. Zuerst im Zugang, dann im Abgang
	 * 
	 * @param serialnumber ist die gesuchte Eineindeutige Seriennummer
	 * @return null wenn es die seriennummer nicht gibt, ansonsten die Artikel-IId
	 * @throws RemoteException
	 */
	Integer artikelIdFindBySeriennummerOhneExc(String serialnumber) throws RemoteException ;	

	BigDecimal getGemittelterGestehungspreisEinesLagers(
			Integer itemId, Integer lagerId) throws RemoteException;

	LagerDto lagerFindByPrimaryKeyOhneExc(Integer lagerIId) ;

	/**
	 * Ermittelt ein Lager auf Grund seiner Nummer
	 * 
	 * @param lagerCnr die Lagernummer
	 * @return 
	 * @throws NamingException
	 * @throws RemoteException
	 */
	LagerDto lagerFindByCnrOhnExc(String lagerCnr) throws RemoteException ;
	
	BigDecimal getLagerstandOhneExc(Integer itemId, Integer lagerIId) throws RemoteException;

	boolean hatRolleBerechtigungAufLager(Integer lagerIId) ;
	
	List<AllLagerEntry> getAllLager() throws RemoteException, EJBExceptionLP ; 

	BigDecimal getLagerstandsVeraenderungOhneInventurbuchungen(
			Integer artikelIId, Integer lagerIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis) throws RemoteException ; 
	
	BigDecimal getLagerstandAllerLagerEinesMandanten(Integer itemId, Boolean mitKonsignationsLager) throws RemoteException ;
	BigDecimal getPaternosterLagerstand(Integer itemId) throws RemoteException ;
	
	List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartposition(
			String belegartCNr, Integer belegartpositionIId) throws RemoteException ;
	
	List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
			String belegartCNr, Integer belegartpositionIId) throws RemoteException ;
	
	void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum) throws RemoteException ;
	
	BigDecimal getEinstandspreis(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrChargennr)
			throws RemoteException, EJBExceptionLP ;

	/**
	 * Den definierten Artikellagerplatz eines Artikels liefern
	 * 
	 * @param artikelIId die gewuenschte ArtikelId
	 * @param lagerIId die gewuenschte LagerId
	 * 
	 * @return null (wenn kein Lagerplatz definiert) oder der Artikellagerplatz
	 * @throws RemoteException
	 */
	ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerIId(
			Integer artikelIId, Integer lagerIId) throws RemoteException ;
	
	/**
	 * Das Hauptlager des Mandanten 
	 * @return das Hauptlager des Mandanten
	 * @throws RemoteException
	 */
	LagerDto getHauptlager() throws RemoteException ;

	/**
	 * Alle Serienchargennr des angegebenen Artikels und ihre Mengen die St&auml;nde 
	 * zum aktuellen Zeitpunkt ermitteln
	 * 
	 * @param artikelIId
	 * @param lagerIId
	 * @return
	 */
	SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId);  

	/**
	 * Alle Serienchargennr des angegebenen Artikels und ihre Mengen die St&auml;nde ermitteln
	 * @param artikelIId
	 * @param lagerIId
	 * @param tStichtag
	 * @return
	 */
	SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, java.sql.Timestamp tStichtag);  

	/**
	 * Alle Serienchargennr des angegebenen Artikels und ihre Mengen die St&auml;nde ermitteln 
	 * zum aktuellen Zeitpunkt
	 * 
	 * @param artikelIId
	 * @param lagerIId
	 * @param serienChargenNr
	 * @return
	 */
	SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, String serienChargenNr);
	
	/**
	 * F&uuml;r den angegebenen Artikel und SerienChargennr die St&auml;nde ermitteln
	 * @param artikelIId
	 * @param lagerIId
	 * @param serienChargenNr
	 * @param tStichtag
	 * @return
	 */
	SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, String serienChargenNr, java.sql.Timestamp tStichtag);	
	
	LagerDto lagerFindByMandantCNrLagerartCNrOhneExc(String lagerartCnr) throws RemoteException;

	List<LagerplatzDto> lagerplatzFindByArtikelIIdOhneExc(Integer artikelIId);

	Integer createArtikellagerplaetze(ArtikellagerplaetzeDto artikellagerplatzDto) throws RemoteException;

	LagerplatzDto lagerplatzFindByPrimaryKeyOhneExc(Integer lagerplatzIId);

	ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(
			Integer artikelIId, Integer lagerplatzIId) throws RemoteException;

	LagerplatzDto lagerplatzFindByCLagerplatzLagerIIdOhneExc(String cLagerplatz, Integer lagerIId);

	void removeArtikellagerplaetze(Integer artikellagerplaetzeIId) throws RemoteException;

	List<LagerplatzDto> lagerplatzFindByArtikelIIdLagerIIdOhneExc(Integer artikelIId, Integer lagerIId);

	List<LosDto> getAlleBetroffenenLose(Integer artikelIId, String chargennummer) throws RemoteException;

	List<LosDto> chargennummerWegwerfen(Integer artikelIId, String chargennummer, List<LosDto> losDtos) throws RemoteException;

	LagerbewegungDto[] lagerbewegungFindByArtikelIIdCSeriennrChargennr(
			Integer iId, String chargennr) throws RemoteException;

	boolean hatRolleBerechtigungAufLager(Collection<Integer> lagerIIds);

	List<ArtikellagerplaetzeDto> artikellagerplaetzeFindByLagerplatzIId(Integer lagerplatzIId);

	List<LagerplatzInfoDto> lagerplatzInfoFindByArtikelIIdOhneExc(Integer artikelIId);

	Collection<LagerstandInfoDto> getLagerstandAllerArtikelEinesMandanten(String[] lagerartCnr);

	Collection<LagerstandInfoDto> getLagerstandAllerArtikelEinerGruppe(
			Integer lagerId, Integer artikelgruppeId);

	Integer createHandlagerbewegung(HandlagerbewegungDto handlagerbewegungDto) throws RemoteException;

	Integer bucheUm(Integer artikelIId, Integer sourceLagerId, Integer targetLagerId, BigDecimal amount,
			List<SeriennrChargennrMitMengeDto> identities, String comment, BigDecimal price) throws RemoteException;

	/**
	 * Die Kombination aus ArtikelId und Serien/Chargennummer finden
	 * @param artikelId
	 * @param identity
	 * @return null wenn nicht vorhanden, sonst die Id der Kombination
	 */
	Integer artikelSerienChargenNrFindBy(Integer artikelId, String identity);

	PaneldatenDto[] getLetzteChargeninfosEinesArtikels(Integer artikelIId, String identity);
}
