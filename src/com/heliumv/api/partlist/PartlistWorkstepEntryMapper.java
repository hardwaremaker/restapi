package com.heliumv.api.partlist;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IStuecklisteCall;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;

public class PartlistWorkstepEntryMapper {
	@Autowired
	private IStuecklisteCall stuecklisteCall;

	public PartlistWorkstepEntry mapEntry(StuecklistearbeitsplanDto arbeitsplanDto) {
		PartlistWorkstepEntry entry = new PartlistWorkstepEntry();
		entry.setId(arbeitsplanDto.getIId());
		entry.setWorkstepNumber(arbeitsplanDto.getIArbeitsgang());
		entry.setSetupTimeMs(arbeitsplanDto.getLRuestzeit());
		entry.setJobTimeMs(arbeitsplanDto.getLStueckzeit());
		entry.setComment(arbeitsplanDto.getCKommentar());
		entry.setMachineId(arbeitsplanDto.getMaschineIId());
		entry.setText(arbeitsplanDto.getXLangtext());
		entry.setWorkstepType(arbeitsplanDto.getAgartCNr());
		
		if (arbeitsplanDto.getApkommentarIId() != null) {
			ApkommentarDto apkommentarDto = stuecklisteCall.apkommentarFindByPrimaryKey(arbeitsplanDto.getApkommentarIId());
			entry.setWorkplanComment(apkommentarDto.getApkommentarsprDto() != null ? 
					apkommentarDto.getApkommentarsprDto().getCBez() : null);
		}
		
		return entry;
	}
}
