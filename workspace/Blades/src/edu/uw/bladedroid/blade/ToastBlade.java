package edu.uw.bladedroid.blade;

import android.app.Activity;
import android.widget.Toast;

public class ToastBlade extends AbstractBlade {
	
	@Override
	public void onResume(Activity activity) {
		Toast.makeText(activity.getApplicationContext(), "Hello World!", Toast.LENGTH_LONG).show();
	}
	
}
