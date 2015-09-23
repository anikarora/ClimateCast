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
import com.example.climatecast.bean.DayCast;
import com.squareup.picasso.Picasso;

public class DayCastAdapter extends ArrayAdapter<DayCast> {

	Context mContext;
	int mResource;
	ArrayList<DayCast> allDayCast;
	ImageView ivImage;
	boolean isDegreeCelsius;

	public DayCastAdapter(Context context, int resource, List<DayCast> objects,
			boolean bool) {
		super(context, resource, objects);

		mContext = context;
		mResource = resource;
		allDayCast = (ArrayList<DayCast>) objects;
		isDegreeCelsius = bool;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(mResource, parent, false);
		}

		TextView tvDayWeek = (TextView) convertView
				.findViewById(R.id.tvDayWeek);
		tvDayWeek.setText(allDayCast.get(position).getDayDisplay());
		TextView tvDayTemp = (TextView) convertView
				.findViewById(R.id.tvDayTemp);
		if (isDegreeCelsius)
			tvDayTemp.setText(allDayCast.get(position).getTemp_low_c() + "/"
					+ allDayCast.get(position).getTemp_high_c() + "°C");
		else
			tvDayTemp.setText(allDayCast.get(position).getTemp_low_f() + "/"
					+ allDayCast.get(position).getTemp_high_f() + "°F");
		TextView tvDayStatus = (TextView) convertView
				.findViewById(R.id.tvDayStatus);
		tvDayStatus.setText(allDayCast.get(position).getCloudStatus());

		ivImage = (ImageView) convertView.findViewById(R.id.ivDayCityCond);
		// Log.d("democd","Day image url "+allDayCast.get(position).getIcon_url().toString());
		Picasso.with(mContext).load(allDayCast.get(position).getIcon_url())
				.error(R.drawable.ic_launcher).into(ivImage);

		return convertView;
	}

}
