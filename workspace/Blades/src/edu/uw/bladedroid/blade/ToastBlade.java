package edu.uw.bladedroid.blade;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import edu.uw.bladedroid.AbstractBlade;

public class ToastBlade extends AbstractBlade {
	
	@Override
	public void onResume(Activity activity) {
		Toast.makeText(activity.getApplicationContext(), "Hello World!",
				   Toast.LENGTH_LONG).show();
	}
	
}
