package com.example.climatecast.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climatecast.Alert;
import com.example.climatecast.R;
import com.example.climatecast.activity.CityDetailsActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AlertDialogAdapter extends ArrayAdapter<Alert> {

	Context mContext;
	int mResource;
	List<Alert> alertList;

	public AlertDialogAdapter(Context context, int resource, List<Alert> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
		this.alertList = objects;
	}

	@Override
	public int getCount() {
		return alertList.size();
	}

	@Override
	public Alert getItem(int position) {
		return alertList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Alert listItem = getItem(position);

		if (convertView == null) {

			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

			convertView = inflater.inflate(mResource, parent, false);

			holder = new ViewHolder();
			holder.locationTv = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.tempRangeTv = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.rainTv = (TextView) convertView.findViewById(R.id.textView3);

			holder.deleteIv = (ImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.locationTv.setText(listItem.getLocationName());
		holder.tempRangeTv.setText("Temperature Range: "
				+ listItem.getTargetLow() + "°C  -  " + listItem.getTargetHigh()
				+ "°C");
		holder.rainTv.setText("Rain State: " + listItem.getRainState());

		holder.deleteIv.setImageResource(R.drawable.ic_delete);

		holder.deleteIv.setTag(new Integer(position));

		holder.deleteIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> deleteQuery = ParseQuery
						.getQuery("Alert");
				deleteQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
						.getUsername());
				final int position = (Integer) v.getTag();

				deleteQuery.whereEqualTo("Location", alertList.get(position)
						.getLocationName());
				deleteQuery.whereEqualTo("TargetLow", alertList.get(position)
						.getTargetLow());
				deleteQuery.whereEqualTo("TargetHigh", alertList.get(position)
						.getTargetHigh());
				deleteQuery.whereEqualTo("RainState", alertList.get(position)
						.getRainState());
				deleteQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {

						if (e == null) {

							for (ParseObject obj : objects) {

								obj.deleteInBackground();
							}
							alertList.remove(position);
							((CityDetailsActivity) mContext)
									.refreshReminders(alertList);
						} else {
							e.printStackTrace();
							Log.d("DeleteCityError", e.getLocalizedMessage());
						}
					}
				});

			}
		});
		return convertView;
	}

	static class ViewHolder {
		private TextView locationTv, tempRangeTv, rainTv;
		private ImageView deleteIv;
	}

}
