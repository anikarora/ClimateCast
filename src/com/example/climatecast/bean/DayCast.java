package com.example.climatecast.bean;

import java.io.Serializable;

public class DayCast implements Serializable {
	int monthDay;
	int month;
	int year;
	String dayDisplay;
	int temp_low_f, temp_low_c, temp_high_f, temp_high_c;
	String cloudStatus;
	String icon_url;

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getDayDisplay() {
		return dayDisplay;
	}

	public int getMonthDay() {
		return monthDay;
	}

	public void setMonthDay(int monthDay) {
		this.monthDay = monthDay;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setDayDisplay(String dayDisplay) {
		this.dayDisplay = dayDisplay;
	}

	public int getTemp_low_f() {
		return temp_low_f;
	}

	public void setTemp_low_f(int temp_low_f) {
		this.temp_low_f = temp_low_f;
	}

	public int getTemp_low_c() {
		return temp_low_c;
	}

	public void setTemp_low_c(int temp_low_c) {
		this.temp_low_c = temp_low_c;
	}

	public int getTemp_high_f() {
		return temp_high_f;
	}

	public void setTemp_high_f(int temp_high_f) {
		this.temp_high_f = temp_high_f;
	}

	public int getTemp_high_c() {
		return temp_high_c;
	}

	public void setTemp_high_c(int temp_high_c) {
		this.temp_high_c = temp_high_c;
	}

	public String getCloudStatus() {
		return cloudStatus;
	}

	public void setCloudStatus(String cloudStatus) {
		this.cloudStatus = cloudStatus;
	}
}
