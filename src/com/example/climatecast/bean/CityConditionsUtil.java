package com.example.climatecast.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class CityConditionsUtil {
	Context mContext;

	public CityConditions parseCityCondition(Context context, String cityCode,
			String cityName) throws JSONException, IOException {
		mContext = context;
		HttpURLConnection con;
		CityConditions ccond = new CityConditions();
		ArrayList<TimeCast> altimecast = new ArrayList<TimeCast>();
		ArrayList<DayCast> alDayCast = new ArrayList<DayCast>();

//		Log.d("demo", "Context is" + mContext);
		altimecast = parseTimeCast(cityCode);
		alDayCast = parseDayCast(cityCode);

		URL urlCond = new URL("http://api.wunderground.com/api/" + getKey(1)
				+ "/conditions" + cityCode + ".json");
//		Log.d("demo", "URL for city" + urlCond);

		con = (HttpURLConnection) urlCond.openConnection();

		con.setRequestMethod("GET");
		con.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}

		JSONObject jsonObj = new JSONObject(sb.toString());
		JSONObject cityJSONObject = jsonObj
				.getJSONObject("current_observation");

		ccond.setCityCode(cityCode);

		if (!((cityJSONObject.getString("icon_url")).trim().equals(""))) {
			ccond.setCityImgUrl(cityJSONObject.getString("icon_url"));
		}

		ccond.setCityName(cityName);

		if (!((cityJSONObject.getString("temp_c")).trim().equals(""))) {
			ccond.setCurrentTempC(Double.parseDouble(cityJSONObject
					.getString("temp_c")));
		}

		if (!((cityJSONObject.getString("temp_f")).trim().equals(""))) {
			ccond.setCurrentTempF(Double.parseDouble(cityJSONObject
					.getString("temp_f")));
		}

		if (!((cityJSONObject.getString("relative_humidity")).trim().equals(""))) {
			String prec = cityJSONObject.getString("relative_humidity");
			ccond.setPrecip(prec.substring(0, prec.indexOf("%")));
		}

		if (!((cityJSONObject.getString("visibility_mi")).trim().equals(""))) {
			ccond.setVisibility(cityJSONObject.getString("visibility_mi"));
		}

		if (!((cityJSONObject.getString("wind_mph")).trim().equals(""))) {
			ccond.setWind(cityJSONObject.getString("wind_mph"));
		}

		if (!((cityJSONObject.getString("dewpoint_f")).trim().equals(""))) {
			ccond.setDewpoint(cityJSONObject.getString("dewpoint_f"));
		}

		if (!((cityJSONObject.getString("observation_time")).trim().equals(""))) {
			String cTime = cityJSONObject.getString("observation_time");
			ccond.setCityTime(cTime.substring(cTime.indexOf(",")+2,cTime.length()));
		}

		if (con != null)
			con.disconnect();

		ccond.setAlTimeCast(altimecast);
		ccond.setAlDayCast(alDayCast);
		return ccond;

	}

	private ArrayList<TimeCast> parseTimeCast(String cityCode)
			throws MalformedURLException {

		HttpURLConnection con;
		Log.d("demo", "Context is" + mContext);
		ArrayList<TimeCast> alTimeCast = new ArrayList<TimeCast>();

		URL url;
		try {
			url = new URL("http://api.wunderground.com/api/" + getKey(2)
					+ "/hourly" + cityCode + ".json");

			Log.d("demo", "URL for hourCast" + url);
			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}

			JSONObject jsonObj = new JSONObject(sb.toString());
			JSONArray jsonhourly = jsonObj.getJSONArray("hourly_forecast");
			for (int i = 0; i < jsonhourly.length(); i++) {
				JSONObject TimeCastJSONObject = jsonhourly.getJSONObject(i);
				TimeCast tc = new TimeCast();

				if (!((TimeCastJSONObject.getJSONObject("FCTTIME")
						.getString("civil")).trim().equals(""))) {
					tc.setsCivilTime(TimeCastJSONObject
							.getJSONObject("FCTTIME").getString("civil"));
				}

				if (!((TimeCastJSONObject.getJSONObject("FCTTIME")
						.getString("hour")).trim().equals(""))) {
					tc.setHour(Integer.parseInt(TimeCastJSONObject
							.getJSONObject("FCTTIME").getString("hour")));
				}

				if (!((TimeCastJSONObject.getString("icon_url")).trim()
						.equals(""))) {
					tc.setIcon_url(TimeCastJSONObject.getString("icon_url"));
				}

				if (!((TimeCastJSONObject.getJSONObject("temp")
						.getString("metric")).trim().equals(""))) {
					tc.setTemp_c(Integer.parseInt(TimeCastJSONObject
							.getJSONObject("temp").getString("metric")));
				}

				if (!((TimeCastJSONObject.getJSONObject("temp")
						.getString("english")).trim().equals(""))) {
					tc.setTemp_f(Integer.parseInt(TimeCastJSONObject
							.getJSONObject("temp").getString("english")));
				}

				if (!((TimeCastJSONObject.getString("humidity")).trim()
						.equals(""))) {
					tc.setHumidity(Integer.parseInt(TimeCastJSONObject
							.getString("humidity")));
				}

				alTimeCast.add(tc);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return alTimeCast;

	}

	private ArrayList<DayCast> parseDayCast(String cityCode)
			throws MalformedURLException {

		HttpURLConnection con;
		Log.d("demo", "Context is" + mContext);
		ArrayList<DayCast> alDayCast = new ArrayList<DayCast>();

		URL url;
		try {
			url = new URL("http://api.wunderground.com/api/" + getKey(3)
					+ "/forecast10day" + cityCode + ".json");

			Log.d("demo", "URL for DayCast" + url);
			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}

			JSONObject jsonObj = new JSONObject(sb.toString());
			JSONObject jsonForecastObj = jsonObj.getJSONObject("forecast")
					.getJSONObject("simpleforecast");

			JSONArray jsonDay = jsonForecastObj.getJSONArray("forecastday");
			for (int i = 0; i < jsonDay.length(); i++) {
				JSONObject DayCastJSONObject = jsonDay.getJSONObject(i);
				DayCast dc = new DayCast();

				// Time time= new Time();
				int monthDay = Integer.parseInt(DayCastJSONObject
						.getJSONObject("date").getString("day"));
				int month = Integer.parseInt(DayCastJSONObject.getJSONObject(
						"date").getString("month")) - 1;// to make the value
														// between 0-11
				int year = Integer.parseInt(DayCastJSONObject.getJSONObject(
						"date").getString("year"));

				dc.setMonthDay(monthDay);
				dc.setMonth(month);
				dc.setYear(year);

				String display = DayCastJSONObject.getJSONObject("date")
						.getString("weekday_short")
						+ "  "
						+ DayCastJSONObject.getJSONObject("date").getString(
								"day");
				dc.setDayDisplay(display);

				if (!(DayCastJSONObject.getJSONObject("high")
						.getString("celsius").trim().equals(""))) {
					dc.setTemp_high_c(Integer.parseInt(DayCastJSONObject
							.getJSONObject("high").getString("celsius")));
				}

				if (!(DayCastJSONObject.getJSONObject("high")
						.getString("fahrenheit").trim().equals(""))) {
					dc.setTemp_high_f(Integer.parseInt(DayCastJSONObject
							.getJSONObject("high").getString("fahrenheit")));
				}

				if (!(DayCastJSONObject.getJSONObject("low")
						.getString("celsius").trim().equals(""))) {
					dc.setTemp_low_c(Integer.parseInt(DayCastJSONObject
							.getJSONObject("low").getString("celsius")));
				}

				if (!(DayCastJSONObject.getJSONObject("low")
						.getString("fahrenheit").trim().equals(""))) {
					dc.setTemp_low_f(Integer.parseInt(DayCastJSONObject
							.getJSONObject("low").getString("fahrenheit")));
				}

				if (!(DayCastJSONObject.getString("conditions").trim()
						.equals(""))) {
					dc.setCloudStatus(DayCastJSONObject.getString("conditions"));
				}

				if (!(DayCastJSONObject.getString("icon_url").trim().equals(""))) {
					Log.d("demo","DayCastimg URL "+DayCastJSONObject.getString("icon_url"));
					dc.setIcon_url(DayCastJSONObject.getString("icon_url"));
				}
				alDayCast.add(dc);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return alDayCast;

	}

	private String getKey(int i) {
		StringBuilder key = new StringBuilder("");
		InputStream in = null;
		AssetManager assetManager = mContext.getResources().getAssets();

		try {
			in = assetManager.open("app.properties");

			Properties props = new Properties();
			props.load(in);
			if (i == 1)
				key.append(props.getProperty("key1").toString());
			if (i == 2)
				key.append(props.getProperty("key2").toString());
			if (i == 3)
				key.append(props.getProperty("key3").toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return key.toString();
	}

}
