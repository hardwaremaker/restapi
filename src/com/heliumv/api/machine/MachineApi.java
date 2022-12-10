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
package com.heliumv.api.machine;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.production.IProductionApi;
import com.heliumv.api.staff.WorkCalendarEntry;
import com.heliumv.api.staff.WorkCalendarEntryList;
import com.heliumv.api.worktime.DayTypeEntry;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IMaschineCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IPersonalCall;
import com.heliumv.factory.IZeiterfassungCall;
import com.heliumv.factory.query.MachineGroupQuery;
import com.heliumv.factory.query.MachineQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.MaschineHandlerFeature;
import com.lp.server.personal.service.MaschinenVerfuegbarkeitsStundenDto;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.EJBExceptionLP;

@Service("hvMachine")
@Path("/api/v1/machine/")
public class MachineApi extends BaseApi implements IMachineApi {	
	@Autowired
	private MachineQuery machineQuery ;
	
	@Autowired
	private MachineGroupQuery machineGroupQuery ;

	@Autowired
	private IMaschineCall maschineCall ;
	@Autowired
	private IZeiterfassungCall zeiterfassungCall ;
	@Autowired
	IProductionApi productionApi ;
	@Autowired
	IPersonalCall personalCall ;
	@Autowired
	IJudgeCall judgeCall ;
	@Autowired
	IParameterCall parameterCall ;
	
	private DayTypeModel dayTypeModel ;
	
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public MachineEntryList getMachines(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,			
			@QueryParam(Filter.HIDDEN) Boolean filterWithHidden,
			@QueryParam("filter_productiongroupid") Integer productiongroupId,			
			@QueryParam("filter_planningview") Boolean filterPlanningView,
			@QueryParam("filter_staffid") Integer filterStaffId) throws RemoteException, NamingException, EJBExceptionLP {
		if(connectClient(userId) == null) return new MachineEntryList() ;
		
		MachineQueryFilter filter = new MachineQueryFilter();
		filter.setFilterWithHidden(filterWithHidden);
		filter.setProductiongroupId(productiongroupId);
		filter.setFilterPlanningView(filterPlanningView);
		filter.setFilterStaffId(filterStaffId);
		
		return getMachinesImpl(limit, startIndex, filter);
	}

	public MachineEntryList getMachinesImpl(Integer limit, Integer startIndex,
			MachineQueryFilter filter) throws NamingException, RemoteException {
		MachineEntryList entries = new MachineEntryList() ;
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(machineQuery.getFilterWithHidden(filter.getFilterWithHidden())) ;
		collector.add(machineQuery.getFilterProductionGroup(filter.getProductiongroupId()));
		collector.add(machineQuery.getFilterPlanningView(filter.getFilterPlanningView()));
		collector.add(machineQuery.getFilterStaff(filter.getFilterStaffId()));
		collector.add(machineQuery.getFilterInMachineIds(filter.getFilterInMachineIds()));
		
		QueryParametersFeatures params = machineQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(MaschineHandlerFeature.PERSONAL_ID);

		QueryResult result = machineQuery.setQuery(params) ;
		entries.setEntries(machineQuery.getResultList(result)) ;
		
		return entries ;
	}


	@GET
	@Path("/{" + Param.MACHINEID + "}/availability")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public MachineAvailabilityEntryList getAvaibilities(
			@PathParam(Param.MACHINEID) Integer machineId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam("dateMs") Long dateMs,
			@QueryParam("days") Integer days,
			@QueryParam(With.DESCRIPTION) Boolean withDescription) throws RemoteException, NamingException, EJBExceptionLP {
		if(connectClient(userId) == null) return new MachineAvailabilityEntryList() ;
		
		return getAvailabilitiesImpl(machineId, dateMs, days, withDescription, getDayTypeModel());
	}


