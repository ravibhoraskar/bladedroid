package edu.uw.bladedroid;

import android.app.Activity;
import android.os.Bundle;

public abstract class AbstractBlade implements IBlade {

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onStart(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onResume(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onPause(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onStop(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onDestroy(Activity activity, Bundle savedInstanceState) {
	}

}
