package edu.uw.bladedroid.blade;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AdsBlocker extends AbstractBlade {
	
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.wtf("ADSBLOCK", "I'm going to remove the ads!");
	}
}
