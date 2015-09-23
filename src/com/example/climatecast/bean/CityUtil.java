package com.example.climatecast.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityUtil {
	public static ArrayList<City> parseCities(InputStream in)
			throws JSONException, IOException {

		ArrayList<City> CitiesList = new ArrayList<City>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();

		String line = reader.readLine();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}

		JSONObject jsonObj = new JSONObject(sb.toString());
		JSONArray jsonCityArray = jsonObj.getJSONArray("RESULTS");
		for (int i = 0; i < jsonCityArray.length(); i++) {
			JSONObject cityJSONObject = jsonCityArray.getJSONObject(i);
			City city = new City();
			city.setCityName(cityJSONObject.getString("name"));
			city.setCityCode(cityJSONObject.getString("l"));

			CitiesList.add(city);
		}
		return CitiesList;
	}

}
