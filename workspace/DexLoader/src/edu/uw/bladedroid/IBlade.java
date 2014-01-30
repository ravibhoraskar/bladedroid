package edu.uw.bladedroid;

import android.app.Activity;
import android.os.Bundle;

public interface IBlade {
	/**
	 * 
	 * @param activity
	 * @param savedInstanceState
	 * @see Activity#onCreate
	 */
	void onCreate(Activity activity, Bundle savedInstanceState);
	/**
	 * 
	 * @param activity
	 * @param savedInstanceState
	 * @see Activity#onStart
	 */
	void onStart(Activity activity, Bundle savedInstanceState);
	/**
	 * 
	 * @param activity
	 * @param savedInstanceState
	 * @see Activity#onResume
	 */
	void onResume(Activity activity, Bundle savedInstanceState);
	/**
	 * 
	 * @param activity
	 * @param savedInstanceState
	 * @see Activity#onPause
	 */
	void onPause(Activity activity, Bundle savedInstanceState);
	/**
	 * 
	 * @param activity
	 * @param savedInstanceState
	 * @see Activity#onStrop
	 */
	void onStop(Activity activity, Bundle savedInstanceState);
	/**
	 * 
	 * @param activity
	 * @param savedInstanceState
	 * @see Activity#onDestroy
	 */
	void onDestroy(Activity activity, Bundle savedInstanceState);
	
}
