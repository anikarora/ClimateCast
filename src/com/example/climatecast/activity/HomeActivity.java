package com.example.climatecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climatecast.AlertService;
import com.example.climatecast.R;
import com.example.climatecast.adapter.CityCondAdapter;
import com.example.climatecast.bean.CityConditions;
import com.example.climatecast.bean.CityConditionsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HomeActivity extends Activity {
	final static int REQ_CODE = 100;
	ArrayList<CityConditions> alCityCondList = new ArrayList<CityConditions>();
	ArrayAdapter<CityConditions> arrayAdapter;

	Switch reminderSwitch;

	CityConditions ccond;
	String reminderState;

	ListView llView;
	final static String addlocf = "AddLocationFragment";
	final static String loginf = "LoginFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		reminderSwitch = (Switch) findViewById(R.id.switch1);
		llView = (ListView) findViewById(R.id.lvCities);

		llView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ccond = alCityCondList.get(position);

				Intent preview = new Intent(HomeActivity.this,
						CityDetailsActivity.class);
				preview.putExtra("CITYCONDITIONS", ccond);
				startActivity(preview);
			}
		});

		final Intent intentService = new Intent(HomeActivity.this,
				AlertService.class);

		ParseQuery<ParseObject> reminderQuery = ParseQuery
				.getQuery("ReminderState");
		reminderQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
				.getUsername());
		reminderQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {

					for (ParseObject obj : objects) {

						if (obj.getString("State").equals("On")) {
							reminderSwitch.setChecked(true);
							startService(intentService);

						} else {
							reminderSwitch.setChecked(false);
							// stopService(new Intent(HomeActivity.this,
							// AlertService.class));
						}

					}

				} else {
					Log.d("DeleteError", e.getLocalizedMessage());
				}
			}
		});

		doBringCityDetails();

		reminderSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							deleteState("On");

							Log.d("anik", "yes clicked");
							startService(intentService);
						} else {
							deleteState("Off");

							stopService(intentService);

							Log.d("anik", "not clicked");
						}

					}
				});
	}

	private void deleteState(final String state) {
		ParseQuery<ParseObject> reminderQuery = ParseQuery
				.getQuery("ReminderState");
		reminderQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
				.getUsername());
		reminderQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {

					for (ParseObject obj : objects) {

						try {
							obj.delete();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

					}
					saveState(state);

				} else {
					Log.d("DeleteError", e.getLocalizedMessage());
				}
			}
		});

	}

	private void saveState(final String state) {
		ParseObject reminderObj = new ParseObject("ReminderState");
		reminderObj.put("UserName", ParseUser.getCurrentUser().getUsername());
		reminderObj.put("State", state);
		reminderObj.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {
				if (arg0 == null) {
					Toast.makeText(HomeActivity.this,
							"Alert Notification is " + state, Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(HomeActivity.this,
							arg0.getLocalizedMessage(), Toast.LENGTH_LONG)
							.show();
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.climate_cast, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_Add_locations) {

			Intent i = new Intent(HomeActivity.this, AddLocationActivity.class);
			startActivityForResult(i, REQ_CODE);

			return true;

		} else if (id == R.id.action_signOut) {
			ParseUser.logOut();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE) {
			doBringCityDetails();
		}
	}

	public void doBringCityDetails() {
		alCityCondList.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("userCities");
		query.whereEqualTo("UserEmail", ParseUser.getCurrentUser()
				.getUsername());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> inList, ParseException e) {
				if (e == null) {
					for (int i = 0; i < inList.size(); i++) {
						CityConditions cd = new CityConditions();
						cd.setCityCode(inList.get(i).getString("CityCode"));
						cd.setCityName(inList.get(i).getString("CityName"));

						alCityCondList.add(cd);
						Log.d("demo", "***" + i);
					}

					new DoBringDetails().execute();

				} else {
					e.printStackTrace();
					Toast.makeText(HomeActivity.this,
							"Error in fetching City list from Parse",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public class DoBringDetails extends
			AsyncTask<Void, Void, ArrayList<CityConditions>> {
		ProgressDialog pd;

		@Override
		protected ArrayList<CityConditions> doInBackground(Void... params) {
			ArrayList<CityConditions> alccondUpdated = new ArrayList<CityConditions>();
			try {
				Log.d("demo1", "" + alCityCondList.toString());
				// Log.d("demo1",alCityCondList.get(1).getCityName());
				// Log.d("demo1",alCityCondList.get(2).getCityName());
				// Log.d("demo1",alCityCondList.get(3).getCityName());
				for (int j = 0; j < alCityCondList.size(); j++) {
					CityConditions cityConditions = alCityCondList.get(j);
					CityConditionsUtil cdUtil = new CityConditionsUtil();
					CityConditions cdUpdated = cdUtil.parseCityCondition(
							HomeActivity.this, cityConditions.getCityCode(),
							cityConditions.getCityName());
					if (cdUpdated != null) {
						alccondUpdated.add(cdUpdated);
					} else {
						// Log.d("demo", "Unable to add ciry to array list");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// Log.d("demo","size of alccondUpdated "+alccondUpdated.size()+""+alccondUpdated.toString());
			return alccondUpdated;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(HomeActivity.this,
					AlertDialog.THEME_HOLO_DARK);
			pd.setTitle("Loading Data");
			pd.setMessage("Loading..");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected void onPostExecute(ArrayList<CityConditions> result) {
			super.onPostExecute(result);
			alCityCondList.clear();
			alCityCondList = result;

			arrayAdapter = new CityCondAdapter(HomeActivity.this,
					R.layout.cities_list_row, alCityCondList);

			llView.setAdapter(arrayAdapter);
			pd.dismiss();
		}
	}

	public void refreshListView(ArrayList<CityConditions> refList) {

		arrayAdapter = new CityCondAdapter(HomeActivity.this,
				R.layout.cities_list_row, refList);
		llView.setAdapter(arrayAdapter);

		Toast.makeText(HomeActivity.this, "Removed from favorites",
				Toast.LENGTH_SHORT).show();
	}

}
