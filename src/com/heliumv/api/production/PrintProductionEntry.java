package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class PrintProductionEntry extends BaseEntryId {
	private boolean printProductionpaper;
	private boolean printAnalysissheet;

	/**
	 * Wurde der Fertigungsbegleitschein gedruckt?
	 * @return true wenn der Fertigungsbegleitschein gedruckt wurde
	 */
	public boolean isPrintProductionpaper() {
		return printProductionpaper;
	}
	public void setPrintProductionpaper(boolean printPaper) {
		this.printProductionpaper = printPaper;
	}
	
	/** 
	 * Wurde die Ausgabeliste gedruckt?
	 * @return true wenn die Ausgabeliste gedruckt wurde
	 */
	public boolean isPrintAnalysissheet() {
		return printAnalysissheet;
	}
	public void setPrintAnalysissheet(boolean printAnalysisSheet) {
		this.printAnalysissheet = printAnalysisSheet;
	}
}
