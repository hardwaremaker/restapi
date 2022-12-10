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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IVkPreisfindungCall;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.util.EJBExceptionLP;

public class VkPreisfindungCall extends BaseCall<VkPreisfindungFac> implements IVkPreisfindungCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	protected VkPreisfindungCall() {
		super(VkPreisfindungFac.class);
	}

	@Override
	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByPrimaryKey(
			Integer preislisteId) throws RemoteException {
		try {
			VkpfartikelpreislisteDto preislisteDto = getFac().vkpfartikelpreislisteFindByPrimaryKey(preislisteId) ;
			return preislisteDto ;
		} catch(EJBExceptionLP e) {
			if(e.getCode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
				return null ;
			}
			
			throw e ;
		}
	}

	@Override
	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByCnr(
			String preislisteCnr) throws RemoteException {
		try {
			VkpfartikelpreislisteDto preislisteDto = getFac()
					.vkpfartikelpreislisteFindByMandantCNrAndCNr(globalInfo.getMandant(), preislisteCnr);
			return preislisteDto ;
		} catch(EJBExceptionLP e) {
			if(e.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
				return null ;
			}
			
			throw e ;
		}
	}
	
	@Override
	public VkpfartikelpreislisteDto[] vkpfartikelpreislisteFindByMandant() throws RemoteException {
		return getFac().vkpfartikelpreislisteFindByMandantCNr(globalInfo.getMandant());
	}
	
	@Override
	public VkpreisfindungDto verkaufspreisfindung(Integer itemId, Integer customerId, BigDecimal amount, 
			java.sql.Date date, Integer pricelistId, Integer mwstsatzbezId, String currencyCnr) throws RemoteException  {
		VkpreisfindungDto dto = getFac().verkaufspreisfindung(itemId, customerId, amount, 
				date, pricelistId, mwstsatzbezId, currencyCnr, globalInfo.getTheClientDto()) ;
		return dto ;
	}

	@Override
	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
			Integer itemId, Date datGueltigkeit, Integer preislisteId) throws RemoteException {
		return getFac().vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(itemId, 
				datGueltigkeit, preislisteId, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public VkpfMengenstaffelDto vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
			Integer itemId, BigDecimal amount,
			Date datGueltigkeit, Integer preislisteId ) throws RemoteException {
		return getFac().vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
				itemId, amount, datGueltigkeit, preislisteId, globalInfo.getTheClientDto()) ;
	}
	
}
