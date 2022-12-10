package com.heliumv.factory.impl;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IEdifactOrdersImportCall;
import com.lp.server.auftrag.service.EdifactOrdersImportFac;
import com.lp.server.auftrag.service.EdifactOrdersImportResult;
import com.lp.server.system.service.WebshopAuthHeader;

public class EdifactOrdersImportCall extends 
	BaseCall<EdifactOrdersImportFac> implements IEdifactOrdersImportCall {
	public EdifactOrdersImportCall() {
		super(EdifactOrdersImportFac.class);
	}

	@Override
	public EdifactOrdersImportResult importWebMessage(
			String content, WebshopAuthHeader webAuth) {
		return getFac().importWebMessage(content, webAuth);
	}
}
