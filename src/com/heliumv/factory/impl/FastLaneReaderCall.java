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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.GlobalInfo;
import com.heliumv.factory.IFastLaneReaderCall;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public abstract class FastLaneReaderCall extends BaseCall<FastLaneReader> implements IFastLaneReaderCall {
	private static Logger log = LoggerFactory.getLogger(FastLaneReaderCall.class) ;

	@Autowired
	protected GlobalInfo globalInfo;
	
	private String uuid ;
	private Integer usecaseId ;
	private TableColumnInformation cachedColumnInfo ;

	protected FastLaneReaderCall(Integer theUsecaseId) {
		this(UUID.randomUUID().toString(), theUsecaseId) ;
	}
	
	protected FastLaneReaderCall(String theUuid, Integer theUsecaseId) {
		super(FastLaneReader.class) ;
		
		uuid = theUuid ;
		usecaseId = theUsecaseId ; 
	}

	public Integer getUsecaseId() {
		return usecaseId ;
	}
	
	public QueryResult setQuery(QueryParameters queryParams) throws NamingException, RemoteException, EJBExceptionLP {
		installRequiredFilters(queryParams) ;
		
		try {
			QueryResult result = getFac().setQuery(uuid,
					usecaseId, queryParams, globalInfo.getTheClientDto()) ;
			result.getRowCount() ;

			if(cachedColumnInfo == null) {
				cachedColumnInfo = getFac().getTableColumnInfo(uuid, 
						usecaseId, globalInfo.getTheClientDto()) ;
			}
			
			return result ;
		} catch(RemoteException e) {
			log.error("setQuery", e);
			throw e;
		} catch(EJBExceptionLP e) {
			log.error("EJBExceptionLP", e);
			throw e;
		}
	}
	
	public QueryParameters getDefaultQueryParameters() {
		return getDefaultQueryParameters(new FilterBlock(new FilterKriterium[0], " AND ")) ;
	}
	
	public QueryParameters getDefaultQueryParameters(FilterBlock filterCrits) {
		ArrayList<?> listOfExtraData = new ArrayList<Object>() ;
		SortierKriterium[] sortCrits = new SortierKriterium[0] ;
		QueryParameters params = new QueryParameters(
				getUsecaseId(), sortCrits, filterCrits, 0, listOfExtraData) ;
		params.setIsApi(true) ;
		return params ;		
	}
	
	public QueryParameters getDefaultQueryParameters(FilterKriteriumCollector collector) {
		FilterBlock filterBlock = new FilterBlock(collector.asArray(), " AND ");
		return getDefaultQueryParameters(filterBlock);
	}
	
	public QueryParametersFeatures getFeatureQueryParameters(FilterBlock filterCrits) {
		ArrayList<?> listOfExtraData = new ArrayList<Object>() ;
		SortierKriterium[] sortCrits = new SortierKriterium[0] ;
		QueryParametersFeatures params = new QueryParametersFeatures(
				getUsecaseId(), sortCrits, filterCrits, 0, listOfExtraData) ;
		params.setIsApi(true) ;
		return params ;
	}
	
	public QueryParametersFeatures getFeatureQueryParameters(FilterKriteriumCollector collector) {
		FilterBlock filterBlock = new FilterBlock(collector.asArray(), " AND ");
		return getFeatureQueryParameters(filterBlock);
	}
	
	public TableInfo getTableInfo() throws NamingException, RemoteException {
//		return getFac().getTableInfo(uuid, usecaseId, null, Globals.getTheClientDto()) ;
		return getFac().getTableInfo(uuid, usecaseId, null, globalInfo.getTheClientDto()) ;
//		TableInfo info = getFac().getTableInfo(uuid, usecaseId, Globals.getTheClientDto()) ;
//		System.out.println("" + Arrays.toString(info.getDataBaseColumnNames())) ;
	}
	
	public TableColumnInformation getTableColumnInfo()  {
		return cachedColumnInfo ;
//		return getFac().getTableColumnInfo(uuid, usecaseId, Globals.getTheClientDto()) ;
	}
	
	private void installRequiredFilters(QueryParameters queryParams) throws NamingException, RemoteException {
		List<FilterKriterium> requiredFilters = getRequiredFilters() ;
		if(requiredFilters == null || requiredFilters.size() == 0) return ;
		
		FilterKriterium[] knownFilters = queryParams.getFilterBlock().filterKrit ;
		for(int i = 0 ; knownFilters != null && i < knownFilters.length; i++) {
			requiredFilters.add(knownFilters[i]) ;
		}
		queryParams.getFilterBlock().filterKrit = requiredFilters.toArray(new FilterKriterium[0]);
	}
	
	/**
	 * Jene Filter einhängen, die automatisch (und immer!) notwendig sind damit richtige 
	 * Daten geliefert werden. Beispielsweise der Mandant.
	 * 
	 * @return null, oder eine (auch leere) Liste von immer notwendigen Filtern
	 */
	protected abstract List<FilterKriterium> getRequiredFilters() throws NamingException, RemoteException ;
}
