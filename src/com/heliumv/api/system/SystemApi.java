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
package com.heliumv.api.system;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.BaseApi.Param;
import com.heliumv.factory.IMediaCall;
import com.heliumv.factory.ISystemCall;
import com.heliumv.factory.query.CostBearingUnitQuery;
import com.heliumv.factory.query.CountryQuery;
import com.heliumv.factory.query.ItemUnitQuery;
import com.heliumv.factory.query.TaxDescriptionQuery;
import com.heliumv.factory.query.TextblockQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.StringHelper;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

@Service("hvSystem")
@Path("/api/v1/system")
public class SystemApi extends BaseApi implements ISystemApi {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
	private static Logger externalLog = LoggerFactory.getLogger("externalLog") ;
	private static Logger log = LoggerFactory.getLogger(SystemApi.class) ;
	private static final String RESOURCE_BUNDLE_VERSION = "com.heliumv.res.lp";
	private static final String RESOURCE_BUILD_NUMBER = "lp.version.restapi.build";
	
	@Autowired
	private ISystemCall systemCall ;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CountryQuery countryQuery;
	@Autowired
	private CostBearingUnitQuery costBearingUnitQuery;
	@Autowired
	private TextblockQuery textblockQuery;
	@Autowired
	private IMediaCall mediaCall;
	@Autowired
	private TaxDescriptionQuery taxDescriptionQuery;
	@Autowired
	private ItemUnitQuery itemUnitQuery;
	
//	@Autowired
//	private IParameterCall parameterCall ;
//	@Autowired
//	private IJudgeCall judgeCall ;	
	
	@GET
	@Path("/ping")
	@Produces({FORMAT_JSON, FORMAT_XML, "text/html"})
	@Override
	public PingResult ping() {
		PingResult result = new PingResult() ;
		try {
			long startMs = System.currentTimeMillis() ;
			result.setApiTime(startMs) ;
			result.setServerBuildNumber(systemCall.getServerBuildNumber()) ;
			result.setServerVersionNumber(systemCall.getServerVersion()) ;
			result.setServerTime(systemCall.getServerTimestamp().getTime());
			result.setServerId(systemCall.getServerId());
			long stopMs = System.currentTimeMillis() ;
			result.setServerDuration(Math.abs(stopMs - startMs));
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		}
		
 		return result ;
	}

	@GET
	@Path("/localping")
	@Produces({FORMAT_JSON, FORMAT_XML, "text/html"})
	@Override
	public LocalPingResult localping() {
		return localpingImpl() ;
	}

	private Integer getBuildNumber() {
		Integer apiBuildNumber = 1;
		
		try {
			ResourceBundle resBundle = ResourceBundle
					.getBundle(RESOURCE_BUNDLE_VERSION);
			if(resBundle.containsKey(RESOURCE_BUILD_NUMBER)) {
				String buildNumber = resBundle.getString(RESOURCE_BUILD_NUMBER);
				apiBuildNumber = Integer.parseInt(buildNumber);
				
			}
		} catch(MissingResourceException e) {
		} catch(NumberFormatException e) {
		}
		
		return apiBuildNumber;
	}
	
	private LocalPingResult localpingImpl() {		
		LocalPingResult result = new LocalPingResult() ;
		result.setApiTime(System.currentTimeMillis());
		result.setApiBuildNumber(getBuildNumber());
		result.setApiVersionNumber("1.0.1") ;
		return result ;		
	}

	@POST
	@Path("/log")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void logMessage(
			LogMessageEntry logEntry
	) {
		long now = System.currentTimeMillis();
		log.warn("now = " + now + ", entrytime = " + logEntry.getTime() + " diff = " + (now - logEntry.getTime()));
		
		long t = logEntry.getTime() == 0 ? now : logEntry.getTime();
		String d = "[" + sdf.format(new Date(t)) + "] ";
		if(logEntry.getSeverity().equals(LogSeverity.DEBUG)) {
			externalLog.debug(d + logEntry.getMsg());
			return;
		}
		if(logEntry.getSeverity().equals(LogSeverity.INFO)) {
			externalLog.info(d + logEntry.getMsg());
			return;
		}
		if(logEntry.getSeverity().equals(LogSeverity.WARN)) {
			externalLog.warn(d + logEntry.getMsg());
			return;
		}
		if(logEntry.getSeverity().equals(LogSeverity.ERROR)) {
			externalLog.error(d + logEntry.getMsg());
			return;
		}
		
		log.warn("External logentry with unknown severity " + d + logEntry.getMsg());
	}


	@POST
	@Path("/logbarcode")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void logBarcode(LogBarcodeEntry entry) {
		if(entry == null) {
			respondBadRequestValueMissing("barcodeEntry");
			return;
		}
		
		systemService.logBarcode(entry.getBarcode(), entry.getTime());
	}
	
	@GET
	@Path("/country")
	@Produces({FORMAT_JSON, FORMAT_XML, "text/html"})
	@Override
	public CountryEntryList getCountries(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new CountryEntryList();
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		
		QueryParameters params = countryQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = countryQuery.setQuery(params);
		return new CountryEntryList(countryQuery.getResultList(result));
	}
	
	@GET
	@Path("/costbearingunit")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public CostBearingUnitEntryList getCostBearingUnits(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new CostBearingUnitEntryList();
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		
		QueryParameters params = costBearingUnitQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = costBearingUnitQuery.setQuery(params);
		return new CostBearingUnitEntryList(costBearingUnitQuery.getResultList(result));
	}

	@GET
	@Path("/textblock")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public TextblockEntryList getTextBlocks(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey,
			@QueryParam(Filter.HIDDEN) Boolean filterWithHidden,
			@QueryParam("addContents") Boolean addContents) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new TextblockEntryList();
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(textblockQuery.buildFilterWithHidden(filterWithHidden));
		QueryParameters params = textblockQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = textblockQuery.setQuery(params);
		return updateMedia(new TextblockEntryList(
				textblockQuery.getResultList(result)), addContents);
	}
	
	private TextblockEntryList updateMedia(TextblockEntryList list, Boolean addContents) throws RemoteException {
		if(!Boolean.TRUE.equals(addContents)) return list;
		
		for (TextblockEntry entry : list.getEntries()) {
			MediastandardDto dto = mediaCall
					.mediaFindByPrimaryKey(entry.getId()).get();
			if(dto.getDatenformatCNr().startsWith("text/")) {
				entry.setText(dto.getOMediaText());
				entry.setLocaleCnr(StringHelper.trim(entry.getLocaleCnr()));
			} else {
				entry.setBlob(dto.getOMediaImage());
				entry.setFilename(dto.getCDateiname());
			}
		}
		
		return list;
	}
	
	@GET
	@Path("/taxdescription")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public TaxDescriptionEntryList getTaxDescriptions(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new TaxDescriptionEntryList();
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		QueryParameters params = taxDescriptionQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = taxDescriptionQuery.setQuery(params);
		return new TaxDescriptionEntryList(taxDescriptionQuery.getResultList(result));
	}
	
	@GET
	@Path("/itemunit")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public ItemUnitEntryList getItemUnits(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.CNR) String cnr, 
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new ItemUnitEntryList();
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(itemUnitQuery.getCnrFilter(cnr));
		QueryParameters params = itemUnitQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = itemUnitQuery.setQuery(params);
		return new ItemUnitEntryList(itemUnitQuery.getResultList(result));
	}
}
