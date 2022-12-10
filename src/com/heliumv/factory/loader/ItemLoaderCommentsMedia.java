package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.IItemCommentService;
import com.heliumv.api.item.ItemCommentMediaInfoEntryList;
import com.heliumv.api.item.ItemEntryInternal;
import com.lp.server.artikel.service.ArtikelDto;

public class ItemLoaderCommentsMedia implements IItemLoaderAttribute {
	@Autowired
	private IItemCommentService itemCommentService;

	@Override
	public ItemEntryInternal load(ItemEntryInternal itemEntry, ArtikelDto artikelDto) {
		try {
			ItemCommentMediaInfoEntryList comments = itemCommentService.getCommentsMedia(
					artikelDto.getIId(), itemCommentService.createDefaultCommentFilter());
			itemEntry.setItemCommentMediaInfoEntries(comments);
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		
		return itemEntry;
	}

}
