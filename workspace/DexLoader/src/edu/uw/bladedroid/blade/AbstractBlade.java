package edu.uw.bladedroid.blade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import edu.uw.bladedroid.BladeDroid;

public abstract class AbstractBlade implements IBlade {

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

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onStart(Activity activity) {
    }

    @Override
    public void onResume(Activity activity) {
    }

    @Override
    public void onPause(Activity activity) {
    }

    @Override
    public void onStop(Activity activity) {
    }

    @Override
    public void onDestroy(Activity activity) {
    }

    @Override
    public boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

}
