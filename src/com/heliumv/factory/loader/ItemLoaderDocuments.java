package com.heliumv.factory.loader;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.api.item.ItemEntryInternal;
import com.lp.server.artikel.service.ArtikelDto;

public class ItemLoaderDocuments implements IItemLoaderAttribute {

	@Autowired
	private IDocumentCategoryService itemDocService;
	@Autowired
	private IDocumentService documentService;
	
	@Override
	public ItemEntryInternal load(ItemEntryInternal entry, ArtikelDto artikelDto) {
		try {
			DocumentInfoEntryList docInfoEntries = documentService.getDocs(itemDocService, artikelDto.getIId(), null);
			entry.setDocumentInfoEntries(docInfoEntries);
		} catch (RemoteException e) {
		} catch (RepositoryException e) {
		} catch (IOException e) {
		}
		return entry;
	}

}
