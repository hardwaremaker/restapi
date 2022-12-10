package com.heliumv.factory.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IDruckerCall;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.util.HvOptional;
import com.lp.util.EJBExceptionLP;

public class DruckerCall extends BaseCall<DruckerFac> implements IDruckerCall {
	private static Logger log = LoggerFactory.getLogger(DruckerCall.class) ;

	public DruckerCall() {
		super(DruckerFac.class);
	}

	@Override
	public ReportvarianteDto reportvarianteFindByCReportnameCVariante(String cReportname,
			String cReportnameVariante) {
		return getFac().reportvarianteFindByCReportnameCReportnameVariante(cReportname, cReportnameVariante);
	}
	
	private HvOptional<Integer> getVariante(String reportName, String reportNameVariante) {
		try {
			return HvOptional.of(
					reportvarianteFindByCReportnameCVariante(
							reportName, reportNameVariante).getIId());
		} catch(EJBExceptionLP ex) {
			log.error("EJBException on getVariante", ex);
			return HvOptional.empty();
		}
	}
	
	@Override
	public HvOptional<Integer> varianteAuftragzeitbestaetigungUnterschrift() {
		return getVariante(AuftragReportFac.REPORT_ZEITBESTAETIGUNG, 
				AuftragReportFac.REPORT_ZEITBESTAETIGUNG_UNTERSCHRIFT);
/*		
		Integer variante = null;
		
		try {
			return reportvarianteFindByCReportnameCVariante(
					"auft_zeitbestaetigung.jasper", "auft_zeitbestaetigung_unterschrift.jasper").getIId();
		} catch(EJBExceptionLP e) {
			
		}
		
		return variante;
*/		
	}
	
	@Override
	public HvOptional<Integer> varianteLieferscheinUnterschrift() {
		return getVariante(LieferscheinReportFac.REPORT_LIEFERSCHEIN, 
				LieferscheinReportFac.REPORT_LIEFERSCHEIN_UNTERSCHRIFT);
	}
}
