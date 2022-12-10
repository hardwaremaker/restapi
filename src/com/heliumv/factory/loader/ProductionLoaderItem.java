package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.api.production.ProductionEntry;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IStuecklisteCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;

public class ProductionLoaderItem implements IProductionLoaderAttribute {
	
	@Autowired
	private IStuecklisteCall stuecklisteCall;
	
	@Autowired
	private IArtikelCall artikelCall;
	
	@Autowired
	private ItemEntryMapper itemEntryMapper ;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		if (losDto.getStuecklisteIId() == null) return entry;
		
		try {
			StuecklisteDto stuecklisteDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(stuecklisteDto.getArtikelIId());
			if (artikelDto == null) return entry;
			artikelDto.setArtikelsprDto(artikelCall.artikelSprFindByArtikelIIdOhneExc(artikelDto.getIId()));

			entry.setItemEntry(itemEntryMapper.mapV1EntrySmall(artikelDto));
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}

		return entry;
	}

}
