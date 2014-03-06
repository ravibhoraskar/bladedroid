package edu.uw.bladedroid;

import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import edu.uw.bladedroid.blade.AbstractBlade;
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

    public static void onCreate(Activity activity, final Bundle savedInstanceState) {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onCreate(activity, savedInstanceState);
            }
        });
    }

    public static void onStart(Activity activity) {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onStart(activity);
            }
        });
    }

    public static void onResume(Activity activity) {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onResume(activity);
            }
        });
    }

    public static void onPause(Activity activity) {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onPause(activity);
            }
        });
    }

    public static void onStop(Activity activity) {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onStop(activity);
            }
        });
    }

    public static void onDestroy(Activity activity) {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onDestroy(activity);
            }
        });
    }

    public static void onKeyDown(Activity activity, final int keyCode, final KeyEvent event)
    {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onKeyDown(activity, keyCode, event);
            }
        });
    }

    public static void onKeyLongPress(Activity activity, final int keyCode, final KeyEvent event)
    {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onKeyLongPress(activity, keyCode, event);
            }
        });
    }

    public static void onKeyMultiple(Activity activity, final int keyCode, final int repeatCount, final KeyEvent event)
    {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onKeyMultiple(activity, keyCode, repeatCount, event);
            }
        });
    }

    public static void onKeyShortcut(Activity activity, final KeyEvent event)
    {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onKeyShortcut(activity, event);
            }
        });
    }

    public static void onKeyUp(Activity activity, final int keyCode, final KeyEvent event)
    {
        executeBlades(activity, new BladeExecutor() {
            @Override
            public void execute(Activity activity, AbstractBlade b) {
                b.onKeyUp(activity, keyCode, event);
            }
        });
    }

    private static void executeBlades(Activity activity, BladeExecutor executor) {
        Set<AbstractBlade> blds = getInstance(activity).getBladeLoader().getBlades();
        for (AbstractBlade b : blds) {
            if (b != null && b.isForActivity(activity.getPackageName())) {
                Log.i(TAG, "executing blade " + b);
                executor.execute(activity, b);
            } else {
                Log.i(TAG, "omitting execution of blade " + String.valueOf(b));
            }
        }
    }

    private BladeLoader getBladeLoader() {
        return loader;
    }

    private interface BladeExecutor {
        void execute(Activity a, AbstractBlade b);
    }

}
