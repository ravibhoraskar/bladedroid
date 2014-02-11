package edu.uw.bladedroid.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import edu.uw.bladedroid.BladeDroid;
import edu.uw.bladedroid.blade.AbstractBlade;

/**
 * Loads all Blades from a specified location into memory for later use.
 */
public class BladeLoader {

    // note that this needs the READ_EXTERNAL_STORAGE permission
    // http://developer.android.com/reference/android/Manifest.permission.html#READ_EXTERNAL_STORAGE
    private static final String FOLDER_NAME = "blades";
    private static final File BASE_PATH = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
    /**
     * directory where optimized dex files should be written; must not be null
     */
    private final File tmpDir;
    private final File bladeDir;

    private final Context applicationContext;
    private final Set<AbstractBlade> blades;

    public BladeLoader(Activity initiator) {
        applicationContext = initiator.getApplicationContext();
        tmpDir = getContext().getDir("dex", Context.MODE_PRIVATE);
        blades = new HashSet<AbstractBlade>();

        // Only .jar files in the bladeDir are loaded
        // .jar files in subfolder with package name of application
        bladeDir = new File(BASE_PATH + File.separator + getApplicationPackageName());
        bladeDir.mkdirs();

        loadAllBlades();
    }

    private void loadAllBlades() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && isJar(pathname.getName());
            }
        };

        Log.i(BladeDroid.TAG, "Analyzing folder " + bladeDir);
        File[] jarFiles = bladeDir.listFiles(filter);

        if (jarFiles == null) {
            Log.e(BladeDroid.TAG, "Faied to load jar files from folder. Probably READ_EXTERNAL_STORAGE permission missing.");
            return;
        }

        for (File f : jarFiles) {
            Log.i(BladeDroid.TAG, "Loading file " + f.getAbsolutePath());
            loadBlades(f);
        }

    }

    public void loadBlades(File jarFile) {
        DexClassLoader classLoader = new DexClassLoader(jarFile.getPath(),
                tmpDir.getPath(), null, getClass().getClassLoader());

        try {
            DexFile df = DexFile.loadDex(jarFile.getAbsolutePath(), new File(tmpDir, jarFile.getName() + ".odex").getAbsolutePath(), 0);
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements();) {
                String className = iter.nextElement();
                System.out.println("Found class: " + className);
                Class<?> cls = Class.forName(className, true, classLoader);
                Log.i(BladeDroid.TAG, "loaded class " + className + " sucessfully for " + getPlainFilename(jarFile.getName()) + "!");
                if (AbstractBlade.class.isAssignableFrom(cls)) {
                    // blade class found
                    Log.i(BladeDroid.TAG, "Found Blade class: " + className);
                    blades.add((AbstractBlade) cls.newInstance());
                }
            }
        } catch (IOException e) {
            Log.e(BladeDroid.TAG, "Error while receiving classes from " + jarFile, e);
        } catch (ClassNotFoundException e) {
            Log.e(BladeDroid.TAG, "Error while loading class from " + jarFile, e);
        } catch (InstantiationException e) {
            Log.e(BladeDroid.TAG, "Could not create instance of blade in " + jarFile, e);
        } catch (IllegalAccessException e) {
            Log.e(BladeDroid.TAG, "Error when accessing class", e);
        }

    }

    public Set<AbstractBlade> getBlades() {
        return blades;
    }

    private String getPlainFilename(String filename) {
        return filename.substring(0, filename.length() - 4);
    }

    private String getApplicationPackageName() {
        return getContext().getApplicationInfo().packageName;
    }

    private Context getContext() {
        return applicationContext;
    }

    private Locale getLocale() {
        return getContext().getResources().getConfiguration().locale;
    }

    private boolean isJar(String filename) {
        return filename.toLowerCase(getLocale()).endsWith(".jar");
    }
}
