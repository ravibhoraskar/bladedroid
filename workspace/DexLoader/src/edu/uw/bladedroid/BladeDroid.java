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
		AbstractBlade b = retrieveBlade(a);
		if (b != null) {
			Log.i(TAG, "found blade, executing onCreate");
			b.onCreate(a, savedInstanceState);
		}
	}
	
	public static void onStart(Activity a, Bundle savedInstanceState) {
		AbstractBlade b = retrieveBlade(a);
		if (b != null) {
			Log.i(TAG, "found blade, executing onStart");
			b.onStart(a, savedInstanceState);
		}
	}
	
	public static void onResume(Activity a, Bundle savedInstanceState) {
		AbstractBlade b = retrieveBlade(a);
		if (b != null) {
			Log.i(TAG, "found blade, executing onResume");
			b.onResume(a, savedInstanceState);
		}
	}
	
	public static void onPause(Activity a, Bundle savedInstanceState) {
		AbstractBlade b = retrieveBlade(a);
		if (b != null) {
			Log.i(TAG, "found blade, executing onPause");
			b.onPause(a, savedInstanceState);
		}
	}
	
	public static void onStop(Activity a, Bundle savedInstanceState) {
		AbstractBlade b = retrieveBlade(a);
		if (b != null) {
			Log.i(TAG, "found blade, executing onStop");
			b.onStop(a, savedInstanceState);
		}
	}
	
	public static void onDestroy(Activity a, Bundle savedInstanceState) {
		AbstractBlade b = retrieveBlade(a);
		if (b != null) {
			Log.i(TAG, "found blade, executing onDestroy");
			b.onDestroy(a, savedInstanceState);
		}
	}
	
	private static AbstractBlade retrieveBlade(Activity a) {
		// change identifier to something different than appName
		String appName = getApplicationName(a.getApplicationContext());
		Log.i(TAG, "calling onCreate for app " + appName + " in activity " + a.getPackageName());
		AbstractBlade b = getInstance(a).getBladeLoader().getBlade(appName);
		return b;
	}
	
	private BladeLoader getBladeLoader() {
		return loader;
	}
	
	private static String getApplicationName(Context context) {
	    int stringId = context.getApplicationInfo().labelRes;
	    return context.getString(stringId);
	}
	
}
