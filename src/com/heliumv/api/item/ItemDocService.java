package com.heliumv.api.item;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.purchaseinvoice.PurchaseInvoiceDocService;
import com.heliumv.factory.IArtikelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikel;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class ItemDocService implements IDocumentCategoryService {
	private static Logger log = LoggerFactory.getLogger(PurchaseInvoiceDocService.class) ;
	
	@Autowired
	private IArtikelCall artikelCall;

	public ItemDocService() {
	}

	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException {
		return null;
	}

	@Override
	public DocPath getDocPath(Integer id, String cnr) throws RemoteException {
		ArtikelDto artikelDto = findArtikel(id, cnr);
		if (artikelDto == null) return null;
		
		return getDocPath(artikelDto);
	}
	
	private DocPath getDocPath(ArtikelDto artikelDto) {
		return new DocPath(new DocNodeArtikel(artikelDto));
	}
	
	private ArtikelDto findArtikel(Integer id, String cnr) throws RemoteException {
		if (id != null) {
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(id);
			if (artikelDto == null) {
				log.error("Could not find item with id = " + id);
			}
			return artikelDto;
		}
		
		if (cnr != null) {
			ArtikelDto artikelDto = artikelCall.artikelFindByCNrOhneExc(cnr);
			if (artikelDto == null) {
				log.error("Could not find item with cnr = " + cnr);
			}
			return artikelDto;
		}
		
		return null;
	}
}
