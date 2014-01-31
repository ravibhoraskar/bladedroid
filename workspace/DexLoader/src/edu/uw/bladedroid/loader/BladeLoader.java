package edu.uw.bladedroid.loader;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dalvik.system.DexClassLoader;

import edu.uw.bladedroid.BladeDroid;
import edu.uw.bladedroid.AbstractBlade;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
	
	private Context applicationContext;
	private Map<String, AbstractBlade> blades;
	
	public BladeLoader(Activity initiator) {
		applicationContext = initiator.getApplicationContext();
		tmpDir = getContext().getDir("dex", Context.MODE_PRIVATE);
		blades = new HashMap<String, AbstractBlade>();
	
		loadAllBlades();
	}
	
	
	private void loadAllBlades() {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && isJar(pathname.getName());
			}
		};
		
//		String jarFiles = TextUtils.join(File.pathSeparator, );

		if(!BASE_PATH.exists() || !BASE_PATH.isDirectory()) {
			Log.i(BladeDroid.TAG, "No blades found because directory " +BASE_PATH+ " does not exit");
			return;
		}
		
		Log.i(BladeDroid.TAG, "Analyzing folder " + BASE_PATH);
		File[] jarFiles = BASE_PATH.listFiles(filter);
		
		if(jarFiles == null) {
			Log.e(BladeDroid.TAG, "Faied to load jar files from folder. Probably READ_EXTERNAL_STORAGE permission missing.");
			return;
		}
		
		for (File f : jarFiles) {
			Log.i(BladeDroid.TAG, "Loading file " + f.getAbsolutePath());
			// http://stackoverflow.com/questions/3022454/how-to-load-a-java-class-dynamically-on-android-dalvik
			// use separate classLoader for every jar file (classes could have same names etc) 
			DexClassLoader classLoader = new DexClassLoader(f.getPath(), 
					tmpDir.getPath(), null, 
					getClass().getClassLoader());
			
			// TODO: improve this to load all that implement IBlade etc.
			// this is just temporary for testing
			String className = "edu.uw.bladedroid.blade.TestBlade";
			try {
				Class<?> cls = Class.forName(className, true, classLoader);
				Log.i(BladeDroid.TAG, "loaded class " + className + " sucessfully for " +getPlainFilename(f.getName())+ "!");
				blades.put(getPlainFilename(f.getName()), (AbstractBlade) cls.newInstance());
			} catch (Throwable t) {
	            Log.e(BladeDroid.TAG, "Error while loading class from jar file " + f.getPath(), t);
	        }
		}
		
	}
	
	public AbstractBlade getBlade(String appName) {
		return blades.get(appName);
	}
	
	private String getPlainFilename(String filename) {
		return filename.substring(0,  filename.length() - 4);
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
