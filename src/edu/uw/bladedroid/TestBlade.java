package edu.uw.bladedroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestBlade implements IBlade {

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.i("BLADE", "THIS IS THE TEST BLADE on activity " + activity.getClass().getName());
	}

}
