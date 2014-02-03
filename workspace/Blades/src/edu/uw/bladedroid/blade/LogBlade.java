package edu.uw.bladedroid.blade;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import edu.uw.bladedroid.AbstractBlade;

public class LogBlade extends AbstractBlade {
	
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.wtf("BLADE", "THIS IS THE TEST BLADE on activity " + activity.getClass().getName());
	}
	
}
