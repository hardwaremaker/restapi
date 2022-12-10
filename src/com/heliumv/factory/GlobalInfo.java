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
package com.heliumv.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lp.server.system.service.TheClientDto;

public class GlobalInfo implements IGlobalInfo {
	private static Logger log = LoggerFactory.getLogger(GlobalInfo.class) ;

	private TheClientDto theClientDto ;
	
	@Override
	public TheClientDto getTheClientDto() {
		if(theClientDto == null) {
			log.warn("globalInfo.getTheClientDto() == null " + this.toString());
		}
		return theClientDto ;
	}

	@Override
	public void setTheClientDto(TheClientDto theClientDto) {
		this.theClientDto = theClientDto ;
	}
	
	@Override
	public String getMandant() {
		if(theClientDto == null) {
			log.warn("globalInfo.getMandant() theClientDto == null " + this.toString());
			return null;
		}		
		return theClientDto.getMandant() ;
	}
	
	@Override
	public String getArbeitsplatzname() {
		if(theClientDto == null) {
			log.warn("globalInfo.getArbeitsplatzname() theClientDto == null " + this.toString());
			return null;	
		}
		// regex: | ist ein Controlzeichen, daher escapen mit \. Weil \ escape einleitung (java) \\ 
		String[] tokens = theClientDto.getBenutzername().split("\\|");
		if(tokens.length < 2) {
			log.warn("Benutzername-Syntax unbekannt '" + theClientDto.getBenutzername() + "'.");
			return null;
		}
		return tokens[1].trim();
	}
}
