package com.heliumv.api;

import java.util.List;

import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public abstract class BaseFLRTransformerFeatureData<T, F> extends BaseFLRTransformer<T> {
	private F[] flrData ;
	
	public void setFlrData(F[] flrData) {
		this.flrData = flrData ;
	}

	@Override
	protected List<T> transformAll(List<T> resultEntries,
			Object[][] flrObjects, TableColumnInformation columnInformation) {
		super.transformAll(resultEntries, flrObjects, columnInformation);
		if(flrData != null) {
			if(flrData.length != flrObjects.length) {
				throw new IllegalArgumentException(
						"flrData.length (" + flrData.length + ") != flrObjects.length (" + flrObjects.length + ")") ;				
			}
			
			for(int i = 0; i < flrObjects.length; i++) {
				transformFlr(resultEntries.get(i), flrData[i]) ;
			}
		}
		return resultEntries ;
	}
	
	/**
	 * Die FLR-Daten in das Ergebnis (entry) stellen
	 * 
	 * @param entry der resultierende gesamte Datensatz
	 * @param flrData die FLR-Daten
	 */
	protected abstract void transformFlr(T entry, F flrData) ; 
}
