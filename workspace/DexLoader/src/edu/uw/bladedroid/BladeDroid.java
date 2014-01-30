package edu.uw.bladedroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import edu.uw.bladedroid.loader.BladeLoader;

public final class BladeDroid {
	public static final String TAG = "BLADE-DROID";
	
	private static BladeDroid instance;
	private BladeLoader loader;
	
	private BladeDroid(Activity initiator) {
		loader = new BladeLoader(initiator);
	}
	
	public static synchronized BladeDroid getInstance(Activity initiator) {
		if(instance == null) {
			instance = new BladeDroid(initiator);
		}
		return instance;
	}
	
	public static void onCreate(Activity a, Bundle savedInstanceState) {
		// change identifier to something different than appName
		String appName = getApplicationName(a.getApplicationContext());
		Log.i(TAG, "calling onCreate for app " + appName + " in activity " + a.getPackageName());
		AbstractBlade b = getInstance(a).getBladeLoader().getBlade(appName);
		if (b != null) {
			Log.i(TAG, "found blade, executing onCreate");
			b.onCreate(a, savedInstanceState);
		}
	}
	
	private BladeLoader getBladeLoader() {
		return loader;
	}
	
	private static String getApplicationName(Context context) {
	    int stringId = context.getApplicationInfo().labelRes;
	    return context.getString(stringId);
	}
	
}
