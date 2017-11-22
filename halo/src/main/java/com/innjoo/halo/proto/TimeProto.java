package com.innjoo.halo.proto;

import java.io.Serializable;

public class TimeProto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private short year;
	private byte month;
	private byte day;
	private byte hour;
	private byte min;
	private byte sec;
	private byte weekIndex; /* 0=Sunday */
	
	
	public TimeProto(short year, byte month, byte day, byte hour, byte min, byte sec, byte weekIndex) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.min = min;
		this.sec = sec;
		this.weekIndex = weekIndex;
	}
	public short getYear() {
		return year;
	}
	public void setYear(short year) {
		this.year = year;
	}
	public byte getMonth() {
		return month;
	}
	public void setMonth(byte month) {
		this.month = month;
	}
	public byte getDay() {
		return day;
	}
	public void setDay(byte day) {
		this.day = day;
	}
	public byte getHour() {
		return hour;
	}
	public void setHour(byte hour) {
		this.hour = hour;
	}
	public byte getMin() {
		return min;
	}
	public void setMin(byte min) {
		this.min = min;
	}
	public byte getSec() {
		return sec;
	}
	public void setSec(byte sec) {
		this.sec = sec;
	}
	public byte getWeekIndex() {
		return weekIndex;
	}
	public void setWeekIndex(byte weekIndex) {
		this.weekIndex = weekIndex;
	}
	@Override
	public String toString() {
		return "TimeProto [year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + ", min=" + min
				+ ", sec=" + sec + ", weekIndex=" + weekIndex + "]";
	}
	
	
	
	
}
