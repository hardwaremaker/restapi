package com.heliumv.factory.loader;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.api.production.ProductionEntry;
import com.lp.server.fertigung.service.LosDto;

public class ProductionLoaderDocuments implements IProductionLoaderAttribute {
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService productionDocService;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		try {
			DocumentInfoEntryList docInfoEntries = documentService.getDocs(productionDocService, losDto.getIId(), null);
			entry.setDocumentInfoEntries(docInfoEntries);
		} catch (RemoteException e) {
		} catch (RepositoryException e) {
		} catch (IOException e) {
		}
		return entry;
	}

}
