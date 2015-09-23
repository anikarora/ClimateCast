package com.example.climatecast.bean;

public class City {

	String cityName;
	String cityCode;

	@Override
	public String toString() {
		return cityName;
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
}
