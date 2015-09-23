package com.example.climatecast.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.climatecast.R;
import com.example.climatecast.bean.City;
import com.example.climatecast.bean.CityUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddLocationActivity extends Activity {
	ArrayList<City> alSeachCities = new ArrayList<City>();
	Button doneBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		final EditText etCityName = (EditText) findViewById(R.id.etSearchCity);

		doneBtn = (Button) findViewById(R.id.DoneBtn);

		doneBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult("NewCity", new Intent());
				finish();
			}

			private void setResult(String string, Intent putExtra) {
			}
		});

		findViewById(R.id.SearchBtn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!etCityName.getText().toString().isEmpty())
							try {
								new getCitiesData().execute(MakeURL(etCityName
										.getText().toString()));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						else
							Toast.makeText(AddLocationActivity.this,
									"City Name is Empty", Toast.LENGTH_SHORT)
									.show();

					}
				});
		ListView lv = (ListView) findViewById(R.id.lvsearchCities);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				addCityName(position);
				return false;
			}
		});

	}

	private class getCitiesData extends
			AsyncTask<String, Void, ArrayList<City>> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(AddLocationActivity.this, AlertDialog.THEME_HOLO_DARK);
			pd.setMessage("Fetching Related Cities");
			pd.show();
		}

		@Override
		protected ArrayList<City> doInBackground(String... params) {
			HttpURLConnection con;
			try {

				URL url = new URL(params[0]);

				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				return CityUtil.parseCities(con.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<City> result) {
			alSeachCities = result;

			pd.dismiss();
			ListView lv = (ListView) findViewById(R.id.lvsearchCities);
			ArrayAdapter<City> adapter = new ArrayAdapter<City>(
					AddLocationActivity.this, R.layout.search_location,
					R.id.tvLocation, alSeachCities);
			lv.setAdapter(adapter);

			doneBtn.setVisibility(View.VISIBLE);
		}
	}

	private String MakeURL(String sKeyword) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		// https://api.500px.com/v1/photos/search?rpp=50&image_size=4&consumer_key=2PMlT4rY49YIBR5vamicpSRW3UM6GqU0foRXpfG9&term=uncc
		sb.append("http://autocomplete.wunderground.com/aq?query=");

		sb.append(URLEncoder.encode(sKeyword, "utf-8"));
		return sb.toString();
	}

	public void addCityName(int position) {

		final City city = alSeachCities.get(position);

		ParseObject userCities = new ParseObject("userCities");

		userCities.put("CityName", city.getCityName());
		userCities.put("CityCode", city.getCityCode());
		userCities.put("UserEmail",
				ParseUser.getCurrentUser().getString("email"));
		userCities.saveInBackground(new SaveCallback() {
			@Override
			public void done(com.parse.ParseException arg0) {
				Toast.makeText(AddLocationActivity.this,
						"Added " + city.getCityName(), Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
