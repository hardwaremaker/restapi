package com.heliumv.api.production;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.factory.IArtikelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.util.Helper;

public class ProductionWorkstepEntryMapper {

	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private ItemEntryMapper itemEntryMapper;
	
	public ProductionWorkstepEntry mapEntry(LossollarbeitsplanDto dto) throws RemoteException {
		ProductionWorkstepEntry entry = new ProductionWorkstepEntry();
		entry.setId(dto.getIId());
		entry.setWorkstepNumber(dto.getIArbeitsgangnummer());
		entry.setSetupTimeMs(dto.getLRuestzeit());
		entry.setJobTimeMs(dto.getLStueckzeit());
		entry.setComment(dto.getCKomentar());
		entry.setMachineId(dto.getMaschineIId());
		entry.setText(dto.getXText());
		entry.setWorkstepType(dto.getAgartCNr());
		entry.setDuration(dto.getNGesamtzeit());
		entry.setHasFinished(Helper.short2Boolean(dto.getBFertig()));
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(dto.getArtikelIIdTaetigkeit());
		entry.setItemEntry(artikelDto != null ? itemEntryMapper.mapV1EntrySmall(artikelDto) : null);

		return entry;
	}
}
