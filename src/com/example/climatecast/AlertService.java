package com.example.climatecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.climatecast.activity.HomeActivity;
import com.example.climatecast.bean.TimeCast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AlertService extends Service {

	private final IBinder mBinder = new LocalBinder();

	private final Random mGenerator = new Random();

	public static final long NOTIFY_INTERVAL = 20 * 1000; // 20 seconds

	private Handler mHandler = new Handler();

	private Timer mTimer = null;
	ArrayList<TimeCast> aList;
	String tempLow = "";
	String tempHigh = "";
	String interval = "";
	String locationName = "";
	String rainWarning = "";

	@Override
	public void onCreate() {

		// Log.d("SERVICE", "THE SERVICE WAS STARTED SUCCESSFULLY");

		if (mTimer != null) {
			mTimer.cancel();
		} else {
			mTimer = new Timer();
		}
		mTimer.scheduleAtFixedRate(new CheckAlertTask(), 0, NOTIFY_INTERVAL);

		// sendAlert();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	class CheckAlertTask extends TimerTask {

		@Override
		public void run() {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// display toast
					// sendAlert();
					if (ParseUser.getCurrentUser() != null) {
						checkAlerts();
					}
				}

			});
		}
	}

	public void checkAlerts() {

		ParseQuery<ParseObject> alertQuery = ParseQuery.getQuery("Alert");
		alertQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
				.getUsername());
		alertQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {

					for (ParseObject obj : objects) {

						tempLow = obj.getString("TargetLow");
						tempHigh = obj.getString("TargetHigh");
						interval = obj.getString("Interval");
						locationName = obj.getString("Location");
						rainWarning = obj.getString("RainState");

						new JSONTempAsyncTask().execute(
								obj.getString("CityCode"), locationName,
								tempLow, tempHigh, interval, rainWarning);
					}

				} else {
					Log.d("ServiceError", e.getLocalizedMessage());
				}
			}
		});

	}

	@Override
	public void onDestroy() {
		if (mTimer != null) {
			mTimer.cancel();
		}
		super.onDestroy();
	}

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		AlertService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return AlertService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/** method for clients */
	public int getRandomNumber() {
		return mGenerator.nextInt(10000000);
	}

	/**
	 * Go to parse.com and query the users alerts, if one is active, send
	 * notification.
	 */

	public void sendAlert(String location, int temp, String pretty) {
		Log.d("demo", "location is" + location);
		Notification.Builder mBuilder = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("ClimateCast Alert!! ")
				.setStyle(
						new Notification.BigTextStyle().bigText(location
								+ " will touch " + temp + "°C at " + pretty));

		Intent resultIntent = new Intent(this, HomeActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		stackBuilder.addParentStack(HomeActivity.class);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(getRandomNumber(), mBuilder.build());

	}

	public void sendAlertForRain(String location, String pretty) {
		Notification.Builder mBuilder = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("ClimateCast Alert!! ")
				.setStyle(
						new Notification.BigTextStyle().bigText(location
								+ " will have rainfall " + " around " + pretty));

		Intent resultIntent = new Intent(this, HomeActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		stackBuilder.addParentStack(HomeActivity.class);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(getRandomNumber(), mBuilder.build());

	}

	private ArrayList<TimeCast> parseTimeCast(String cityCode)
			throws MalformedURLException {

		HttpURLConnection con;
		ArrayList<TimeCast> alTimeCast = new ArrayList<TimeCast>();

		URL url;
		try {
			url = new URL("http://api.wunderground.com/api/" + getKey(2)
					+ "/hourly" + cityCode + ".json");

			Log.d("demo", "URL for hourCast " + url);
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
	//		Log.d("abcde",sb.toString());
			JSONObject jsonObj = new JSONObject(sb.toString());
			JSONArray jsonhourly = jsonObj.getJSONArray("hourly_forecast");
			for (int i = 0; i < jsonhourly.length(); i++) {
				JSONObject TimeCastJSONObject = jsonhourly.getJSONObject(i);
				TimeCast tc = new TimeCast();

				if (!(TimeCastJSONObject.getJSONObject("FCTTIME")
						.getString("civil").trim().equals(""))) {
					tc.setsCivilTime(TimeCastJSONObject
							.getJSONObject("FCTTIME").getString("civil"));
				}
				if (!(TimeCastJSONObject.getJSONObject("FCTTIME")
						.getString("hour").trim().equals(""))) {
					tc.setHour(Integer.parseInt(TimeCastJSONObject
							.getJSONObject("FCTTIME").getString("hour")));
				}

				if (!(TimeCastJSONObject.getString("icon_url").trim()
						.equals(""))) {
					tc.setIcon_url(TimeCastJSONObject.getString("icon_url"));
				}

				if (!(TimeCastJSONObject.getJSONObject("temp")
						.getString("metric").trim().equals(""))) {
					tc.setTemp_c(Integer.parseInt(TimeCastJSONObject
							.getJSONObject("temp").getString("metric")));
				}

				if (!(TimeCastJSONObject.getJSONObject("temp")
						.getString("english").trim().equals(""))) {
					tc.setTemp_f(Integer.parseInt(TimeCastJSONObject
							.getJSONObject("temp").getString("english")));
				}

				if (!(TimeCastJSONObject.getString("humidity").trim()
						.equals(""))) {
					tc.setHumidity(Integer.parseInt(TimeCastJSONObject
							.getString("humidity")));
				}
				if (!(TimeCastJSONObject.getString("condition").trim()
						.equals(""))) {
					tc.setRain(TimeCastJSONObject.getString("condition"));
				}
				if (!(TimeCastJSONObject.getJSONObject("FCTTIME")
						.getString("pretty").trim().equals(""))) {
					tc.setPretty(TimeCastJSONObject.getJSONObject("FCTTIME")
							.getString("pretty"));
				}
				alTimeCast.add(tc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return alTimeCast;
	}

	private String getKey(int i) {
		StringBuilder key = new StringBuilder("");
		InputStream in = null;
		AssetManager assetManager = this.getResources().getAssets();

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

	public class JSONTempAsyncTask extends
			AsyncTask<String, Void, ArrayList<TimeCast>> {

		@Override
		protected ArrayList<TimeCast> doInBackground(String... params) {

			ArrayList<TimeCast> aList = new ArrayList<TimeCast>();
			try {
				aList = parseTimeCast(params[0]);

				TimeCast lastObj = new TimeCast();

				lastObj.setCityName(params[1]);
				lastObj.setTempLow(params[2]);
				lastObj.setTempHigh(params[3]);
				lastObj.setInterval(params[4]);
				lastObj.setRain(params[5]);

				aList.add(lastObj);

				return aList;
			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<TimeCast> result) {
			super.onPostExecute(result);

			if (result != null) {
				int size = result.size();

				int tempLow = Integer.parseInt(result.get(size - 1)
						.getTempLow());
				int tempHigh = Integer.parseInt(result.get(size - 1)
						.getTempHigh());
				int interval = Integer.parseInt(result.get(size - 1)
						.getInterval());
				String rainWarning = result.get(size - 1).getRain();

				for (int i = 0; i <= interval; i++) {

					if (size > interval
							&& (tempLow <= result.get(i).getTemp_c())
							&& (tempHigh >= result.get(i).getTemp_c())) {

						sendAlert(result.get(result.size() - 1).getCityName(),
								result.get(i).getTemp_c(), result.get(i)
										.getPretty());
						break;
					}
				}
				for (int i = 0; i <= interval; i++) {
					if (size > interval
							&& (rainWarning != null)
							&& (rainWarning.equals("YES"))
							&& (result.get(i).getRain().contains("Rain") || result
									.get(i).getRain().contains("Thunder"))) {

						sendAlertForRain(result.get(result.size() - 1)
								.getCityName(), result.get(i).getPretty());
					}
					break;
				}
			}
		}
	}
}
