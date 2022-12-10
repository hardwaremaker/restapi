package com.heliumv.api.item;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.EnumSet;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentCategory;
import com.heliumv.factory.IArtikelkommentarCall;
import com.heliumv.tools.CollectionTools;
import com.heliumv.tools.ISelect;
import com.heliumv.tools.SelectChain;
import com.heliumv.types.MimeTypeEnum;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;

public class ItemCommentService implements IItemCommentService {
	@Autowired
	private IArtikelkommentarCall artikelkommentarCall;
	@Autowired
	private ItemCommentMediaInfoEntryMapper itemCommentMediaInfoEntryMapper;

	@Override
	public ItemCommentFilter createDefaultCommentFilter() {
		ItemCommentFilter commentFilter = new ItemCommentFilter();
		commentFilter.setMimeTypes(EnumSet.of(MimeTypeEnum.IMAGEJPEG, MimeTypeEnum.IMAGEPNG, MimeTypeEnum.APPPDF));
		
		return commentFilter;
	}
	
	@Override
	public ItemCommentMediaInfoEntryList getCommentsMedia(Integer itemId) throws RemoteException, NamingException {
		return getCommentsMedia(itemId, new ItemCommentFilter());
	}

	@Override
	public ItemCommentMediaInfoEntryList getCommentsMedia(Integer itemId, ItemCommentFilter filter) throws RemoteException, NamingException {
		Collection<ArtikelkommentarDto> artikelkommentare = artikelkommentarCall.artikelkommentarFindByArtikelIIdFull(itemId);
		ISelect<ArtikelkommentarDto> kommentarSelector = new SelectChain<ArtikelkommentarDto>()
				.and(new MimeTypeSelector(filter))
				.and(new DocumentCategorySelector(filter));
		artikelkommentare = CollectionTools.select(artikelkommentare, kommentarSelector);
		
		ItemCommentMediaInfoEntryList comments = new ItemCommentMediaInfoEntryList();
		for (ArtikelkommentarDto kommentar : artikelkommentare) {
			ItemCommentMediaInfoEntry entry = itemCommentMediaInfoEntryMapper.mapEntry(kommentar);
			comments.getEntries().add(entry);
		}
		return comments;
	}
	
	@Override
	public ItemCommentMediaInfoEntryList getCommentsMediaContent(
			Integer itemId, ItemCommentFilter filter) throws RemoteException, NamingException {
		Collection<ArtikelkommentarDto> artikelkommentare = artikelkommentarCall.artikelkommentarFindByArtikelIIdFull(itemId);
		ISelect<ArtikelkommentarDto> kommentarSelector = new SelectChain<ArtikelkommentarDto>()
				.and(new MimeTypeSelector(filter))
				.and(new DocumentCategorySelector(filter));
		artikelkommentare = CollectionTools.select(artikelkommentare, kommentarSelector);
		
		ItemCommentMediaInfoEntryList comments = new ItemCommentMediaInfoEntryList();
		for (ArtikelkommentarDto kommentar : artikelkommentare) {
			ItemCommentMediaInfoEntry entry = itemCommentMediaInfoEntryMapper.mapEntryContent(kommentar);
			comments.getEntries().add(entry);
		}
		return comments;
	}

	private abstract class ItemCommentSelector  implements ISelect<ArtikelkommentarDto> {
		private ItemCommentFilter itemCommentFilter;
		public ItemCommentSelector(ItemCommentFilter itemCommentFilter) {
			this.itemCommentFilter = itemCommentFilter;
		}
		protected ItemCommentFilter getItemCommentFilter() {
			return itemCommentFilter;
		}
	}
	
	private class MimeTypeSelector extends ItemCommentSelector {
		public MimeTypeSelector(ItemCommentFilter itemCommentFilter) {
			super(itemCommentFilter);
		} 
		
		public boolean select(ArtikelkommentarDto element) {
			MimeTypeEnum mimeType = getMimeType(element);
			return mimeType != null && getItemCommentFilter().getMimeTypes().contains(mimeType);
		}
	}

	private MimeTypeEnum getMimeType(ArtikelkommentarDto kommentar) {
		try {
			MimeTypeEnum mimeType = MimeTypeEnum.fromString(kommentar.getDatenformatCNr());
			return mimeType;
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	private class DocumentCategorySelector extends ItemCommentSelector {
		public DocumentCategorySelector(ItemCommentFilter itemCommentFilter) {
			super(itemCommentFilter);
		}

		public boolean select(ArtikelkommentarDto element) {
			EnumSet<DocumentCategory> filterDocCategories = getItemCommentFilter().getDocCategories();
			if (filterDocCategories.isEmpty()) {
				return true;
			}
			if (element.getArtikelkommentardruckDto() == null) {
				return false;
			}
			
			for (ArtikelkommentardruckDto druckDto : element.getArtikelkommentardruckDto()) {
				DocumentCategory docCategory = getDocumentCategory(druckDto);
				if (docCategory != null && filterDocCategories.contains(docCategory)) {
					return true;
				}
			}
			return false;
		}
	}

	private DocumentCategory getDocumentCategory(ArtikelkommentardruckDto druckDto) {
		try {
			return DocumentCategory.fromString(druckDto.getBelegartCNr());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
}
