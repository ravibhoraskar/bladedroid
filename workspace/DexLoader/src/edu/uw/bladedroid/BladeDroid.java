package edu.uw.bladedroid;

import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import edu.uw.bladedroid.loader.BladeLoader;

public final class BladeDroid {
    public static final String TAG = "BLADE-DROID";

    private static BladeDroid instance;
    private final BladeLoader loader;

    private BladeDroid(Activity initiator) {
        loader = new BladeLoader(initiator);
    }

    public static synchronized BladeDroid getInstance(Activity initiator) {
        if (instance == null) {
            instance = new BladeDroid(initiator);
        }
        return instance;
    }

    public static void onCreate(Activity activity, Bundle savedInstanceState) {
        executeBlades(activity, savedInstanceState, new BladeExecutor() {
            @Override
            public void execute(Activity activity, Bundle savedInstanceState, AbstractBlade b) {
                b.onCreate(activity, savedInstanceState);
            }
        });
    }

    public static void onStart(Activity activity, Bundle savedInstanceState) {
        executeBlades(activity, savedInstanceState, new BladeExecutor() {
            @Override
            public void execute(Activity activity, Bundle savedInstanceState, AbstractBlade b) {
                b.onStart(activity, savedInstanceState);
            }
        });
    }

    public static void onResume(Activity activity, Bundle savedInstanceState) {
        executeBlades(activity, savedInstanceState, new BladeExecutor() {
            @Override
            public void execute(Activity activity, Bundle savedInstanceState, AbstractBlade b) {
                b.onResume(activity, savedInstanceState);
            }
        });
    }

    public static void onPause(Activity activity, Bundle savedInstanceState) {
        executeBlades(activity, savedInstanceState, new BladeExecutor() {
            @Override
            public void execute(Activity activity, Bundle savedInstanceState, AbstractBlade b) {
                b.onPause(activity, savedInstanceState);
            }
        });
    }

    public static void onStop(Activity activity, Bundle savedInstanceState) {
        executeBlades(activity, savedInstanceState, new BladeExecutor() {
            @Override
            public void execute(Activity activity, Bundle savedInstanceState, AbstractBlade b) {
                b.onStop(activity, savedInstanceState);
            }
        });
    }

    public static void onDestroy(Activity activity, Bundle savedInstanceState) {
        executeBlades(activity, savedInstanceState, new BladeExecutor() {
            @Override
            public void execute(Activity activity, Bundle savedInstanceState, AbstractBlade b) {
                b.onDestroy(activity, savedInstanceState);
            }
        });
    }

    private static void executeBlades(Activity activity, Bundle savedInstanceState, BladeExecutor executor) {
        Set<AbstractBlade> blds = getInstance(activity).getBladeLoader().getBlades();
        for (AbstractBlade b : blds) {
            if (b != null) {
                Log.i(TAG, "executing blade " + b);
                executor.execute(activity, savedInstanceState, b);
            }
        }
    }

    private BladeLoader getBladeLoader() {
        return loader;
    }

    private interface BladeExecutor {
        void execute(Activity a, Bundle savedIntanceState, AbstractBlade b);
    }

}