	private MachineAvailabilityEntryList getAvailabilitiesImpl(
			Integer machineId, Long dateMs, Integer days,
			Boolean withDescription, DayTypeModel dayTypeModel)
			throws NamingException, RemoteException {
		MachineAvailabilityEntryList entries = new MachineAvailabilityEntryList() ;
		
		Date d = new Date(dateMs == null ? System.currentTimeMillis() : dateMs) ;
		
		if(days == null) {
			days = new Integer(1) ;
		} else {
			if(days < 0) {
				respondBadRequest("days", "value = '" + days + "' is not >= 0.");
				return entries ;
			}
		}
		
		if(withDescription == null) {
			withDescription = new Boolean(false) ;
		}
		
		List<MaschinenVerfuegbarkeitsStundenDto> dtos =
				maschineCall.getVerfuegbarkeitStunden(machineId, d, days) ;
		
		List<MachineAvailabilityEntry> apiDtos = new ArrayList<MachineAvailabilityEntry>() ;
		for (MaschinenVerfuegbarkeitsStundenDto dto : dtos) {
			MachineAvailabilityEntry entry = new MachineAvailabilityEntry() ;
			entry.setMachineId(dto.getMaschineId());
			entry.setDayTypeId(dto.getTagesartId());
			entry.setAvailabilityHours(dto.getVerfuegbarkeitH()) ;
			entry.setDateMs(dto.getDate().getTime()) ;
			if(withDescription) {
				entry.setDayTypeDescription(dayTypeModel.getDescription(entry.getDayTypeId()));
			}
			apiDtos.add(entry) ;
		}
		
		entries.setEntries(apiDtos) ;
		return entries ;
	}
	
	@GET
	@Path("/groups")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public MachineGroupEntryList getMachineGroups(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam("filter_productiongroupid") Integer productiongroupId,
			@QueryParam("filter_planningview") Boolean filterPlanningView) throws RemoteException, NamingException, EJBExceptionLP {
		if(connectClient(userId) == null) return new MachineGroupEntryList() ;
		return getMachineGroupsImpl(limit, startIndex, productiongroupId, filterPlanningView);
	}


	private MachineGroupEntryList getMachineGroupsImpl(
			Integer limit, Integer startIndex, Integer filterProductiongroupId, Boolean filterPlanningView) throws NamingException, RemoteException {
		MachineGroupEntryList entries = new MachineGroupEntryList() ;
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(machineGroupQuery.getFilterPlanningView(filterPlanningView));
		collector.add(machineGroupQuery.getFilterProductiongroupId(filterProductiongroupId));
		
		QueryParameters params = machineGroupQuery.getDefaultQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;

		QueryResult result = machineGroupQuery.setQuery(params) ;
		entries.setEntries(machineGroupQuery.getResultList(result)) ;
		
		return entries ;
	}

