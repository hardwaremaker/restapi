package com.heliumv.factory;

import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.util.HvOptional;

public interface IDruckerCall {
	ReportvarianteDto reportvarianteFindByCReportnameCVariante(
			String cReportname, String cReportnameVariante);

	HvOptional<Integer> varianteAuftragzeitbestaetigungUnterschrift();
	HvOptional<Integer> varianteLieferscheinUnterschrift();
}
