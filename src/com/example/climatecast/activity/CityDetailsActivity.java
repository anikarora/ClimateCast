package com.example.climatecast.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.climatecast.Alert;
import com.example.climatecast.HorizontialListView;
import com.example.climatecast.R;
import com.example.climatecast.adapter.AlertDialogAdapter;
import com.example.climatecast.adapter.DayCastAdapter;
import com.example.climatecast.adapter.TimeCastAdapter;
import com.example.climatecast.bean.CityConditions;
import com.example.climatecast.bean.DayCast;
import com.example.climatecast.bean.TimeCast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class CityDetailsActivity extends Activity {

	ImageView ivCityCondition;
	TextView tvtempDisplay, tvWind, tvVisibility, tvPrec, tvDew, tvTempText;
	CityConditions ccond;
	ListView llViewHour, llViewDay;
	ArrayAdapter<TimeCast> arrayAdapterTimeCast;
	ArrayAdapter<DayCast> arrayAdapterDayCast;
	ArrayAdapter<Alert> arrayAdapter;
	ListView lv;

	TextView tvTemp;
	TextView tvDuration;
	SeekBar sbTemp1;
	SeekBar sbTemp2;
	SeekBar sbDuration;
	ToggleButton tbRain;
	ToggleButton tbCloudy;
	ToggleButton tbSnow;
	ToggleButton tbTornado;
	String rainState = "";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.city_details_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.ManageReminders) {
			final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					CityDetailsActivity.this, AlertDialog.THEME_HOLO_DARK);
			builderSingle.setTitle("Manage Alerts");
			final LinearLayout LL = new LinearLayout(this);
			LL.setOrientation(LinearLayout.VERTICAL);
			final ArrayList<Alert> aList = new ArrayList<Alert>();

			ParseQuery<ParseObject> alertQuery = ParseQuery.getQuery("Alert");
			alertQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
					.getUsername());
			alertQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {

					if (arg1 == null) {
						for (ParseObject obj : arg0) {
							Alert a = new Alert();
							a.setLocationName(obj.getString("Location"));
							a.setRainState(obj.getString("RainState"));
							a.setTargetHigh(obj.getString("TargetHigh"));
							a.setTargetLow(obj.getString("TargetLow"));
							aList.add(a);
						}

						LayoutParams LLParams = new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);

						LL.setLayoutParams(LLParams);

						lv = new ListView(CityDetailsActivity.this);

						arrayAdapter = new AlertDialogAdapter(
								CityDetailsActivity.this,
								R.layout.alert_list_view, aList);
						lv.setAdapter(arrayAdapter);
						LL.addView(lv);
						builderSingle.setView(LL);
						builderSingle.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builderSingle.show();

					} else {
						arg1.printStackTrace();
					}
				}
			});

		} else if (id == R.id.Reminders) {

			final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					CityDetailsActivity.this, AlertDialog.THEME_HOLO_DARK);
			builderSingle.setTitle("Add Alert");
			LayoutInflater inflater = (LayoutInflater) getSystemService(CityDetailsActivity.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.activity_reminder,
					(ViewGroup) findViewById(R.id.LinearLayout1), false);

			builderSingle.setView(layout);

			tvTemp = (TextView) layout.findViewById(R.id.tvTemp);
			tvDuration = (TextView) layout.findViewById(R.id.tvDuration);
			sbTemp1 = (SeekBar) layout.findViewById(R.id.seekBarTemp1);
			sbTemp2 = (SeekBar) layout.findViewById(R.id.seekBarTemp2);
			sbDuration = (SeekBar) layout.findViewById(R.id.seekBarDuration);
			tbRain = (ToggleButton) layout.findViewById(R.id.tbRain);
			sbDuration.setMax(23);

			OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					setTempTextAndWindText();
				}
			};
			sbTemp1.setOnSeekBarChangeListener(listener);
			sbTemp2.setOnSeekBarChangeListener(listener);
			sbDuration.setOnSeekBarChangeListener(listener);

			builderSingle.setPositiveButton("Add",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							final ProgressDialog pd;
							pd = new ProgressDialog(CityDetailsActivity.this,
									AlertDialog.THEME_HOLO_DARK);
							pd.setTitle("Setting Alert");
							pd.setMessage("Alerting System..");
							pd.setCancelable(false);
							pd.show();

							if (tbRain.isChecked()) {
								rainState = "YES";
							} else {
								rainState = "NO";
							}

							ParseObject alertObj = new ParseObject("Alert");
							alertObj.put("UserName", ParseUser.getCurrentUser()
									.getUsername());
							alertObj.put("Location", ccond.getCityName());
							alertObj.put("CityCode", ccond.getCityCode());
							alertObj.put("TargetLow", (sbTemp1.getProgress()-30)
									+ "");
							alertObj.put("TargetHigh", (sbTemp2.getProgress()-30)
									+ "");
							alertObj.put("Interval", sbDuration.getProgress()
									+ "");
							alertObj.put("RainState", rainState);

							alertObj.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException arg0) {
									if (arg0 == null) {
										Toast.makeText(
												CityDetailsActivity.this,
												"Alert Set", Toast.LENGTH_LONG)
												.show();
										pd.dismiss();
										// finish();
									} else {
										Toast.makeText(
												CityDetailsActivity.this,
												arg0.getLocalizedMessage(),
												Toast.LENGTH_LONG).show();
									}

								}
							});

						}
					});
			builderSingle.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builderSingle.show();
		}
		return super.onOptionsItemSelected(item);
	}

	public void setTempTextAndWindText() {
		int minTemp = -30;

		tvTemp.setText("Temperature (" + (sbTemp1.getProgress() + minTemp)
				+ "°C" + "  :   " + (sbTemp2.getProgress() + minTemp) + "°C "
				+ ")");
		tvDuration.setText("Alert me before : " + sbDuration.getProgress()
				+ " hours");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_details);
		ivCityCondition = (ImageView) findViewById(R.id.ivCityCond);
		tvtempDisplay = (TextView) findViewById(R.id.tvtempDisplay);
		tvWind = (TextView) findViewById(R.id.tvWind);
		tvVisibility = (TextView) findViewById(R.id.tvVisibility);
		tvPrec = (TextView) findViewById(R.id.tvPrec);
		tvDew = (TextView) findViewById(R.id.tvDew);
		llViewHour = (ListView) findViewById(R.id.lvTimeCast);
		tvTempText = (TextView) findViewById(R.id.tvTempText);

		ccond = (CityConditions) getIntent().getExtras().getSerializable(
				"CITYCONDITIONS");

		tvTempText.setText(ccond.getCityName());

		if (ccond.isDegreeCelsius) {
			tvtempDisplay.setText(ccond.getCurrentTempC() + " °C");
		} else {
			tvtempDisplay.setText(ccond.getCurrentTempF() + " °F");
		}
		tvWind.setText(ccond.getWind() + " mph");
		tvVisibility.setText(ccond.getVisibility() + " miles");
		tvPrec.setText(ccond.getPrecip() + "%");
		tvDew.setText(ccond.getDewpoint() + "");

		Picasso.with(this).load(ccond.getCityImgUrl())
				.error(getResources().getDrawable(R.drawable.ic_launcher))
				.into(ivCityCondition);

		arrayAdapterTimeCast = new TimeCastAdapter(CityDetailsActivity.this,
				R.layout.hour_list_row, ccond.getAlTimeCast(),
				ccond.isDegreeCelsius);
		llViewHour.setAdapter(arrayAdapterTimeCast);

		arrayAdapterDayCast = new DayCastAdapter(CityDetailsActivity.this,
				R.layout.day_list_row, ccond.getAlDayCast(),
				ccond.isDegreeCelsius);

		HorizontialListView listview = (HorizontialListView) findViewById(R.id.lvDayCast);
		listview.setAdapter(arrayAdapterDayCast);

	}

	public void refreshReminders(List<Alert> alertList) {
		arrayAdapter = new AlertDialogAdapter(CityDetailsActivity.this,
				R.layout.alert_list_view, alertList);
		lv.setAdapter(arrayAdapter);
		Toast.makeText(CityDetailsActivity.this, "Reminder removed",
				Toast.LENGTH_SHORT).show();
	}

}
