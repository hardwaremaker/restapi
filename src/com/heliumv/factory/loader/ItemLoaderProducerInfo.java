package com.heliumv.factory.loader;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryInternal;
import com.heliumv.api.item.ProducerInfoEntry;
import com.heliumv.factory.IArtikelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.HerstellerDto;

public class ItemLoaderProducerInfo implements IItemLoaderAttribute {

	@Autowired
	private IArtikelCall artikelCall ;

	@Override
	public ItemEntryInternal load(ItemEntryInternal entry, ArtikelDto artikelDto) {
		ProducerInfoEntry producerEntry = new ProducerInfoEntry() ;
		producerEntry.setProducerId(artikelDto.getHerstellerIId());
		producerEntry.setItemCnr(artikelDto.getCArtikelnrhersteller());
		producerEntry.setItemDescription(artikelDto.getCArtikelbezhersteller());

		if(producerEntry.getProducerId() != null) {
			HerstellerDto herstellerDto = artikelCall.herstellerFindByPrimaryKey(producerEntry.getProducerId()) ;
			herstellerDto.getPartnerIId() ;
			producerEntry.setBarcodeLeadIn(herstellerDto.getCLeadIn()) ;
		}
		entry.setProducerInfo(producerEntry);
		return entry;
	}
}
