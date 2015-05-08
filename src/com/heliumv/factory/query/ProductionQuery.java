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
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IParameterCall;
import com.heliumv.tools.StringHelper;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ProductionQuery extends BaseQuery<ProductionEntry> {
	@Autowired
	private IParameterCall parameterCall ;
	@Autowired
	private IGlobalInfo globalInfo ;
	@Autowired
	private ProductionEntryTransformer productionEntryTransformer ;
	
	public ProductionQuery() {
		super(QueryParameters.UC_ID_LOS) ;
	//	setTransformer(new ProductionEntryTransformer()) ;
		setTransformer(productionEntryTransformer);
	}
	
	
	public ProductionQuery(IParameterCall parameterCall) throws NamingException {
		super(UUID.randomUUID().toString(), QueryParameters.UC_ID_LOS) ;
//		setTransformer(new ProductionEntryTransformer()) ;
		setTransformer(productionEntryTransformer) ;
		this.parameterCall = parameterCall ;
	}
	
			
	@Override
	protected List<FilterKriterium> getRequiredFilters() {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;

		filters.add(getMandantFilter()) ;	
		try {
			filters.add(FertigungFilterFactory.getInstance().createFKBebuchbareLosStatus(
					parameterCall.isZeitdatenAufErledigteBuchbar(), true, 
					parameterCall.isZeitdatenAufAngelegteLoseBuchbar())) ;
		} catch(Throwable t) {			
		}

		return filters ;
	}

	private FilterKriterium getMandantFilter() {
		return new FilterKriterium("flrlos.mandant_c_nr", true,
				StringHelper.asSqlString( globalInfo.getMandant()),
				FilterKriterium.OPERATOR_EQUAL, false);
	}
}
