package com.heliumv.api.staff;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IHvmaCall;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IPersonalCall;
import com.heliumv.factory.query.StaffQuery;
import com.heliumv.tools.FilterHelper;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.StringHelper;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.EJBExceptionLP;

/**
 * Funktionalit&auml;t rund um das Personal</br>
 * 
 * @author andi
 *
 */
@Service("hvStaffV1_1")
@Path("/api/v11/staff")
public class StaffApiV11 extends BaseApi implements IStaffApiV11 {
	@Autowired
	private StaffQuery staffQuery ;
	@Autowired
	private IPersonalCall personalCall;
	@Autowired 
	private IGlobalInfo globalInfo ;
	@Autowired 
	private StaffEntryMapper staffEntryMapper;
	@Autowired
	private IJudgeCall judgeCall;
	@Autowired
	private IHvmaCall hvmaCall;
	
	@GET
	@Path("/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<StaffEntry> getStaff(
			@QueryParam("userid") String userId,
			@QueryParam("limit") Integer limit,
			@QueryParam("startIndex") Integer startIndex) throws RemoteException, EJBExceptionLP, NamingException {
		List<StaffEntry> entries = new ArrayList<StaffEntry>() ;

		if(connectClient(userId) == null) return entries ;
	
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(buildFilterWithHidden(false)) ;
//		FilterBlock filterCrits = collector.createFilterBlock();
		
		QueryParameters params = staffQuery.getDefaultQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		
		QueryResult result = staffQuery.setQuery(params) ;
		entries = staffQuery.getResultList(result) ;			

		return entries ;
	}
	
	private FilterKriterium buildFilterWithHidden(Boolean withHidden) {
		return FilterHelper.createWithHidden(withHidden, PersonalFac.FLR_PERSONAL_B_VERSTECKT) ;
	}
	
	@GET
	@Path("/{userid}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public StaffEntry findStaff(
			@PathParam("userid") String userId,
			@QueryParam("forStaffId") Integer forStaffId,
			@QueryParam("forStaffCnr") String forStaffCnr,
			@QueryParam("forStaffIdCard") String forStaffIdCard) throws NamingException, RemoteException {
		if(connectClient(userId) == null) return null;
		
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				forStaffId, forStaffCnr, forStaffIdCard);
		if(!validator.validate()) return null;

		PersonalDto personalDto = validator.getStaffToUse();
		StaffEntry staffEntry = staffEntryMapper.mapEntry(personalDto);
		
		return staffEntry;
	}

	protected class ValidPersonalId {
		private Integer staffId ;
		private String staffCnr ;
		private Integer myPersonalId ;
		private Integer staffIdToUse ;
		private String staffIdCard;
		private PersonalDto staffToUse;

		public ValidPersonalId(Integer myPersonalId) {
			this.myPersonalId = myPersonalId ;
		}
		
		public ValidPersonalId(Integer myPersonalId, Integer forStaffId, String forStaffCnr) {			
			this(myPersonalId, forStaffId, forStaffCnr, null);
		}

		public ValidPersonalId(Integer myPersonalId, Integer forStaffId, String forStaffCnr, String forStaffIdCard) {			
			this.myPersonalId = myPersonalId ;
			this.staffId = forStaffId ;
			this.staffCnr = forStaffCnr ;
			this.staffIdToUse = null ;
			this.staffIdCard = forStaffIdCard;
		}

		public Integer getStaffIdToUse() {
			return staffIdToUse ;
		}
		
		public PersonalDto getStaffToUse() {
			return staffToUse;
		}
		
		public boolean validate(Integer forStaffId, String forStaffCnr) throws RemoteException, NamingException {
			return validate(forStaffId, forStaffCnr, null);
		}

		public boolean validate(Integer forStaffId, String forStaffCnr, String forStaffIdCard) throws RemoteException, NamingException {
			this.staffId = forStaffId ;
			this.staffCnr = forStaffCnr ;
			this.staffIdToUse = null ;
			this.staffIdCard = forStaffIdCard;
			return validate() ;
		}

		public boolean validate() throws RemoteException, NamingException {
			if(staffId != null) {
				PersonalDto forPers = personalCall.byPrimaryKeySmall(staffId) ;
				return validatePersonalDto("staffId", staffId.toString(), forPers) ;
			}
			
			if(staffCnr != null) {
				PersonalDto forPers = personalCall.byCPersonalnrMandantCNrOhneExc(staffCnr) ;
				return validatePersonalDto("staffCnr", staffCnr, forPers) ;
			}

			if (staffIdCard != null) {
				PersonalDto forPers = personalCall.byCAusweis(staffIdCard);
				return validatePersonalDto("staffIdCard", staffIdCard, forPers);
			}
			
			staffIdToUse = myPersonalId ;
			staffToUse = personalCall.byPrimaryKeySmall(myPersonalId);
			return true ;
		}
		
		private boolean validatePersonalDto(String fieldName, String value, PersonalDto forPers) throws NamingException {
			if(forPers != null) {
				if(judgeCall.hasPersSichtbarkeitAlle()) {
					staffIdToUse = forPers.getIId() ;
					staffToUse = forPers;
					return true ;
				}

				if(judgeCall.hasPersSichtbarkeitAbteilung()) {
					PersonalDto mePers = personalCall.byPrimaryKeySmall(myPersonalId) ;
					if(mePers.getKostenstelleIIdAbteilung() != null) {
						if(mePers.getKostenstelleIIdAbteilung().equals(
								forPers.getKostenstelleIIdAbteilung())){
							staffIdToUse = forPers.getIId() ;
							staffToUse = forPers;
							return true ;
						}						
					}
				}				
			}

			respondBadRequest(fieldName, value);
			return false ;
		}
	}
	

	@Override
	@POST
	@Path("/synch")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void synch(
			SynchEntry entry) throws NamingException, RemoteException {
		HvValidateBadRequest.notNull(entry, "entry");
		HvValidateBadRequest.notEmpty(entry.getUserId(), "userId");
		HvValidateBadRequest.notEmpty(entry.getWhere(), "where");
		if(connectClient(entry.getUserId()) == null) return;
		
		hvmaCall.synch(StringHelper.trimCut(entry.getWhere(), 80));
	}
}
