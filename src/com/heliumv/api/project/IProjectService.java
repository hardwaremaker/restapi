package com.heliumv.api.project;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

public interface IProjectService {
	List<ProjectEntry> getProjects(Integer limit, Integer startIndex, 
			String filterCnr, String filterCompany,
			Boolean filterMyOpen, Boolean filterWithHidden) throws RemoteException, NamingException ;
	List<ProjectEntry> getMyProjects(Integer tageZieldatum,
			List<String> stati) throws RemoteException, NamingException;
}
