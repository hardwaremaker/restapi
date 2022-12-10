package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.invoice.InvoiceEntry;
import com.heliumv.api.invoice.InvoiceEntryTransformer;
import com.heliumv.factory.IParameterCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungQueryResult;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterDslLikeStringValue.Wildcard;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class InvoiceQuery extends BaseQuery<InvoiceEntry> {
//	@Autowired
//	private IGlobalInfo globalInfo ;
	@Autowired
	private IParameterCall parameterCall ;

	public InvoiceQuery() {
		super(QueryParameters.UC_ID_RECHNUNG) ;
	}
	
	@Override
	protected List<InvoiceEntry> transform(QueryResult result) {
		if(result.hasFlrData()) {
			prepareTransformer((RechnungQueryResult) result) ;
		}
		return super.transform(result);
	}
	
	private void prepareTransformer(RechnungQueryResult result) {
		InvoiceEntryTransformer transformer = (InvoiceEntryTransformer) getTransformer() ;
		transformer.setFlrData(result.getFlrData());
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters()
			throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(filterMandant()) ;
		filters.add(getFilterRechnungsart()) ;
		return filters ;
	}
	
	private FilterKriterium getFilterRechnungsart() {
		return FilterDslBuilder.create(
				RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "." + RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR)
			.equal(RechnungFac.RECHNUNGTYP_RECHNUNG).build() ;		
	}
	
	public List<FilterKriterium> getFilterCnr(String cnr) {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		if(!StringHelper.isEmpty(cnr)) {
			filters.add(
				FilterDslBuilder.create(RechnungFac.FLR_RECHNUNG_C_NR).like(cnr).leading().build()) ;
		}
		return filters ;
	}
	
	public List<FilterKriterium> getFilterOpen(Boolean withOpen) {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		if(withOpen == null || withOpen.equals(Boolean.FALSE)) {
			return filters ;
		}
		filters.add(
				FilterDslBuilder.create(RechnungFac.FLR_RECHNUNG_STATUS_C_NR)
					.not().in(new String[]{RechnungFac.STATUS_STORNIERT, RechnungFac.STATUS_BEZAHLT}).build()) ;
//		filters.add(
//				FilterDslBuilder.create(
//					RechnungFac.FLR_RECHNUNG_STATUS_C_NR).equal(RechnungFac.STATUS_OFFEN).build()) ;
		return filters ;
	}

	public List<FilterKriterium> getFilterProject(String projekt) {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		if(!StringHelper.isEmpty(projekt)) {
			filters.add(
				FilterDslBuilder.create("c_bez").like(projekt).ignoreCase().build()) ;
		}
		return filters ;
	}
	
	public List<FilterKriterium> getFilterStatisticsAddress(String address) {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		if(!StringHelper.isEmpty(address)) {
			filters.add(
				FilterDslBuilder.create(RechnungFac.FLR_RECHNUNG_FLRKUNDE + "."
						+ KundeFac.FLR_KONTO + ".c_nr").like(address).ignoreCase().trailing().build()) ;
		}
		return filters ;
	}
	 
	public List<FilterKriterium> getFilterTextsearch(String textSearch) {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		if(!StringHelper.isEmpty(textSearch)) {
			filters.add(
				FilterDslBuilder.create("c_suche").like(textSearch).ignoreCase().build()) ;
		}
		return filters ;
	}
	
	public List<FilterKriterium> getFilterCustomername(String customerName) throws RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		if(!StringHelper.isEmpty(customerName)) {
			Wildcard position = parameterCall
					.isPartnerSucheWildcardBeidseitig() ? Wildcard.BOTH : Wildcard.TRAILING;
			filters.add(
				FilterDslBuilder.create(RechnungFac.FLR_RECHNUNG_FLRKUNDE
						+ "." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)
					.like(customerName).ignoreCase()
					.wildcard(position).build()) ;
		}

		return filters ;
	}	
}
