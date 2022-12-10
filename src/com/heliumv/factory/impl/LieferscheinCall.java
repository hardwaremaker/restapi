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

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILieferscheinCall;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.BestaetigterLieferscheinDto;
import com.lp.server.lieferschein.service.ILieferscheinAviso;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;

public class LieferscheinCall extends BaseCall<LieferscheinFac> implements ILieferscheinCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public LieferscheinCall() {
		super(LieferscheinFac.class) ;
	}

	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinDto lieferscheinFindByPrimaryKey(Integer lieferscheinId) throws RemoteException {
		return lieferscheinFindByPrimaryKey(lieferscheinId, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinDto lieferscheinFindByPrimaryKey(
			Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		LieferscheinDto lieferscheinDto = getFac()
				.lieferscheinFindByPrimaryKeyOhneExc(lieferscheinId) ;
		if(lieferscheinDto == null) return lieferscheinDto ;
		
		if(!theClientDto.getMandant().equals(lieferscheinDto.getMandantCNr())) {
			return null ;
		}
		
		return lieferscheinDto ;
	}
	

	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinDto lieferscheinFindByCNr(String cnr) throws RemoteException {
		return getFac().lieferscheinFindByCNrMandantCNr(cnr, globalInfo.getTheClientDto().getMandant()) ;
	}

	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinDto lieferscheinFindByCNr(
			String cnr, String clientCnr) throws RemoteException {
		return getFac().lieferscheinFindByCNrMandantCNr(cnr, clientCnr) ;
	}
	
	@Override
	public ILieferscheinAviso createLieferscheinAviso(Integer lieferscheinId,
			TheClientDto theClientDto) throws RemoteException, NamingException {
		return getFac().createLieferscheinAviso(lieferscheinId, theClientDto) ;
	}

	@Override
	public String getLieferscheinAvisoAsString(LieferscheinDto lieferscheinDto, ILieferscheinAviso lieferscheinAviso,
			TheClientDto theClientDto) throws NamingException, RemoteException {
		return getFac().lieferscheinAvisoToString(lieferscheinDto, lieferscheinAviso, theClientDto) ;
//		return getFac().sendLieferscheinAviso(lieferscheinDto, theClientDto) ;
	}
	
	@Override
	public String createLieferscheinAvisoPost(Integer lieferscheinId,
			TheClientDto theClientDto) throws RemoteException, NamingException {
		return getFac().createLieferscheinAvisoPost(lieferscheinId, theClientDto);		
	}	
	
	@Override
	public String createLieferscheinAvisoToString(
			Integer lieferscheinId, TheClientDto theClientDto)
			throws RemoteException, NamingException {
		return getFac().createLieferscheinAvisoToString(lieferscheinId, theClientDto) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_CUD, RechteFac.RECHT_LS_LIEFERSCHEIN_R})
	public LieferscheinDto[] lieferscheinFindByAuftrag(Integer auftragId) throws RemoteException {
		return lieferscheinFindByAuftrag(auftragId, globalInfo.getTheClientDto()) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_CUD, RechteFac.RECHT_LS_LIEFERSCHEIN_R})
	public LieferscheinDto[] lieferscheinFindByAuftrag(Integer auftragId, TheClientDto theClientDto) throws RemoteException {
		LieferscheinDto[] lsDtos = getFac().lieferscheinFindByAuftrag(auftragId, theClientDto) ;
		if(lsDtos.length > 0) {
			if(!theClientDto.getMandant().equals(lsDtos[0].getMandantCNr())) {
				return new LieferscheinDto[0] ;
			}
		}
		
		return lsDtos ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinpositionDto getLieferscheinpositionByLieferscheinAuftragposition(
			Integer lieferscheinId, Integer auftragPositionId) throws RemoteException {
		return getFac().getLieferscheinpositionByLieferscheinAuftragposition(lieferscheinId, auftragPositionId) ;
	}	
	
	public void berechneAktiviereBelegControlled(Integer deliveryId) throws RemoteException {
		getFac().berechneAktiviereBelegControlled(deliveryId, globalInfo.getTheClientDto()) ;
	}
	
	public LieferscheinDto setupDefaultLieferschein(KundeDto kundeDto) throws RemoteException {
		return getFac().setupDefaultLieferschein(kundeDto, globalInfo.getTheClientDto()) ;
	}
	
	public Integer createLieferschein(LieferscheinDto lieferscheinDto) throws RemoteException {
		return getFac().createLieferschein(lieferscheinDto, globalInfo.getTheClientDto()) ;
	}
	
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNr(Integer kundeId)
		throws RemoteException {
		return getFac().lieferscheinFindByKundeIIdLieferadresseMandantCNr(kundeId, 
				globalInfo.getMandant(), globalInfo.getTheClientDto()) ;
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_R, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD})	
	public LieferscheinDto lieferscheinFindByPrimaryKeyOhneExc(Integer lieferscheinIId) {
		return getFac().lieferscheinFindByPrimaryKeyOhneExc(lieferscheinIId);
	}

	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(recht=RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)	
	public void archiveSignedResponse(
			BestaetigterLieferscheinDto bestaetigungDto) throws RemoteException {
		getFac().archiveSignedResponse(bestaetigungDto,  globalInfo.getTheClientDto());
	}
}
