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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungTerminalDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.util.EJBExceptionLP;

public interface IFertigungCall {
	
	void bucheMaterialAufLos(LosDto losDto, BigDecimal menge) throws RemoteException ;

	void bucheMaterialAufLos(LosDto losDto, BigDecimal menge,
			boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen,
			boolean bUnterstuecklistenAbbuchen,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) throws RemoteException ;

	
	LosablieferungDto createLosablieferung(LosablieferungDto losablieferungDto,
			boolean bErledigt) throws RemoteException, EJBExceptionLP ;
	
	/**
	 * Ein Los ueber seine IId finden.
	 * 
	 * @param losId ist die gesuchte Los-IId
	 * @return null wenn das Los nicht vorhanden ist, ansonsten das dto
	 */
	LosDto losFindByPrimaryKeyOhneExc(Integer losId) ;

	/**
	 * Los ueber seine Nummer im angemeldeten Mandanten finden
	 * 
	 * @param cNr
	 * @return
	 */
	LosDto losFindByCNrMandantCNrOhneExc(String cNr) ;

	/**
	 * Los ueber seine Nummer in einem beliebigen Mandanten finden
	 * @param cNr
	 * @param mandantCNr
	 * @return
	 */
	LosDto losFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr) ;
	
	LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId) throws RemoteException, EJBExceptionLP ;
	
	void gebeMaterialNachtraeglichAus(
			LossollmaterialDto lossollmaterialDto, LosistmaterialDto losistmaterialDto, 
			List<SeriennrChargennrMitMengeDto> listSnrChnr, boolean reduzierFehlmenge)
		throws RemoteException, EJBExceptionLP ;
	
	LossollmaterialDto[] lossollmaterialFindByLosIIdOrderByISort(
			Integer losIId) throws RemoteException, EJBExceptionLP ;
	
	LosistmaterialDto[] losistmaterialFindByLossollmaterialIId(
			Integer lossollmaterialIId) throws RemoteException, EJBExceptionLP ;
	
	void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu) 
			throws RemoteException, EJBExceptionLP ;	
	
	LosistmaterialDto createLosistmaterial(LosistmaterialDto losistmaterialDto,
			String sSerienChargennummer) throws RemoteException, EJBExceptionLP ; 	
	
	LossollmaterialDto lossollmaterialFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP ;

	/**
	 * Lossollmaterialpositionen f&uuml;r das Los und den Artikel ermitteln
	 * 
	 * @param losIId
	 * @param artikelIId
	 * @return
	 * @throws RemoteException
	 */
	LossollmaterialDto[] lossollmaterialFindyByLosIIdArtikelIId(
			Integer losIId, Integer artikelIId) throws RemoteException ;
	
	/**
	 * Nur die Menge des Lossollmaterials auf die neue Menge &auml;ndern
	 * 
	 * @param lossollmaterialId
	 * @param mengeNeu
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void updateLossollmaterialMenge(Integer lossollmaterialId, BigDecimal mengeNeu) 
			throws RemoteException, EJBExceptionLP ;
	
	/**
	 * Die bereits ausgegebene Menge fuer die Sollposition ermitteln
	 * 
	 * @param lossollmaterialId
	 * @return die bereits (bis heute) ausgegebene Menge 
	 * @throws RemoteException
	 */
	BigDecimal getAusgegebeneMenge(Integer lossollmaterialId) throws RemoteException ;

	/**
	 * Den Arbeitsplan mittels Id ermitteln
	 * 
	 * @param iId
	 * @return
	 */
	LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKey(Integer iId) throws RemoteException, EJBExceptionLP ;
	
	LossollarbeitsplanDto updateLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto) throws RemoteException, EJBExceptionLP ;
	LossollarbeitsplanDto updateLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto, boolean verschiebeNachfolger) throws RemoteException, EJBExceptionLP ;
	
	
	LosDto updateLos(LosDto losDto) throws RemoteException, EJBExceptionLP ;
	
	LoszusatzstatusDto[] loszusatzstatusFindByLosIIdOhneExc(Integer losIId) throws RemoteException;
	
	ZusatzstatusDto zusatzstatusFindByPrimaryKey(Integer iId) throws RemoteException;
	
	void terminverschieben(Integer losId, Timestamp start, Timestamp finish);

	LosablieferungDto[] losablieferungFindByLosIId(Integer losId) throws RemoteException;
	
	LosablieferungDto losablieferungFindByPrimaryKeyOhneExc(Integer losablieferungIId);
	
	LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIId(Integer productionId) throws EJBExceptionLP, RemoteException;
	
	void setzeVPEtikettGedruckt(Integer losIId);

	BigDecimal getErledigteMenge(Integer losIId) throws EJBExceptionLP, RemoteException;
	
	void aendereLosgroesse(Integer losIId, BigDecimal neueLosgroesse) throws RemoteException;
	
	void pruefePositionenMitSollsatzgroesseUnterschreitung(Integer losIId, BigDecimal menge) throws EJBExceptionLP, RemoteException;
	
	LosablieferungDto createLosablieferungFuerTerminal(LosablieferungTerminalDto laDto) 
			throws RemoteException, EJBExceptionLP;
	
	LosDto losFindByForecastpositionIIdOhneExc(Integer forecastpositionIId);

	LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKeyOhneExc(Integer iId) throws RemoteException, EJBExceptionLP;

	EJBExceptionLP gebeLosAus(Integer losId, boolean handausgabe, boolean throwWhenCreate) throws RemoteException;

	String getArtikelhinweiseAllerLossollpositionen(Integer losId) throws RemoteException;
	
	void setzeLosInProduktion(Integer losId) throws RemoteException;

	EJBExceptionLP manuellErledigen(Integer losId, boolean datumDerLetztenAblieferungVerwenden) throws RemoteException;

	ZusatzstatusDto zusatzstatusFindByMandantCNrCBezOhneExc(String zusatzstatus, String mandantCnr);

	ZusatzstatusDto zusatzstatusFindByMandantCNrCBezOhneExc(String zusatzstatus);

	LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(Integer losIId, Integer zusatzstatusIId)
			throws RemoteException;

	Integer createLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto) throws RemoteException;

	Integer createBedarfsuebernahme(BedarfsuebernahmeDto bedarfsuebernahmeDto);

	BigDecimal getMengeDerJuengstenLosablieferung(Integer losId) throws RemoteException;

	/**
	 * Sucht Lossollarbeitsplan eines Loses nach der Arbeitsgangnummer.
	 * Liefert die Anfrage mehrere Datens&auml;tze, wird nur der erste retourniert.
	 *  
	 * @param losId
	 * @param arbeitsgang
	 * @return (erster) Lossollarbeitsplan oder <code>null</code> bei keinem Ergebnis
	 */
	LossollarbeitsplanDto lossollarbeitsplanFindByLosIIdArbeitsgangnummerSingleResult(Integer losId,
			Integer arbeitsgang);

	/**
	 * Sucht Lossollarbeitsplan eines Loses nach der Arbeitsgangnummer und Unterarbeitsgang.
	 * Liefert die Anfrage mehrere Datens&auml;tze, wird nur der erste retourniert.
	 * Wird als Unterarbeitsgang <code>null</code> &uuml;bergeben, wird dieser ignoriert und nur &uuml;ber
	 * Los-Id und Arbeitsgang gesucht.
	 * 
	 * @param losId
	 * @param arbeitsgang
	 * @param unterarbeitsgang
	 * @return (erster) Lossollarbeitsplan oder <code>null</code> bei keinem Ergebnis
	 */
	LossollarbeitsplanDto lossollarbeitsplanFindByLosIIdArbeitsgangUnterarbeitsgangSingleResult(Integer losId,
			Integer arbeitsgang, Integer unterarbeitsgang);

	LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(Integer losId, Integer artikelId)
			throws EJBExceptionLP, RemoteException;

	List<LosDto> losFindOffeneByMe(List<String> stati, Integer tageInZukunft);
	
	void stoppeProduktion(Integer losId) throws EJBExceptionLP, RemoteException;
}
