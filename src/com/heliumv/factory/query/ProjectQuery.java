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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.heliumv.api.project.ProjectEntry;
import com.heliumv.api.project.ProjectEntryTransformer;
import com.heliumv.tools.StringHelper;
import com.lp.server.projekt.service.ProjectQueryResult;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class ProjectQuery extends BaseQuery<ProjectEntry> {	
	public ProjectQuery() {
		super(QueryParameters.UC_ID_PROJEKT) ;
	}
	
	@Override
	protected List<ProjectEntry> transform(QueryResult result) {
		if(result.hasFlrData()) {
			prepareTransformer((ProjectQueryResult) result) ;
		}
		return super.transform(result);
	}
	
	private void prepareTransformer(ProjectQueryResult result) {
		ProjectEntryTransformer transformer = (ProjectEntryTransformer) getTransformer() ;
		transformer.setFlrData(result.getFlrData());
	}
	
	protected List<FilterKriterium> getRequiredFilters() {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(getMandantFilter()) ;	
		return filters ;
	}
	
	private FilterKriterium getMandantFilter() {
		return filterMandant(
				"projekt." + ProjektFac.FLR_PROJEKT_MANDANT_C_NR);
//		return FilterDslBuilder
//				.create("projekt." + ProjektFac.FLR_PROJEKT_MANDANT_C_NR)
//				.equal(globalInfo.getMandant()).build() ;
//		return new FilterKriterium(
//				"projekt." + ProjektFac.FLR_PROJEKT_MANDANT_C_NR, true, 
//				StringHelper.asSqlString(globalInfo.getMandant()),
//				FilterKriterium.OPERATOR_EQUAL, false);		
	}
	
	public FilterKriterium getCnrFilter(String cnr) {
		if(StringHelper.isEmpty(cnr)) return null ;
		
		return FilterDslBuilder
				.create("projekt.c_nr")
				.like(StringHelper.removeSqlDelimiters(cnr))
				.leading().build();
//		FilterKriteriumDirekt fk = new FilterKriteriumDirekt(
//				"projekt.c_nr", StringHelper.removeSqlDelimiters(cnr), 
//				FilterKriterium.OPERATOR_LIKE, "", 
//				FilterKriteriumDirekt.PROZENT_LEADING, 
//				true, false, Facade.MAX_UNBESCHRAENKT) ;
//		fk.wrapWithProzent() ;
//		fk.wrapWithSingleQuotes() ;
//		return fk ;		
	}
	
	public FilterKriterium getPartnerNameFilter(String partnerName) {
		if(StringHelper.isEmpty(partnerName)) return null ;
		
		return FilterDslBuilder
				.create("projekt.flrpartner.c_name1nachnamefirmazeile1")
				.like(StringHelper.removeSqlDelimiters(partnerName))
				.ignoreCase().trailing().build();
	}
	
	public List<FilterKriterium> getMyOpenFilter() {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>();
		filters.add(getMeFilter()) ;
		filters.add(getNichtErledigtFilter()) ;
		filters.add(FilterDslBuilder
				.create("projekt." + ProjektFac.FLR_PROJEKT_STATUS_C_NR)
				.not().equal(ProjektServiceFac.PROJEKT_STATUS_STORNIERT).build()) ;
		return filters ;
	}
	
	public FilterKriterium getMeFilter() {
		return FilterDslBuilder
				.create("projekt." + ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER)
				.equal(globalInfo.getTheClientDto().getIDPersonal()).build();
	}
	
	public FilterKriterium getNichtErledigtFilter() {
		return FilterDslBuilder
				.create("projekt." + ProjektFac.FLR_PROJEKT_FLRPROJEKTSTATUS + ".b_erledigt")
				.equal(0).build();		
	}
	
	public FilterKriterium getZieldatumLessThanFilter(Timestamp zieldatum) {
		return FilterDslBuilder
				.create("projekt." + ProjektFac.FLR_PROJEKT_T_ZIELDATUM)
				.lt(zieldatum).build();
	}
	
	public FilterKriterium getStatusFilter(List<String> stati) {
		return FilterDslBuilder
				.create("projekt." + ProjektFac.FLR_PROJEKT_STATUS_C_NR)
				.inString(stati).build();
	}
}
