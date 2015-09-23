package com.example.climatecast.bean;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climatecast.LoginFragment;
import com.example.climatecast.LoginFragment.LoginFragmentListener;
import com.example.climatecast.R;
import com.example.climatecast.SignUpFragment;
import com.example.climatecast.SignUpFragment.SignUpFragmentListener;
import com.example.climatecast.activity.HomeActivity;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class ClimateCast extends Activity implements SignUpFragmentListener,
		LoginFragmentListener {
	ParseQueryAdapter<ParseObject> adapter;
	ArrayList<City> alSeachCities = new ArrayList<City>();

	final static String loginf = "LoginFragment";
	final static String signupf = "SignUpFragment";
	final static String homef = "HomeFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_climate_cast);

		if (ParseUser.getCurrentUser() != null) {
			Intent cityCond = new Intent(ClimateCast.this, HomeActivity.class);
			startActivity(cityCond);
			finish();
		} else {
			getFragmentManager().beginTransaction()
					.replace(R.id.container, new LoginFragment(), loginf)
					.commit();
		}

	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(ClimateCast.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			Toast.makeText(ClimateCast.this, "No Internet Connection",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	public void OnSignUpSuccessfulgoToHomeFragmentfromSignup() {
		Intent cityCond = new Intent(ClimateCast.this, HomeActivity.class);
		startActivity(cityCond);
	}

	@Override
	public void goToSignUpFragment() {
		getFragmentManager().beginTransaction()
				.replace(R.id.container, new SignUpFragment(), signupf)
				.addToBackStack(null).commit();
	}

	@Override
	public void goToToDoFragmentFromLogin() {
		Intent cityCond = new Intent(ClimateCast.this, HomeActivity.class);
		startActivity(cityCond);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.about) {

			final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					ClimateCast.this, AlertDialog.THEME_HOLO_DARK);
			builderSingle.setTitle("About ClimateCast");
			
			final LinearLayout upperLL = new LinearLayout(this);
			upperLL.setOrientation(LinearLayout.VERTICAL);
			 upperLL.setBackgroundColor(Color.BLACK);
			
			 final ScrollView sv = new ScrollView(this);
			 upperLL.addView(sv);
			
			final LinearLayout LL = new LinearLayout(this);
			LL.setOrientation(LinearLayout.VERTICAL);
			 LL.setBackgroundColor(Color.BLACK);
			 

			TextView tvAbout = new TextView(ClimateCast.this);
			tvAbout.setTextColor(Color.WHITE);
			tvAbout.setTextSize(18);
			tvAbout.setText("This app gives hourly/daily weather forecast." + "\n\n"
					+ "Features : " + "\n"
					+ "-User can create a new account or login with the existing credentials." + "\n\n"
					+ "-User can add/remove various locations from the favorites and set alerts for them." + "\n\n"
					+ "-A new alert can be set to notify the user for an increase/decrease of temperature in a "
					+ "range(which can be set by user) and occurence of rainfall before a time interval(as required by user)." + "\n\n"
					+ "-The alerts can be switched on/off using switch and can be managed/deleted using delete image." + "\n\n"
					+ "-User can log out from the application using the logout icon");

			LL.addView(tvAbout);
			sv.addView(LL);
			builderSingle.setView(upperLL);
			builderSingle.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builderSingle.show();

			return true;

		}
		return super.onOptionsItemSelected(item);
	}
}
