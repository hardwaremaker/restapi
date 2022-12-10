package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.api.purchaseorder.PurchaseOrderProposalPositionEntry;
import com.heliumv.api.purchaseorder.PurchaseOrderProposalPositionEntryTransformer;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.BestellvorschlagQueryResult;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class PurchaseOrderProposalQuery extends BaseQuery<PurchaseOrderProposalPositionEntry> {

	protected PurchaseOrderProposalQuery() {
		super(QueryParameters.UC_ID_BESTELLVORSCHLAG);
	}

	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(filterMandant());
		return filters ;
	}
	
	@Override
	protected List<PurchaseOrderProposalPositionEntry> transform(QueryResult result) {
		if (result.hasFlrData()) {
			prepareTransformer((BestellvorschlagQueryResult)result);
		}
		return super.transform(result);
	}

	private void prepareTransformer(BestellvorschlagQueryResult result) {
		PurchaseOrderProposalPositionEntryTransformer transformer = (PurchaseOrderProposalPositionEntryTransformer)getTransformer();
		transformer.setFlrData(result.getFlrData());
	}

	public FilterKriterium getFilterItemCnr(String filterItemCnr) {
		if (filterItemCnr == null) return null;
		
		return FilterDslBuilder
				.create(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRARTIKEL + "." + ArtikelFac.FLR_ARTIKEL_C_NR)
				.like(filterItemCnr).trailing().build();
	}

	public FilterKriterium getFilterNoted(Boolean filterNoted) {
		if (filterNoted == null) return null;
		
		return FilterDslBuilder
				.create("b_vormerkung")
				.equal(Boolean.TRUE.equals(filterNoted) ? "1" : "0").build();
	}

}
