package com.example.climatecast.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climatecast.R;
import com.example.climatecast.activity.HomeActivity;
import com.example.climatecast.bean.CityConditions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class CityCondAdapter extends ArrayAdapter<CityConditions> {
	Context mContext;
	int mResource;
	ArrayList<CityConditions> allCityCond;

	public CityCondAdapter(Context context, int resource,
			List<CityConditions> objects) {

		super(context, resource, objects);
		mContext = context;
		mResource = resource;
		allCityCond = (ArrayList<CityConditions>) objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(mResource, parent, false);
			holder = new ViewHolder();
			holder.tvCityName = (TextView) convertView
					.findViewById(R.id.tvCityName);
			holder.tvCityTime = (TextView) convertView
					.findViewById(R.id.tvCityTime);
			holder.tvCityTemp = (TextView) convertView
					.findViewById(R.id.tvCityTemp);
			holder.ivDelete = (ImageView) convertView
					.findViewById(R.id.ivDelete);
			holder.ivImage = (ImageView) convertView
					.findViewById(R.id.ivCityImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvCityName.setText(allCityCond.get(position).getCityName());
		holder.tvCityTime.setText(allCityCond.get(position).getCityTime());
		holder.tvCityTemp.setText(allCityCond.get(position).getCurrentTempF()
				+ " °F");
		allCityCond.get(position).setDegreeCelsius(false);

		// Log.d("demo",allCityCond.get(position).getCityImgUrl().toString());
		Picasso.with(mContext).load(allCityCond.get(position).getCityImgUrl())
				.error(R.drawable.ic_launcher).into(holder.ivImage);

		holder.ivDelete.setTag(new Integer(position));

		holder.ivDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {

				AlertDialog.Builder adDel1 = new AlertDialog.Builder(mContext,AlertDialog.THEME_HOLO_DARK);

				adDel1.setTitle("Delete Location");
				adDel1.setMessage(
						"Please confirm to delete this location from Favorites")
						.setCancelable(false)
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								})
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										delete((Integer) v.getTag());
									}

								});
				AlertDialog alertDialog = adDel1.create();
				alertDialog.show();
			}
		});

		holder.tvCityTemp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.tvCityTemp.getText().equals(
						allCityCond.get(position).getCurrentTempF() + " °F")) {
					holder.tvCityTemp.setText(allCityCond.get(position)
							.getCurrentTempC() + " °C");
					allCityCond.get(position).setDegreeCelsius(true);
				} else {
					holder.tvCityTemp.setText(allCityCond.get(position)
							.getCurrentTempF() + " °F");
					allCityCond.get(position).setDegreeCelsius(false);
				}

			}
		});

		return convertView;
	}

	public void delete(int pos) {
		ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery("userCities");
		deleteQuery.whereEqualTo("UserEmail", ParseUser.getCurrentUser()
				.getUsername());
		final int position = pos;

		deleteQuery.whereEqualTo("CityName", allCityCond.get(position)
				.getCityName());
		deleteQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {

					for (ParseObject obj : objects) {

						obj.deleteInBackground();
					}
					allCityCond.remove(position);
					((HomeActivity) mContext).refreshListView(allCityCond);
				} else {
					e.printStackTrace();
					Log.d("DeleteCityError", e.getLocalizedMessage());
				}
			}
		});
	}

	static class ViewHolder {

		private TextView tvCityName, tvCityTime, tvCityTemp;
		private ImageView ivImage, ivDelete;
		// String which ="";

	}

}
