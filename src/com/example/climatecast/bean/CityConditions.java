package com.example.climatecast.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CityConditions implements Serializable{

	String cityName;
	String cityCode;
	String cityImgUrl;	
	double currentTempF;
	double currentTempC;
	String cityTime;
	String wind;
	String visibility;
	String dewpoint;
	String precip;
	public boolean isDegreeCelsius;
	ArrayList<TimeCast> alTimeCast;
	ArrayList<DayCast> alDayCast;
	public ArrayList<DayCast> getAlDayCast() {
		return alDayCast;
	}
	public void setAlDayCast(ArrayList<DayCast> alDayCast) {
		this.alDayCast = alDayCast;
	}
	public ArrayList<TimeCast> getAlTimeCast() {
		return alTimeCast;
	}
	public void setAlTimeCast(ArrayList<TimeCast> alTimeCast) {
		this.alTimeCast = alTimeCast;
	}
	public String getPrecip() {
		return precip;
	}
	public void setPrecip(String precip) {
		this.precip = precip;
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
	public String getCityImgUrl() {
		return cityImgUrl;
	}
	public void setCityImgUrl(String cityImgUrl) {
		this.cityImgUrl = cityImgUrl;
	}
	public double getCurrentTempF() {
		return currentTempF;
	}
	public void setCurrentTempF(double currentTempF) {
		this.currentTempF = currentTempF;
	}
	public double getCurrentTempC() {
		return currentTempC;
	}
	public void setCurrentTempC(double currentTempC) {
		this.currentTempC = currentTempC;
	}	
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getDewpoint() {
		return dewpoint;
	}
	public void setDewpoint(String dewpoint) {
		this.dewpoint = dewpoint;
	}
	public String getCityTime() {
		return cityTime;
	}
	public void setCityTime(String cityTime) {
		this.cityTime = cityTime;
	}
	public boolean isDegreeCelsius() {
		return isDegreeCelsius;
	}
	public void setDegreeCelsius(boolean isDegreeCelsius) {
		this.isDegreeCelsius = isDegreeCelsius;
	}
}