	@GET
	@Path("/planningview")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PlanningView getPlanningView(
			@QueryParam(Param.USERID) String userId,
			@QueryParam("dateMs") Long dateMs,
			@QueryParam("days") Integer days,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,			
			@QueryParam(Filter.HIDDEN) Boolean filterWithHidden,
			@QueryParam("filter_startdate") Boolean filterStartDate,
			@QueryParam("filter_productiongroupid") Integer filterProductiongroupId,
			@QueryParam(With.DESCRIPTION) Boolean withDescription) throws RemoteException, NamingException, EJBExceptionLP {
		PlanningView planningView = new PlanningView() ;
		if(connectClient(userId) == null) return planningView ;
		
		MachineQueryFilter queryFilter = new MachineQueryFilter();
		queryFilter.setFilterWithHidden(filterWithHidden);
		queryFilter.setProductiongroupId(filterProductiongroupId);
		queryFilter.setFilterPlanningView(Boolean.TRUE);
		
		planningView.setMachineList(getMachinesImpl(limit, startIndex, queryFilter)) ;

		if(filterStartDate == null) {
			filterStartDate = Boolean.FALSE;
		}
		
		if(dateMs == null) {
			dateMs = new Long(System.currentTimeMillis()) ;
		}
		if(days == null) {
			days = new Integer(1) ;
		}
		if(withDescription == null) {
			withDescription = new Boolean(false) ;
		}

		Calendar c = Calendar.getInstance() ;
		c.setTimeInMillis(dateMs);
		c.add(Calendar.DAY_OF_YEAR, days);
		long endDateMs = c.getTimeInMillis() ;
		planningView.setOpenWorkList(productionApi.getOpenWorkEntriesImpl(
				limit, startIndex, filterStartDate ? dateMs : null, endDateMs, filterProductiongroupId, 
						Boolean.TRUE, null, Boolean.FALSE, Boolean.FALSE)) ;
		planningView.setMachineGroupList(getMachineGroupsImpl(limit, startIndex, filterProductiongroupId, Boolean.TRUE));
		Map<Integer, MachineAvailabilityEntryList> mapAvailability = new HashMap<Integer, MachineAvailabilityEntryList>() ;
		for (MachineEntry machine : planningView.getMachineList().getEntries()) {
			mapAvailability.put(machine.getId(), 
				getAvailabilitiesImpl(machine.getId(), dateMs, days, withDescription, getDayTypeModel())) ;
		}
		planningView.setMachineAvailabilityMap(mapAvailability);
		
		BetriebskalenderDto[] betriebskalenderDtos = personalCall.getFeiertagsKalender() ;
		planningView.setHolidayList(
				new WorkCalendarEntryList(transform(betriebskalenderDtos, withDescription, dateMs)));
		
		BetriebskalenderDto[] betriebsurlaubDtos = personalCall.getBetriebsurlaubKalender() ;
		planningView.setPlantHolidayList(
				new WorkCalendarEntryList(transform(betriebsurlaubDtos, withDescription, dateMs)));
		
		planningView.setJudgeWorkUnitChange(judgeCall.hasFertLosCUD());
		planningView.setViewOpenWorkDetail(parameterCall.getAuslastungsAnzeigeDetailAg());
		planningView.setDispatchingBufferMinutes(parameterCall.getAuslastungsAnzeigePufferdauer());
		planningView.setDispatchingGridMinutes(parameterCall.getAuslastungsAnzeigeEinlastungsRaster());
		
		return planningView ;
	}
	
	private List<WorkCalendarEntry> transform(BetriebskalenderDto[] betriebskalenderDtos,
			boolean withDescription, long minimumDateMs) throws NamingException, RemoteException {
		List<WorkCalendarEntry> entries = new ArrayList<WorkCalendarEntry>() ;
		if(betriebskalenderDtos == null) return entries ;
	
		Calendar c = Calendar.getInstance() ;
		c.setTimeInMillis(minimumDateMs) ;
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0) ;
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0) ;
		
		Calendar bc = Calendar.getInstance() ;
		for(int i = 0 ; i < betriebskalenderDtos.length; i++) {
			bc.setTimeInMillis(betriebskalenderDtos[i].getTDatum().getTime());
			if(bc.compareTo(c) < 0) continue ;

			WorkCalendarEntry entry = new WorkCalendarEntry(betriebskalenderDtos[i].getIId()) ;
			entry.setDayTypeId(betriebskalenderDtos[i].getTagesartIId());
			entry.setDate(betriebskalenderDtos[i].getTDatum().getTime());
			if(withDescription) {
				entry.setDescription(betriebskalenderDtos[i].getCBez()) ;
				entry.setDayTypeDescription(getDayTypeModel().getDescription(entry.getDayTypeId())) ;
			}
			entries.add(entry) ;
		}
		
		return entries ;
	}
	
	private DayTypeModel getDayTypeModel() throws NamingException, RemoteException {
		if(dayTypeModel == null) {
			dayTypeModel = new DayTypeModel(zeiterfassungCall.getAllSprTagesarten()) ;
		}
		return dayTypeModel ;
	}
	
	private class DayTypeModel {
		private HashMap<Integer, DayTypeEntry> mapTypes = new HashMap<Integer, DayTypeEntry>();

		public DayTypeModel() {
		}

		public DayTypeModel(List<DayTypeEntry> dayTypes) {
			for (DayTypeEntry dayTypeEntry : dayTypes) {
				mapTypes.put(dayTypeEntry.getId(), dayTypeEntry) ;
			}					
		}
		
		public String getDescription(Integer dayTypeId) {
			if(mapTypes.get(dayTypeId) == null) {
				return "Unbekannt (" + dayTypeId + ")." ;
			} else {
				return mapTypes.get(dayTypeId).getDescription();
			}	
		}
	}
}
