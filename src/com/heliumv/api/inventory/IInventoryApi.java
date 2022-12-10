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
package com.heliumv.api.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import com.heliumv.api.item.InventoryEntry;
import com.lp.util.EJBExceptionLP;

public interface IInventoryApi {
	List<InventoryEntry> getOpenInventories(String userId) ;

	/**
	 * Einen Eintrag in der Inventurliste erzeugen
	 * 
	 * @param inventoryId die Id der Inventur
	 * @param itemId die Id des Artikels
	 * @param amount die Menge
	 * @param userId der Token des aktuellen Benutzers
	 * @param largeDifference ist true wenn gro&szlig;e Mengenabwechungen erlaubt sind
	 * @param identity die (optionale) Serien/Chargennummer sofern es ein entsprechender Artikel ist
	 * @throws IOException 
	 * @throws NamingException 
	 * @throws EJBExceptionLP 
	 * @throws RemoteException 
	 */
	void createInventoryEntry(
			Integer inventoryId,
			Integer itemId,
			BigDecimal amount,
			String userId,
			Boolean largeDifference,
			String identity) throws RemoteException, EJBExceptionLP, NamingException, IOException ;

	void updateInventoryEntry(
			Integer inventoryId,
			Integer itemId,
			BigDecimal amount,
			String userId,
			Boolean changeAmountTo,
			String identity) ;	
	
	void updateInventoryDataEntry(
			Integer inventoryId,
			String userId,
			InventoryDataEntry inventoryEntry,
			Boolean changeAmountTo) ;
	
	void createInventoryDataEntry(
			Integer inventoryId,
			InventoryDataEntry inventoryEntry,
			String userId,
			Boolean largeDifference) throws RemoteException, NamingException, IOException ;
}
