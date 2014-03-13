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
        long start = System.nanoTime();
        loader = new BladeLoader(initiator);
        int bladeCount = loader.getBlades().size();
        long time = System.nanoTime() - start;
        Log.i("BLADE-LOADING", bladeCount + "-" + time);
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

    public static boolean onKeyDown(Activity activity, final int keyCode, final KeyEvent event)
    {
        return executeBlades(activity, new BooleanBladeExecutor() {
            @Override
            public boolean execute(Activity activity, AbstractBlade b) {
                return b.onKeyDown(activity, keyCode, event);
            }
        });
    }

    public static boolean onKeyLongPress(Activity activity, final int keyCode, final KeyEvent event)
    {
        return executeBlades(activity, new BooleanBladeExecutor() {
            @Override
            public boolean execute(Activity activity, AbstractBlade b) {
                return b.onKeyLongPress(activity, keyCode, event);
            }
        });
    }

    public static boolean onKeyMultiple(Activity activity, final int keyCode, final int repeatCount, final KeyEvent event)
    {
        return executeBlades(activity, new BooleanBladeExecutor() {
            @Override
            public boolean execute(Activity activity, AbstractBlade b) {
                return b.onKeyMultiple(activity, keyCode, repeatCount, event);
            }
        });
    }

    public static boolean onKeyShortcut(Activity activity, final KeyEvent event)
    {
        return executeBlades(activity, new BooleanBladeExecutor() {
            @Override
            public boolean execute(Activity activity, AbstractBlade b) {
                return b.onKeyShortcut(activity, event);
            }
        });
    }

    public static boolean onKeyUp(Activity activity, final int keyCode, final KeyEvent event)
    {
        return executeBlades(activity, new BooleanBladeExecutor() {
            @Override
            public boolean execute(Activity activity, AbstractBlade b) {
                return b.onKeyUp(activity, keyCode, event);
            }
        });
    }

    private static boolean executeBlades(Activity activity, BooleanBladeExecutor executor) {
        Set<AbstractBlade> blds = getInstance(activity).getBladeLoader().getBlades();
        boolean ret = false;
        for (AbstractBlade b : blds) {
            if (b != null && b.isForActivity(activity.getClass().getName())) {
                Log.i(TAG, "executing blade " + b);
                ret = ret || executor.execute(activity, b);
            } else {
                Log.i(TAG, "omitting execution of blade " + String.valueOf(b));
            }
        }
        Log.i(TAG, "Blade execution returning " + ret);
        return ret;
    }

    private static void executeBlades(Activity activity, BladeExecutor executor) {
        Set<AbstractBlade> blds = getInstance(activity).getBladeLoader().getBlades();
        for (AbstractBlade b : blds) {
            if (b != null && b.isForActivity(activity.getClass().getName())) {
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

    private interface BooleanBladeExecutor {
        boolean execute(Activity a, AbstractBlade b);
    }

    private interface BladeExecutor {
        void execute(Activity a, AbstractBlade b);
    }

}
