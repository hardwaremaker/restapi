package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import com.heliumv.types.JudgeEnum;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmabenutzerParameterDto;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.HvmarechtDto;

public interface IHvmaCall {
	List<HvmarechtDto> getHvmaRechte(HvmaLizenzEnum lizenz);
	List<HvmarechtDto> getHvmaRechte() throws RemoteException;
	
	/** 
	 * Eine Liste aller Client-Rechte die dem angemeldeten 
	 * Benutzer die f&uuml;r die HVMA-Apps zur Verf&uuml;gung stehen.</br>
	 * <p>Diese Liste enth&auml;lt maximal jene Rechte die auf App-Seite
	 * "notwendig" sind, in dem Sinne, dass auf Seite der App
	 * gewisse Men&uuml;punkte / Oberfl&auml;chenelemente nur dann 
	 * dargestellt werden sollen, wenn der Benutzer auch die Rechte daf&uuml;r
	 * hat.</p>
	 * <p>Der HELIUM V Server kann unabh&auml;ngig von diesen Rechten die
	 * Funktionalit&auml;t weiter einschr&auml;nken.</p>
	 *  
	 * @return eine (leere) Liste aller Rechte die dem angemeldeten
	 * Benutzer zur Verf&uuml;gung stehen.
	 * 
	 * @throws RemoteException
	 */
	List<JudgeEnum> getRechte() throws RemoteException;
	
	Integer synch(String source);
	HvmalizenzDto lizenzFindByPrimaryKey(Integer lizenzId);
	List<HvmabenutzerParameterDto> parameterMobil();
	List<String> getBelegStatusAuftrag();
	List<String> getBelegStatusLos();
	List<String> getBelegStatusProjekt();
	Integer getZielterminAuftrag();
	Integer getZielterminLos();
	Integer getZielterminProjekt();
	
	String getTaetigkeitAuftrag();
	String getTaetigkeitProjekt();
	Integer getTaetigkeitIdAuftrag() throws RemoteException;
	Integer getTaetigkeitIdProjekt() throws RemoteException;
}
