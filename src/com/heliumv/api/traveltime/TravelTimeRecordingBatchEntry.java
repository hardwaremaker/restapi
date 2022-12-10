package com.heliumv.api.traveltime;

import com.heliumv.api.todo.TodoEntryType;

public class TravelTimeRecordingBatchEntry {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	
	private Integer hvid;
	private TodoEntryType todoType;
	private String remark;
	private Integer carId;
	private String  plateNumber;
	private Integer mileage;
	private boolean startOfTravel;
	private Integer dailyAllowanceId;
	
	/**
	 * Das Jahr der Reise
	 * @return das Jahr (2019) der Reise
	 */
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	
	/** 
	 * Der Monat der Reise im Berech 1 - 12
	 * @return der Monat der Reise
	 */
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * Der Tag der Reise im Bereich 1 - 31
	 * @return der Tag der Reise
	 */
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Die Stunde der Reise im Bereich 0 - 23
	 * @return die Stunde der Reise
	 */
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * Die Minute der Reise im Bereich 0 - 59
	 * @return die Minute der Reise
	 */
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	/**
	 * Die (optionale) Sekunde der Reise im Bereich 0 - 59
	 * @return
	 */
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	
	/**
	 * Die Beleg-Id (des Auftrags)
	 * @return die Beleg-Id 
	 */
	public Integer getHvid() {
		return hvid;
	}
	public void setHvid(Integer hvid) {
		this.hvid = hvid;
	}
	
	/**
	 * Die Beleg-Art (Auftrag)
	 * @return die Belegart
	 */
	public TodoEntryType getTodoType() {
		return todoType;
	}
	public void setTodoType(TodoEntryType todoType) {
		this.todoType = todoType;
	}
	
	/**
	 * Ein (optionaler) Kommentar
	 * @return der Kommentar zu dieser Reise
	 */
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * Die (optionale) Fahrzeug-Id</br>
	 * <p>Wird ein Firmenfahrzeug verwendet, sollte die Id 
	 * des Fahrzeugs angegeben werden. Wird das private Fahrzeug,
	 * oder ein Mietfahrzeug verwendet, ist das Kennzeichen 
	 * anzugeben</p>
	 * @return die Id des Fahrzeugs
	 */
	public Integer getCarId() {
		return carId;
	}
	public void setCarId(Integer carId) {
		this.carId = carId;
	}
	
	/**
	 * Das (optionale) Kennzeichen des Fahrzeugs
	 * <p>Wird ein Firmenfahrzeug verwendet, sollte die Id 
	 * des Fahrzeugs angegeben werden. Wird das private Fahrzeug,
	 * oder ein Mietfahrzeug verwendet, ist das Kennzeichen 
	 * anzugeben und die Id leer zu lassen</p>
	 * @return
	 */
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	/**
	 * Der (optionale) Tachostand zum Zeitpunkt dieser Reise</br>
	 * <p>Er ist nur anzugeben, wenn man das Fahrzeug selbst
	 * gefahren hat (und es sich nat&uuml;rlich um ein Fahrzeug
	 * gehandelt hat). Bei Zug/Flugzeug ist der Tachostand 
	 * nicht anzugeben.</p>
	 * @return der Tachostand
	 */
	public Integer getMileage() {
		return mileage;
	}
	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	
	/**
	 * Handelt es sich um den Beginn/Weiterfahrt der Reise
	 * oder um das Ende?
	 * @return true wenn es sich um den Beginn/Weiterfahrt einer
	 * Reise handelt
	 */
	public boolean isStartOfTravel() {
		return startOfTravel;
	}
	public void setStartOfTravel(boolean startOfTravel) {
		this.startOfTravel = startOfTravel;
	}
	
	/**
	 * Die Id der zu verwendenden Di&auml;t
	 * @return die Id der Di&auml;t
	 */
	public Integer getDailyAllowanceId() {
		return dailyAllowanceId;
	}
	public void setDailyAllowanceId(Integer dailyAllowanceId) {
		this.dailyAllowanceId = dailyAllowanceId;
	}
}
