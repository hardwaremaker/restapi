package com.heliumv.api.production;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.BaseFLRTransformerFeatureData;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IParameterCall.AuslastungsZeitenberechnung;
import com.lp.server.fertigung.service.IOffeneAgsFLRData;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.util.EJBExceptionLP;

public class OpenWorkEntryTransformer extends BaseFLRTransformerFeatureData<OpenWorkEntry, IOffeneAgsFLRData> {
	private static Logger log = LoggerFactory.getLogger(OpenWorkEntryTransformer.class) ;
	
	@Autowired
	private IParameterCall parameterCall ;

	private AuslastungsZeitenberechnung calcType ;
	
	private AuslastungsZeitenberechnung getCalcType() {
		if(calcType == null) {
			try {
				calcType = parameterCall.getAuslastungszeitenBerechnung() ;
			} catch (RemoteException e) {
				log.error("RemoteException", e) ;
			} catch (EJBExceptionLP e) {
				log.error("EJBExceptionLP", e); 
			}
		}
		
		return calcType ;
	}
	
	@Override
	protected List<OpenWorkEntry> transformAll(
			List<OpenWorkEntry> resultEntries, Object[][] flrObjects,
			TableColumnInformation columnInformation) {
		calcType = null ;
		return super.transformAll(resultEntries, flrObjects, columnInformation);
	}
	
	@Override
	public OpenWorkEntry transformOne(Object[] flrObject,
			TableColumnInformation columnInformation) {
		OpenWorkEntry entry = new OpenWorkEntry() ;
		entry.setId((Integer) flrObject[0]) ;
		entry.setCustomerShortDescription((String) flrObject[1]) ;
		entry.setAbc((String) flrObject[2]) ; 
		entry.setProductionCnr((String) flrObject[3]) ;
		entry.setPartlistCnr((String) flrObject[5]);
		entry.setPartlistDescription((String) flrObject[6]) ;
		entry.setWorkNumber((Integer) flrObject[7]) ;
		entry.setWorkItemCnr((String) flrObject[8]) ;
		entry.setWorkItemDescription((String) flrObject[9]);
		entry.setWorkItemStartDate(((Date) flrObject[12]).getTime()) ;
		entry.setMachineOffsetMs((Integer) flrObject[13]);
		String kw = (String) flrObject[14] ;
		if(kw != null) {
			try {
				String s[] = kw.split("/") ;
				entry.setWorkItemStartCalendarYear(Integer.parseInt(s[1]));
				entry.setWorkItemStartCalendarWeek(Integer.parseInt(s[0])); 
			} catch(NumberFormatException e) {
				log.error("Converting '" + kw + "' failed with:", e) ;
			}
		}
		entry.setMachineCnr((String) flrObject[15]) ;
		entry.setMachineDescription((String) flrObject[16]) ;
		entry.setDuration((BigDecimal) flrObject[17]) ;
		entry.setTargetDuration(entry.getDuration());
		entry.setMaterialCnr((String) flrObject[18]) ;
		entry.setMaterialDescription((String) flrObject[19]) ;
		Double progress = (Double) flrObject[21] ;
		if(progress != null) {
			entry.setProgressPercent(new BigDecimal(progress)) ;
			
			if(AuslastungsZeitenberechnung.RESTZEIT.equals(getCalcType())) {
				BigDecimal d = entry.getTargetDuration().subtract(
						entry.getTargetDuration().multiply(entry.getProgressPercent().movePointLeft(2))).setScale(2, RoundingMode.HALF_EVEN) ;
				entry.setDuration(d) ;
			}
		}
		entry.setPriority((Integer) flrObject[22]);
		entry.setHasWorktime(new Boolean(flrObject[23] != null));
		entry.setMachineId((Integer) flrObject[24]) ;
		return entry ;
	}
	
	public void transformFlr(OpenWorkEntry entry, IOffeneAgsFLRData flrData) {
		if(flrData == null) return ;
		
		entry.setOrderId(flrData.getAuftragId());
		entry.setOrderCnr(flrData.getAuftragCnr());
		entry.setOverdue(flrData.isOverdue());
		entry.setOrderFinalDateMs(flrData.getFinalTermin());
		
		entry.setWorkItemShortDescription(flrData.getTaetigkeitArtikelKBez());
		entry.setProductionProjectNr(flrData.getLosProjekt());
		
		entry.setPartlistItemCnr(flrData.getPartlistItemCnr());
		entry.setPartlistItemDescription(flrData.getPartlistItemDescription());
		entry.setPartlistItemShortDescription(flrData.getPartlistItemShortDescription());
		
		entry.setActualTime(flrData.getActualTime());
		entry.setStarttimeMoveable(flrData.isStartTimeMoveable());
		entry.setFinishtimeMoveable(flrData.isFinishTimeMoveable());
		entry.setOpenQuantity(flrData.getOpenQuantity());
		entry.setProductionFinalDateMs(flrData.getLosEndeTermin());
	}
}
