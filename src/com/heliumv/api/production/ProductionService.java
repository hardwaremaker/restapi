package com.heliumv.api.production;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.api.item.ItemV1Entry;
import com.heliumv.api.stock.StockInfoEntryList;
import com.heliumv.api.stock.StockInfoEntryMapper;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IFertigungCallJudge;
import com.heliumv.factory.IFertigungServiceCall;
import com.heliumv.factory.ILagerCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.util.EJBExceptionLP;

public class ProductionService implements IProductionService {

	@Autowired
	private IFertigungCallJudge fertigungCall ;	
	@Autowired
	private IArtikelCall artikelCall ;
	@Autowired
	private ProductionEntryMapper productionEntryMapper;
	@Autowired
	private StockInfoEntryMapper stockInfoEntryMapper;
	@Autowired
	private ILagerCall lagerCall ;
	@Autowired
	private ItemEntryMapper itemEntryMapper;
	@Autowired
	private IFertigungServiceCall fertigungServiceCall;
	@Autowired
	private TestPlanEntryMapper testPlanEntryMapper;

	@Override
	public ProductionTargetMaterialEntryList getTargetMaterials(Integer productionid, Boolean addStockPlaceInfos)
			throws RemoteException {
		LossollmaterialDto[] dtos = fertigungCall.lossollmaterialFindByLosIIdOrderByISort(productionid);
		return getTargetMaterials(dtos, addStockPlaceInfos);
	}

	public ProductionTargetMaterialEntryList getTargetMaterials(LossollmaterialDto[] dtos, 
			Boolean addStockPlaceInfos) throws RemoteException {
		ProductionTargetMaterialEntryList list = new ProductionTargetMaterialEntryList();
		for (LossollmaterialDto lossollmaterialDto : dtos) {
			ProductionTargetMaterialEntry entry = getTargetMaterialImpl(lossollmaterialDto, addStockPlaceInfos);
			list.getEntries().add(entry);
		}
		return list ;
	}

	private ProductionTargetMaterialEntry getTargetMaterialImpl(LossollmaterialDto lossollmaterialDto,
			Boolean addStockPlaceInfos) throws RemoteException {
		ProductionTargetMaterialEntry entry = productionEntryMapper.mapEntry(lossollmaterialDto);
		ArtikelDto artikelDto = findItemByIId(lossollmaterialDto.getArtikelIId());
		entry.setItemEntry(getItemEntry(artikelDto, lossollmaterialDto.getLosIId(), addStockPlaceInfos));
		entry.setAmountIssued(fertigungCall.getAusgegebeneMenge(lossollmaterialDto.getIId()));
		return entry;
	}
	
	private ArtikelDto findItemByIId(Integer iId) throws RemoteException {
		return artikelCall.artikelFindByPrimaryKeySmallOhneExc(iId);
	}
	
	private ItemV1Entry getItemEntry(ArtikelDto artikelDto, Integer losIId, Boolean addStockPlaceInfos) throws RemoteException {
		if (artikelDto == null) {
			return null;
		}
		
		ItemV1Entry itemV1Entry = itemEntryMapper.mapV1EntrySmall(artikelDto);
		if (Boolean.TRUE.equals(addStockPlaceInfos)) {
			addStockPlaceInfos(itemV1Entry, losIId);
		}
		return itemV1Entry;
	}

	private void addStockPlaceInfos(ItemV1Entry itemV1Entry, Integer losIId) throws RemoteException {
		LoslagerentnahmeDto[] loslaeger = fertigungCall.loslagerentnahmeFindByLosIId(losIId);
		if (loslaeger == null || loslaeger.length < 1)
			return;
		
		StockInfoEntryList stockInfoList = new StockInfoEntryList();
		for (LoslagerentnahmeDto loslager : loslaeger) {
			if (lagerCall.hatRolleBerechtigungAufLager(loslager.getLagerIId())) {
				List<LagerplatzDto> lagerplatzDtos = 
						lagerCall.lagerplatzFindByArtikelIIdLagerIIdOhneExc(itemV1Entry.getId(), loslager.getLagerIId());
				stockInfoList.getEntries().addAll(stockInfoEntryMapper.mapEntries(lagerplatzDtos));
				itemV1Entry.setStockplaceInfoEntries(stockInfoList);
			}
		}
	}
	
	@Override
	public TestPlanEntryList getTestPlanEntries(Integer productionId) throws RemoteException {
		TestPlanEntryList list = new TestPlanEntryList();
		List<LospruefplanDto> dtos = fertigungServiceCall.lospruefplanFindByLosId(productionId);
		for (LospruefplanDto dto : dtos) {
			addTestPlanEntry(list.getEntries(), dto);
		}

		return list;
	}
		
	private void addTestPlanEntry(List<TestPlanEntry> entries, LospruefplanDto pruefplanDto) throws RemoteException {
		try {
			TestPlanEntry entry = testPlanEntryMapper.mapEntry(pruefplanDto);
			entries.add(entry);
		} catch (IllegalArgumentException ex) {
			// Aktuell ignorieren
		}
	}
}
