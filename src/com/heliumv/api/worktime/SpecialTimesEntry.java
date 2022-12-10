package com.heliumv.api.worktime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SpecialTimesEntry {
	private SpecialTimesEnum timeType;
	private long fromDateMs;
	private long toDateMs;
	private boolean halfDay;
	
	public SpecialTimesEnum getTimeType() {
		return timeType;
	}
	public void setTimeType(SpecialTimesEnum timeType) {
		this.timeType = timeType;
	}
	public long getFromDateMs() {
		return fromDateMs;
	}
	public void setFromDateMs(long fromDateMs) {
		this.fromDateMs = fromDateMs;
	}
	public long getToDateMs() {
		return toDateMs;
	}
	public void setToDateMs(long toDateMs) {
		this.toDateMs = toDateMs;
	}
	public boolean isHalfDay() {
		return halfDay;
	}
	public void setHalfDay(boolean halfDay) {
		this.halfDay = halfDay;
	}
}
