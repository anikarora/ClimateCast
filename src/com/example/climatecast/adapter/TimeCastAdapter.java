package com.example.climatecast.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climatecast.R;
import com.example.climatecast.bean.TimeCast;
import com.squareup.picasso.Picasso;

public class TimeCastAdapter extends ArrayAdapter<TimeCast> {
	Context mContext;
	int mResource;
	ArrayList<TimeCast> allTimeCast;
	ImageView ivImage;
	boolean isDegreeCelsius;

	public TimeCastAdapter(Context context, int resource,
			List<TimeCast> objects, boolean bool) {
		super(context, resource, objects);
		mContext = context;
		mResource = resource;
		allTimeCast = (ArrayList<TimeCast>) objects;
		isDegreeCelsius = bool;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(mResource, parent, false);
		}

		TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
		tvTime.setText(allTimeCast.get(position).getsCivilTime());
		TextView tvHourTemp = (TextView) convertView
				.findViewById(R.id.tvHourTemp);
		if (isDegreeCelsius) {
			tvHourTemp.setText(allTimeCast.get(position).getTemp_c() + "°C");
		} else {
			tvHourTemp.setText(allTimeCast.get(position).getTemp_f() + "°F");
		}
		TextView tvHourHumid = (TextView) convertView
				.findViewById(R.id.tvHourHumid);
		tvHourHumid.setText(allTimeCast.get(position).getHumidity() + "");

		ivImage = (ImageView) convertView
				.findViewById(R.id.ivHourCityCondition);
		// Log.d("demo",allTimeCast.get(position).getIcon_url().toString());
		Picasso.with(mContext).load(allTimeCast.get(position).getIcon_url())
				.error(R.drawable.ic_launcher).into(ivImage);
		return convertView;
	}

}
