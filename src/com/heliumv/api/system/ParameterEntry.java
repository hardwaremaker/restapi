package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;
import com.lp.util.EJBExceptionLP;


@XmlRootElement
public class ParameterEntry extends BaseEntryCnr {
	private String category ;
	private String datatype ;
	private String value ;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String asString() {
		if ("java.lang.String".equals(getDatatype())) {
			return value ;
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());		
	}
	
	public Boolean asBoolean() {
		if ("java.lang.Boolean".equals(getDatatype())
				|| "java.lang.Integer".equals(getDatatype())) {
			return new Boolean(Integer.parseInt(getValue()) != 0);
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());
	}

	public Integer asInteger() {
		if ("java.lang.Integer".equals(getDatatype())) {
			return new Integer(getValue());
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());		
	}
}
