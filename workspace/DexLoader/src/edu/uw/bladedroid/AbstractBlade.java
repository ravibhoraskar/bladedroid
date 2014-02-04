package edu.uw.bladedroid;

import android.app.Activity;
import android.os.Bundle;

public abstract class AbstractBlade implements IBlade {

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

}
