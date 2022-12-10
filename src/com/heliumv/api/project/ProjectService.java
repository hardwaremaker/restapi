package com.heliumv.api.project;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.query.ProjectQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.projekt.service.ProjektHandlerFeature;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.Helper;

public class ProjectService implements IProjectService {
	@Autowired
	private ProjectQuery projectQuery ;
	@Autowired
	private IMandantCall mandantCall ;
	@Autowired
	private IJudgeCall judgeCall;
	
	@Override
	public List<ProjectEntry> getProjects(Integer limit, Integer startIndex,
			String filterCnr, String filterPartnerName, Boolean filterMyOpen,
			Boolean filterWithHidden) throws RemoteException, NamingException {
		
		if(!mandantCall.hasModulProjekt() || !judgeCall.hasProjektCRUD()) {
			return new ArrayList<ProjectEntry>();
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(buildFilterCnr(filterCnr)) ;
		collector.add(buildFilterCompanyName(filterPartnerName)) ;
		collector.addAll(buildFilterMyOpen(filterMyOpen));

		QueryParametersFeatures params = projectQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(ProjektHandlerFeature.PROJECT_DATA);

		QueryResult result = projectQuery.setQuery(params) ;
		List<ProjectEntry> projects = projectQuery.getResultList(result) ;
		return projects ;
	}
	
	@Override
	public List<ProjectEntry> getMyProjects(Integer tageZieldatum,
			List<String> stati) throws RemoteException, NamingException {
		if(!mandantCall.hasModulProjekt() || !judgeCall.hasProjektCRUD()) {
			return new ArrayList<ProjectEntry>();
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(projectQuery.getMeFilter());
		collector.add(projectQuery.getNichtErledigtFilter());
		collector.add(projectQuery.getStatusFilter(stati));
		if(tageZieldatum != null) {
			Timestamp ts = Helper.cutTimestampAddDays(
					new Timestamp(System.currentTimeMillis()),
					1 + tageZieldatum);
			collector.add(projectQuery.getZieldatumLessThanFilter(ts));	
		}
		
		QueryParametersFeatures params = projectQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(0) ;
		params.setKeyOfSelectedRow(null) ;
		params.addFeature(ProjektHandlerFeature.PROJECT_DATA);

		QueryResult result = projectQuery.setQuery(params) ;
		List<ProjectEntry> projects = projectQuery.getResultList(result) ;
		return projects ;		
	}
	
	private FilterKriterium buildFilterCnr(String cnr) {
		return projectQuery.getCnrFilter(cnr);
//		if(cnr == null || cnr.trim().length() == 0) return null ;
//		
//		FilterKriteriumDirekt fk = new FilterKriteriumDirekt(
//				"projekt.c_nr", StringHelper.removeSqlDelimiters(cnr), 
//				FilterKriterium.OPERATOR_LIKE, "", 
//				FilterKriteriumDirekt.PROZENT_LEADING, 
//				true, false, Facade.MAX_UNBESCHRAENKT) ;
//		fk.wrapWithProzent() ;
//		fk.wrapWithSingleQuotes() ;
//		return fk ;
	}
	
	
	private FilterKriterium buildFilterCompanyName(String companyName) {
		return projectQuery.getPartnerNameFilter(companyName);
//		if(companyName == null || companyName.trim().length() == 0) return null ;
//		
//		FilterKriteriumDirekt fk = new FilterKriteriumDirekt(
//				"projekt.flrpartner.c_name1nachnamefirmazeile1",StringHelper.removeSqlDelimiters(companyName),
//				FilterKriterium.OPERATOR_LIKE, "",
//				FilterKriteriumDirekt.PROZENT_TRAILING,
//				true, 
//				true, Facade.MAX_UNBESCHRAENKT); 
//		fk.wrapWithProzent() ;
//		fk.wrapWithSingleQuotes() ;
//		return fk ;
	}
	
	private List<FilterKriterium> buildFilterMyOpen(Boolean filterMyOpen) {
		return Boolean.TRUE.equals(filterMyOpen) ?
				projectQuery.getMyOpenFilter() : new ArrayList<FilterKriterium>();
	}
	
}
