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
package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IArtikelkommentarCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.util.EJBExceptionLP;

public class ArtikelkommentarCall extends BaseCall<ArtikelkommentarFac> implements
		IArtikelkommentarCall {

	@Autowired
	private IGlobalInfo globalInfo ;
	
	public ArtikelkommentarCall() {
		super(ArtikelkommentarFac.class) ;
	}
	
	@Override
	public List<ArtikelkommentarDto> artikelkommentarFindByArtikelIId(
			Integer artikelIId) throws RemoteException, NamingException {
		ArtikelkommentarDto[] dtos = getFac()
				.artikelkommentarFindByArtikelIId(artikelIId, globalInfo.getTheClientDto()) ;
		
		return Arrays.<ArtikelkommentarDto>asList(dtos);
	}
	
	@Override
	public List<KeyvalueDto> artikelhinweiseFindByArtikelIId(
			Integer artikelIId, String belegartCnr)
					throws RemoteException, NamingException {
		return getFac().getArtikelhinweise(
				artikelIId, belegartCnr, globalInfo.getTheClientDto());
	}
	
	@Override
	public ArtikelkommentarartDto artikelkommentarartFindByPrimaryKey(Integer artikelkommentarartIId) throws EJBExceptionLP, RemoteException {
		return getFac().artikelkommentarartFindByPrimaryKey(artikelkommentarartIId, globalInfo.getTheClientDto());
	}
	
	@Override
	public ArtikelkommentarsprDto artikelkommentarsprFindByPrimaryKeyOhneExc(Integer artikelkommentarIId) {
		return getFac().artikelkommentarsprFindByPrimaryKeyOhneExc(artikelkommentarIId, globalInfo.getTheClientDto().getLocUiAsString(), globalInfo.getTheClientDto());
	}
	
	@Override
	public List<ArtikelkommentarDto> artikelkommentarFindByArtikelIIdFull(Integer artikelIId) {
		return getFac().artikelkommentarFindByArtikelIIdFull(artikelIId, globalInfo.getTheClientDto());
	}
	
	@Override
	public ArtikelkommentarDto artikelkommentarFindByPrimaryKey(Integer artikelkommentarIId) throws EJBExceptionLP, RemoteException {
		return getFac().artikelkommentarFindByPrimaryKey(artikelkommentarIId, globalInfo.getTheClientDto());
	}
}
