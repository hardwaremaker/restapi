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
package com.heliumv.api.project;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;

@Service("hvProject")
@Path("/api/v1/project/")
public class ProjectApi extends BaseApi implements IProjectApi {

	public final static int MAX_CONTENT_LENGTH_DOCUMENT = 30*1024*1024 ;

	@Autowired
	private IProjectService projectService ;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService projectDocService;
	
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ProjectEntryList getProjects(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit, 
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Filter.CNR) String filterCnr, 
			@QueryParam("filter_company") String filterCompany,
			@QueryParam("filter_myopen") Boolean filterMyOpen,			
			@QueryParam("filter_withCancelled") Boolean filterWithHidden) throws NamingException, RemoteException {
		if(null == connectClient(userId)) {
			return new ProjectEntryList() ;
		}
		
		List<ProjectEntry> entries = projectService.getProjects(limit, startIndex, 
				filterCnr, filterCompany, filterMyOpen, filterWithHidden) ;	
		return new ProjectEntryList(entries) ;
	}
	
	@POST
	@Path("{" + Param.PROJECTID + "}/document")
	public void createDocument(
			@PathParam(Param.PROJECTID) Integer projectId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(userId, MAX_CONTENT_LENGTH_DOCUMENT) == null) {
			return;
		}

		HvValidateBadRequest.notNull(projectId, Param.PROJECTID);
		HvValidateBadRequest.notNull(body, "body"); 
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(projectDocService, projectId, 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}
	
}
