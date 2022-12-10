package com.heliumv.api.item;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IArtikelkommentarCall;
import com.heliumv.types.MimeTypeEnum;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.util.EJBExceptionLP;

public class ItemCommentMediaInfoEntryMapper {

	@Autowired
	private IArtikelkommentarCall artikelkommentarCall;
	
	public ItemCommentMediaInfoEntry mapEntry(ArtikelkommentarDto kommentarDto) {
		ItemCommentMediaInfoEntry entry = new ItemCommentMediaInfoEntry(kommentarDto.getIId());
		entry.setMimeType(MimeTypeEnum.fromString(kommentarDto.getDatenformatCNr()));
		entry.setiSort(kommentarDto.getISort());
		entry.setFilename(kommentarDto.getArtikelkommentarsprDto().getCDateiname());
		entry.setSize(new Long(kommentarDto.getArtikelkommentarsprDto().getOMedia().length));
		
		try {
			ArtikelkommentarartDto art = artikelkommentarCall.artikelkommentarartFindByPrimaryKey(kommentarDto.getArtikelkommentarartIId());
			entry.setCommentType(art.getCNr());
			entry.setCommentTypeDescription(art.getBezeichnung());
		} catch (EJBExceptionLP e) {
		} catch (RemoteException e) {
		}

		return entry;
	}

	public ItemCommentMediaInfoEntry mapEntryContent(ArtikelkommentarDto kommentarDto) {
		ItemCommentMediaInfoEntry entry = mapEntry(kommentarDto);
		entry.setContent(kommentarDto.getArtikelkommentarsprDto().getOMedia());
		return entry;
	}
}
