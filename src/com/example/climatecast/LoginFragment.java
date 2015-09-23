package com.example.climatecast;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link LoginFragment.OnFragmentInteractionListener}
 * interface to handle interaction events.
 *
 */
public class LoginFragment extends Fragment {

	ArrayList<String> devNames = new ArrayList<String>();
	private LoginFragmentListener mListener;

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		devNames.add("Anik Arora");
		devNames.add("Ankith Reddy");
		devNames.add("Vinod Gone");

		View view = inflater.inflate(R.layout.fragment_login, container, false);
		final EditText etUserEmail = (EditText) view
				.findViewById(R.id.editTextEmail);
		final EditText etUserPass = (EditText) view
				.findViewById(R.id.editTextPassword);
		view.findViewById(R.id.buttonLogin).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String UsrEmail = etUserEmail.getText().toString();
						String UsrPass = etUserPass.getText().toString();
						if (UsrEmail.isEmpty() || UsrPass.isEmpty()) {
							Toast.makeText(
									getActivity(),
									"User Name or Password cannot be left empty",
									Toast.LENGTH_SHORT).show();
						} else {
							if (!mListener.isConnected()) {
								return;
							}

							ParseUser.logInInBackground(UsrEmail, UsrPass,
									new LogInCallback() {
										public void done(ParseUser user,
												ParseException e) {
											if (user != null) {
												Toast.makeText(
														getActivity(),
														"Logged In successfully",
														Toast.LENGTH_LONG)
														.show();
												mListener
														.goToToDoFragmentFromLogin();
											} else {
												Toast.makeText(getActivity(),
														"Login not successful",
														Toast.LENGTH_LONG)
														.show();
											}
										}
									});
						}

					}
				});
		view.findViewById(R.id.buttonCreateNewAccount).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mListener.goToSignUpFragment();
					}
				});

		view.findViewById(R.id.ivDev).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ListView lView = new ListView(getActivity());
						lView.setBackgroundColor(Color.BLACK);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								getActivity(), R.layout.search_location,
								R.id.tvLocation, devNames);
						lView.setAdapter(adapter);

						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity(), AlertDialog.THEME_HOLO_DARK);
						builder.setTitle("We are developers...");
						builder.setView(lView);

						final AlertDialog dialog = builder.create();
						dialog.show();
					}
				});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (LoginFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement LoginFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface LoginFragmentListener {
		boolean isConnected();

		void goToSignUpFragment();

		void goToToDoFragmentFromLogin();
	}

}
