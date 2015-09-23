
package com.example.climatecast;

import android.app.Application;

import com.parse.Parse;

public class parseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "s54tYlzow3W7UfxMey9KZVx4P06zp3DqZ3i83vMJ",
				"XA0lapufdEsQ9OK8Yz16QjtPSgeGVRdszNlfZADf");

	}

}
