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
package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntry;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.ISystemCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ArtikelArbeitszeitQuery extends BaseQuery<ItemEntry> {
	@Autowired 
	private IMandantCall mandantCall ;
	@Autowired
	private ISystemCall systemCall ;
	@Autowired
	private IParameterCall parameterCall ;
//	@Autowired
//	private IGlobalInfo globalInfo ;


	public ArtikelArbeitszeitQuery() {
		super(QueryParameters.UC_ID_ARTIKELLISTE) ;
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		
		filters.add(getArbeitszeitArtikelFilter()) ;
		filters.add(getArtikelMandantFilter()) ;	
		return filters ;
	}

	private FilterKriterium getArbeitszeitArtikelFilter() {
		return new FilterKriterium("artikelliste."
				+ ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, true,
				StringHelper.asSqlString(ArtikelFac.ARTIKELART_ARBEITSZEIT),
				FilterKriterium.OPERATOR_EQUAL, false) ;
	}
	
	private FilterKriterium getArtikelMandantFilter() throws NamingException {
		String mandant = mandantCall.hasFunctionZentralerArtikelstamm() ?
				systemCall.getHauptmandant() : globalInfo.getMandant();
				
//		String mandant = globalInfo.getMandant() ;
//		if (mandantCall.hasFunctionZentralerArtikelstamm()) {
//			mandant = systemCall.getHauptmandant() ;
//		}
		
		return filterMandant("artikelliste.mandant_c_nr", mandant);
//		return new FilterKriterium("artikelliste.mandant_c_nr",
//					true, StringHelper.asSqlString(mandant),
//					FilterKriterium.OPERATOR_EQUAL, false) ;
	}
	
	public FilterKriterium getFilterArtikelNummer(String filterCnr) throws NamingException, RemoteException {
		if(filterCnr == null || filterCnr.trim().length() == 0) return null ;
		
		int maxLength = parameterCall.getMaximaleLaengeArtikelnummer() ;

		FilterKriteriumDirekt fk = new FilterKriteriumDirekt("artikelliste.c_nr", 
				StringHelper.removeSqlDelimiters(filterCnr),
				FilterKriterium.OPERATOR_LIKE, "",
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, maxLength);
		fk.wrapWithProzent() ;
		fk.wrapWithSingleQuotes() ;
		return fk ;		
	}
}
