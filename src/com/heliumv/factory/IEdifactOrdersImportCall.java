package com.heliumv.factory;

import com.lp.server.auftrag.service.EdifactOrdersImportResult;
import com.lp.server.system.service.WebshopAuthHeader;

public interface IEdifactOrdersImportCall {

	EdifactOrdersImportResult importWebMessage(String content, WebshopAuthHeader webAuth);

}
