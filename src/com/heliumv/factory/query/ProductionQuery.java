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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.production.ProductionEntry;
import com.heliumv.api.production.ProductionEntryTransformer;
import com.heliumv.api.production.ProductionStatus;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IStuecklisteCall;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.StringHelper;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.server.fertigung.service.LosQueryResult;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class ProductionQuery extends BaseQuery<ProductionEntry> {
	@Autowired
	private IParameterCall parameterCall ;
	@Autowired
	private ProductionEntryTransformer productionEntryTransformer ;
	@Autowired
	private IStuecklisteCall stuecklisteCall;
	
	public ProductionQuery() {
		super(QueryParameters.UC_ID_LOS) ;
	//	setTransformer(new ProductionEntryTransformer()) ;
		setTransformer(productionEntryTransformer);
	}
	
	@Override
	protected List<ProductionEntry> transform(QueryResult result) {
		if(result.hasFlrData()) {
			prepareTransformer((LosQueryResult) result);
		}
		return super.transform(result);
	}
	
	private void prepareTransformer(LosQueryResult result) {
		ProductionEntryTransformer transformer = (ProductionEntryTransformer) getTransformer();
		transformer.setFlrData(result.getFlrData());
	}
	
	public ProductionQuery(IParameterCall parameterCall) throws NamingException {
		super(UUID.randomUUID().toString(), QueryParameters.UC_ID_LOS) ;
//		setTransformer(new ProductionEntryTransformer()) ;
		setTransformer(productionEntryTransformer) ;
		this.parameterCall = parameterCall ;
	}
	
			
	@Override
	protected List<FilterKriterium> getRequiredFilters() {
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(getMandantFilter());
		collector.add(getProductionGroupFilter());
		return collector.getFilters() ;
	}

	private FilterKriterium getMandantFilter() {
		return new FilterKriterium("flrlos.mandant_c_nr", true,
				StringHelper.asSqlString( globalInfo.getMandant()),
				FilterKriterium.OPERATOR_EQUAL, false);
	}

	private FilterKriterium getProductionGroupFilter() {
		List<Integer> productionGroupIds = stuecklisteCall
				.getEingeschraenkteFertigungsgruppen();
		if(productionGroupIds.isEmpty()) return null;
		
		return FilterDslBuilder
				.create("flrlos.fertigungsgruppe_i_id")
				.inInteger(productionGroupIds)
				.build();
	}
	
	public FilterKriterium getFilterItemCnr(String itemCnr) {
		if(itemCnr == null) return null;
		
		return FilterDslBuilder
				.create("flrlos.flrstueckliste.flrartikel.c_nr")
				.equal(itemCnr).build();
	}

	public FilterKriterium getFilterProductionGroupId(Integer productionGroupId) {
		if (productionGroupId == null) return null;
		
		return FilterDslBuilder
				.create("flrlos.fertigungsgruppe_i_id")
				.equal(productionGroupId).build();
	}
	
	public FilterKriterium getFilterBuchbareLose() throws Throwable {
		return FertigungFilterFactory.getInstance().createFKBebuchbareLosStatus(
				parameterCall.isZeitdatenAufErledigteBuchbar(), true, 
				parameterCall.isZeitdatenAufAngelegteLoseBuchbar());		
	}
	
	public FilterKriterium getFilterStatus(List<ProductionStatus> status) {
		if(status == null || status.size() == 0) return null;
		
		List<String> ejbStatus = new ArrayList<String>();
		for (ProductionStatus productionStatus : status) {
			ejbStatus.add(productionStatus.getText());
		}

		return FilterDslBuilder
				.create("flrlos.status_c_nr")
				.inString(ejbStatus).build();
	}
	
	public FilterKriterium getFilterStatus(ProductionStatus[] status) {
		if(status == null || status.length == 0) return null;
		
		List<String> ejbStatus = new ArrayList<String>();
		for (ProductionStatus productionStatus : status) {
			ejbStatus.add(productionStatus.getText());
		}

		return FilterDslBuilder
				.create("flrlos.status_c_nr")
				.inString(ejbStatus).build();
	}
}
