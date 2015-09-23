package com.example.climatecast.bean;

import java.io.Serializable;

public class TimeCast implements Serializable {

	String sCivilTime;
	int Hour, temp_f, temp_c, humidity;
	String icon_url;
	boolean isDegreeCelsius;
	String cityCode, cityName, pretty;
	String rain, tempLow, tempHigh, interval;

	public String getTempLow() {
		return tempLow;
	}

	public void setTempLow(String tempLow) {
		this.tempLow = tempLow;
	}

	public String getTempHigh() {
		return tempHigh;
	}

	public void setTempHigh(String tempHigh) {
		this.tempHigh = tempHigh;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getRain() {
		return rain;
	}

	public void setRain(String rain) {
		this.rain = rain;
	}

	public String getPretty() {
		return pretty;
	}

	public void setPretty(String pretty) {
		this.pretty = pretty;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getsCivilTime() {
		return sCivilTime;
	}

	public void setsCivilTime(String sCivilTime) {
		this.sCivilTime = sCivilTime;
	}

	public int getHour() {
		return Hour;
	}

	public void setHour(int hour) {
		Hour = hour;
	}

	public int getTemp_f() {
		return temp_f;
	}

	public void setTemp_f(int temp_f) {
		this.temp_f = temp_f;
	}

	public int getTemp_c() {
		return temp_c;
	}

	public void setTemp_c(int temp_c) {
		this.temp_c = temp_c;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public boolean isDegreeCelsius() {
		return isDegreeCelsius;
	}

	public void setDegreeCelsius(boolean isDegreeCelsius) {
		this.isDegreeCelsius = isDegreeCelsius;
	}
}
