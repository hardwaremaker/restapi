package com.heliumv.factory.loader;

import java.rmi.RemoteException;
import java.util.EnumSet;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentCategory;
import com.heliumv.api.item.IItemCommentService;
import com.heliumv.api.item.ItemCommentFilter;
import com.heliumv.api.item.ItemCommentMediaInfoEntryList;
import com.heliumv.api.production.ProductionEntry;
import com.heliumv.factory.IStuecklisteCall;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;

public class ProductionLoaderCommentsMedia implements IProductionLoaderAttribute {
	@Autowired
	private IItemCommentService itemCommentService;
	@Autowired
	private IStuecklisteCall stuecklisteCall;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		if (losDto.getStuecklisteIId() == null) {
			return entry;
		}

		try {
			StuecklisteDto stuecklisteDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());

			ItemCommentFilter commentFilter = itemCommentService.createDefaultCommentFilter();
			commentFilter.setDocCategories(EnumSet.of(DocumentCategory.PRODUCTION));

			ItemCommentMediaInfoEntryList comments = itemCommentService.getCommentsMedia(
					stuecklisteDto.getArtikelIId(), commentFilter);
			entry.setItemCommentMediaInfoEntries(comments);
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		
		return entry;
	}

}
