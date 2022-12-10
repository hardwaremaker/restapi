package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogMessageEntry {
	private LogSeverity severity;
	private String msg;
	private long time;
	
	/**
	 * Der Schweregrad der Nachricht
	 * 
	 * @return der Schweregrad der Nachricht
	 */
	public LogSeverity getSeverity() {
		return severity;
	}
	public void setSeverity(LogSeverity severity) {
		this.severity = severity;
	}
	
	/**
	 * Der Nachrichtentext
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * Die Zeit in ms zu der die Nachricht erzeugt wurde
	 *
	 * @return
	 */
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
