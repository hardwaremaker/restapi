/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IAuftragpositionCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LocaleFac;

public class AuftragpositionCall extends BaseCall<AuftragpositionFac> implements IAuftragpositionCall {
	@Autowired
	private IGlobalInfo globalInfo;
	
	public AuftragpositionCall() {
		super(AuftragpositionFac.class) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(rechtOder = {RechteFac.RECHT_AUFT_AUFTRAG_CUD, RechteFac.RECHT_AUFT_AUFTRAG_R})	
	public AuftragpositionDto auftragpositionFindByPrimaryKeyOhneExc(Integer positionId) {
		return getFac().auftragpositionFindByPrimaryKeyOhneExc(positionId) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(rechtOder = {RechteFac.RECHT_AUFT_AUFTRAG_CUD, RechteFac.RECHT_AUFT_AUFTRAG_R})	
	public AuftragpositionDto[] auftragpositionFindByAuftragOffeneMenge(
			Integer auftragId) throws RemoteException {
		return getFac().auftragpositionFindByAuftragOffeneMenge(auftragId) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(rechtOder = {RechteFac.RECHT_AUFT_AUFTRAG_CUD, RechteFac.RECHT_AUFT_AUFTRAG_R})	
	public AuftragpositionDto[] auftragpositionFindByAuftrag(Integer auftragId) throws RemoteException {
		return getFac().auftragpositionFindByAuftrag(auftragId);
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(recht=RechteFac.RECHT_AUFT_AUFTRAG_CUD)	
	public Integer createAuftragposition(AuftragpositionDto abposDto) throws RemoteException {
		return getFac().createAuftragposition(abposDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(recht=RechteFac.RECHT_AUFT_AUFTRAG_CUD)	
	public void updateAuftragposition(AuftragpositionDto abposDto) throws RemoteException {
		getFac().updateAuftragposition(abposDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(recht=RechteFac.RECHT_AUFT_AUFTRAG_CUD)	
	public void removeAuftragposition(AuftragpositionDto abposDto) throws RemoteException {
		getFac().removeAuftragposition(abposDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_AUFTRAG)
	@HvJudge(recht=RechteFac.RECHT_AUFT_AUFTRAG_CUD)	
	public void sortiereNachArtikelnummer(Integer auftragId) throws RemoteException {
		getFac().sortiereNachArtikelnummer(auftragId, globalInfo.getTheClientDto());
	}
}
