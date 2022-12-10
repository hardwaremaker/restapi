package com.heliumv.factory.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.car.CarEntry;
import com.heliumv.api.car.CarEntryTransformer;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class CarQuery extends BaseQuery<CarEntry> {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	@Autowired
	private CarEntryTransformer carEntryTransformer ;
	
	public CarQuery() {
		super(QueryParameters.UC_ID_FAHRZEUG) ;
		setTransformer(carEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(filterMandant());
		filters.add(getFilterAuthorizedCars());
		return filters ;
	}
	
	private FilterKriterium getFilterAuthorizedCars() {
		return FilterDslBuilder
				.create(PersonalFac.FILTER_BERECHTIGTE_FAHRZEUGE)
				.equal(globalInfo.getTheClientDto().getIDPersonal())
				.build();
	}
}
