package edu.uw.bladedroid.blade;

import edu.uw.bladedroid.AbstractBlade;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestBlade extends AbstractBlade {

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.i("BLADE", "THIS IS THE TEST BLADE on activity " + activity.getClass().getName());
	}

}
