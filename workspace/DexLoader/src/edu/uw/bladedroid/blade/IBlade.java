package edu.uw.bladedroid.blade;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

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
     * @see Activity#onStart
     */
    void onStart(Activity activity);

    /**
     * 
     * @param activity
     * @see Activity#onResume
     */
    void onResume(Activity activity);

    /**
     * 
     * @param activity
     * @see Activity#onPause
     */
    void onPause(Activity activity);

    /**
     * 
     * @param activity
     * @see Activity#onStrop
     */
    void onStop(Activity activity);

    /**
     * 
     * @param activity
     * @see Activity#onDestroy
     */
    void onDestroy(Activity activity);

    /**
     * 
     * @param activity
     * @param keyCode
     * @param event
     * @return
     * @see Activity#onKeyLongPress
     */
    boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event);

}
