package edu.uw.bladedroid.blade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import edu.uw.bladedroid.BladeDroid;

public abstract class AbstractBlade {

    protected Matcher scopeMatcher;

    public boolean isForActivity(String activity) {
        if (scopeMatcher == null) {
            BladeScope scope = getClass().getAnnotation(BladeScope.class);
            String exp = (scope == null) ? ".*" : scope.activity();
            scopeMatcher = Pattern.compile(exp).matcher("");
            Log.i(BladeDroid.TAG, "Compiling scope " + String.valueOf(scope) + " for activity " + activity);
        }

        Log.i(BladeDroid.TAG, "Checking scope " + scopeMatcher.pattern().pattern() + " for activity " + activity);
        return scopeMatcher.reset(activity).matches();
    }

    /**
     * 
     * @param activity
     * @param savedInstanceState
     * @see Activity#onCreate
     */
    public void onCreate(Activity activity, Bundle savedInstanceState) {
    }

    /**
     * 
     * @param activity
     * @see Activity#onStart
     */
    public void onStart(Activity activity) {
    }

    /**
     * 
     * @param activity
     * @see Activity#onResume
     */
    public void onResume(Activity activity) {
    }

    /**
     * 
     * @param activity
     * @see Activity#onPause
     */
    public void onPause(Activity activity) {
    }

    /**
     * 
     * @param activity
     * @see Activity#onStrop
     */
    public void onStop(Activity activity) {
    }

    /**
     * 
     * @param activity
     * @see Activity#onDestroy
     */
    public void onDestroy(Activity activity) {
    }

    /**
     * 
     * @param activity
     * @param keyCode
     * @param event
     * @return
     * @see Activity#onKeyDown
     */
    public boolean onKeyDown(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 
     * @param activity
     * @param keyCode
     * @param event
     * @return
     * @see Activity#onKeyLongPress
     */
    public boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * NOT INSTRUMENTED
     * 
     * @param activity
     * @param keyCode
     * @param event
     * @return
     * @see Activity#onKeyMultiple
     */
    public boolean onKeyMultiple(Activity activity, int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    /**
     * NOT INSTRUMENTED
     * 
     * @param activity
     * @param keyCode
     * @param event
     * @return
     * @see Activity#onKeyShortcut
     */
    public boolean onKeyShortcut(Activity activity, KeyEvent event) {
        return false;
    }

    /**
     * @param activity
     * @param keyCode
     * @param event
     * @return
     * @see Activity#onKeyUp
     */
    public boolean onKeyUp(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

}
