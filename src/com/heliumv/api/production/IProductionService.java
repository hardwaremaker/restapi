package com.heliumv.api.production;

import java.rmi.RemoteException;

import com.lp.server.fertigung.service.LossollmaterialDto;

public interface IProductionService {

	ProductionTargetMaterialEntryList getTargetMaterials(Integer productionid, Boolean addStockPlaceInfos) throws RemoteException;

	ProductionTargetMaterialEntryList getTargetMaterials(LossollmaterialDto[] dtos, Boolean addStockPlaceInfos) throws RemoteException;

	TestPlanEntryList getTestPlanEntries(Integer productionId) throws RemoteException;
}
