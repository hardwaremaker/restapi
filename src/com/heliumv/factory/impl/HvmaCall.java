package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IHvmaCall;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.types.JudgeEnum;
import com.lp.server.personal.service.HvmaFac;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmabenutzerParameterDto;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.HvmarechtDto;

public class HvmaCall extends BaseCall<HvmaFac> implements IHvmaCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	@Autowired
	private IJudgeCall judgeCall;
	
	public HvmaCall() {
		super(HvmaFac.class);
	}
	
	@Override
	public List<HvmarechtDto> getHvmaRechte(HvmaLizenzEnum lizenz) {
		String lizenzCnr = lizenz.getText();
		return getFac().getHvmaRechte(lizenzCnr, globalInfo.getTheClientDto());
	}
	
	@Override
	public Integer synch(String source) {
		return getFac().createHvmasync(source, globalInfo.getTheClientDto());
	}
	
	@Override
	public HvmalizenzDto lizenzFindByPrimaryKey(Integer lizenzId) {
		return getFac().hvmalizenzFindByPrimaryKey(lizenzId);
	}
	
	@Override
	public List<HvmarechtDto> getHvmaRechte() throws RemoteException {
		return getFac().getHvmaRechte(globalInfo.getTheClientDto());
	}
	
	@Override
	public List<HvmabenutzerParameterDto> parameterMobil() {
		return getFac().parameterMobil(globalInfo.getTheClientDto());
	}
	
	@Override
	public String getTaetigkeitAuftrag() {
		return getFac().getTaetigkeitAuftrag(globalInfo.getTheClientDto());
	}
	
	@Override
	public Integer getTaetigkeitIdAuftrag() throws RemoteException {
		return getFac().getTaetigkeitIdAuftrag(globalInfo.getTheClientDto());
	}

	@Override
	public String getTaetigkeitProjekt() {
		return getFac().getTaetigkeitProjekt(globalInfo.getTheClientDto());
	}
	
	@Override
	public Integer getTaetigkeitIdProjekt() throws RemoteException {
		return getFac().getTaetigkeitIdProjekt(globalInfo.getTheClientDto());
	}

	@Override
	public Integer getZielterminAuftrag() {
		return getFac().getZielterminAuftrag(globalInfo.getTheClientDto());
	}
	@Override
	public Integer getZielterminLos() {
		return getFac().getZielterminLos(globalInfo.getTheClientDto());
	}
	@Override
	public Integer getZielterminProjekt() {
		return getFac().getZielterminProjekt(globalInfo.getTheClientDto());
	}
	
	@Override
	public List<String> getBelegStatusAuftrag() {
		return getFac().getBelegStatusAuftrag(globalInfo.getTheClientDto());
	}
	@Override
	public List<String> getBelegStatusLos() {
		return getFac().getBelegStatusLos(globalInfo.getTheClientDto());
	}
	@Override
	public List<String> getBelegStatusProjekt() {
		return getFac().getBelegStatusProjekt(globalInfo.getTheClientDto());
	}
	
	@Override
	public List<JudgeEnum> getRechte() throws RemoteException {
		List<JudgeEnum> judges = new ArrayList<JudgeEnum>();
		
		if(judgeCall.hasPersZeiterfassungDarfMonatsabrechnungDrucken()) {
			judges.add(JudgeEnum.PrintMonthlyReport);
		}
		return judges;
	}
}
