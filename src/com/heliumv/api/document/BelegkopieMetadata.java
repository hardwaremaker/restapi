package com.heliumv.api.document;

import com.lp.server.system.jcr.service.JCRDocFac;

public class BelegkopieMetadata extends DocumentMetadata {
	public BelegkopieMetadata() {
		super(JCRDocFac.DEFAULT_KOPIE_BELEGART, "", 
				JCRDocFac.DEFAULT_ARCHIV_GRUPPE, JCRDocFac.SECURITY_ARCHIV);
	}
}
