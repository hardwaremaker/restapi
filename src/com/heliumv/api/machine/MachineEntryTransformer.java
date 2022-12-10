package com.heliumv.api.machine;

import com.heliumv.api.BaseFLRTransformerFeatureData;
import com.lp.server.personal.service.IMaschineFLRData;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class MachineEntryTransformer extends BaseFLRTransformerFeatureData<MachineEntry, IMaschineFLRData> {

	@Override
	public MachineEntry transformOne(Object[] flrObject,
			TableColumnInformation columnInformation) {
		MachineEntry entry = new MachineEntry(flrObject[0]) ;
		entry.setInventoryNumber((String) flrObject[2]) ;
		entry.setDescription((String) flrObject[3]) ;
		entry.setIdentificationNumber((String) flrObject[5]) ; 
		entry.setMachineGroupId((Integer) flrObject[9]) ;
		entry.setMachineGroupDescription((String) flrObject[10]) ;
		entry.setMachineGroupShortDescription((String) flrObject[11]);
		entry.setMachineGroupISort((Integer) flrObject[12]);
		
		return entry;
	}

	@Override
	protected void transformFlr(MachineEntry entry, IMaschineFLRData flrData) {
		if (flrData == null) return;
		
		entry.setPersonalIdStarter(flrData.getPersonalIdStarter());
		entry.setStarttime(flrData.getGestartetUm());
		entry.setProductionWorkplanId(flrData.getLossollarbeitsplanIId());
	}
}
