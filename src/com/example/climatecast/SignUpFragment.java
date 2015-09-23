package com.example.climatecast;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link SignUpFragment.OnFragmentInteractionListener}
 * interface to handle interaction events.
 *
 */
public class SignUpFragment extends Fragment {

	private SignUpFragmentListener mListener;

	public SignUpFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sign_up, container,
				false);
		final EditText etUserEmail = (EditText) view
				.findViewById(R.id.editTextEmail);
		final EditText etUserName = (EditText) view
				.findViewById(R.id.editTextUserName);
		final EditText etUserPass = (EditText) view
				.findViewById(R.id.editTextPassword);
		final EditText etUserPassConfirm = (EditText) view
				.findViewById(R.id.editTextPasswordConfirm);

		view.findViewById(R.id.buttonSignup).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (etUserEmail.getText().toString().isEmpty()
								|| etUserName.getText().toString().isEmpty()
								|| etUserPass.getText().toString().isEmpty()
								|| etUserPassConfirm.getText().toString()
										.isEmpty()) {
							Toast.makeText(getActivity(),
									getResources().getString(R.string.NoBlank),
									Toast.LENGTH_LONG).show();
						} else if (!etUserPass.getText().toString()
								.equals(etUserPassConfirm.getText().toString())) {
							Toast.makeText(
									getActivity(),
									getResources().getString(
											R.string.PasswordsDontMatch),
									Toast.LENGTH_LONG).show();
						} else {
							ParseQuery<ParseObject> query = ParseQuery
									.getQuery("User");
							query.whereEqualTo("email", etUserEmail.getText()
									.toString());
							query.getFirstInBackground(new GetCallback<ParseObject>() {
								@Override
								public void done(ParseObject arg0,
										ParseException arg1) {
									if (arg0 == null) {
										ParseUser user = new ParseUser();
										user.setUsername(etUserEmail.getText()
												.toString());
										user.setPassword(etUserPass.getText()
												.toString());
										user.setEmail(etUserEmail.getText()
												.toString());
										user.put(
												getResources().getString(
														R.string.UserFullName),
												etUserName.getText().toString());

										user.signUpInBackground(new SignUpCallback() {
											@Override
											public void done(
													com.parse.ParseException e) {
												if (e == null) {
													Toast.makeText(
															getActivity(),
															getResources()
																	.getString(
																			R.string.SuccesLogIn),
															Toast.LENGTH_SHORT)
															.show();
													mListener
															.OnSignUpSuccessfulgoToHomeFragmentfromSignup();

												} else {
													e.printStackTrace();
													Toast.makeText(
															getActivity(),
															getResources()
																	.getString(
																			R.string.IDTaken),
															Toast.LENGTH_LONG)
															.show();
												}
											}
										});

									} else {
										Toast.makeText(
												getActivity(),
												getResources().getString(
														R.string.IDTaken)
														+ "1",
												Toast.LENGTH_LONG).show();
									}

								}
							});

						}

					}
				});
		view.findViewById(R.id.buttonCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						getFragmentManager().popBackStack();
					}
				});
		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (SignUpFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
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
	public interface SignUpFragmentListener {
		public void OnSignUpSuccessfulgoToHomeFragmentfromSignup();

	}

}
