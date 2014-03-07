package edu.uw.bladedroid.blademanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class BladeUtil {
	private static final String TAG = "BladeUtil";

	private static final String FOLDER_NAME = "blades";
	private static final File BASE_PATH = new File(
			Environment.getExternalStorageDirectory(), FOLDER_NAME);
	private static final String AVAILABLE_BLADES_FOLDER = "all";

	public static List<BladeRef> getAvailableBlades() {
		return getInstalledBlades(AVAILABLE_BLADES_FOLDER);
	}

	public static List<BladeRef> getInstalledBlades(String packageName) {
		File bladeDir = new File(BASE_PATH + File.separator + packageName);
		bladeDir.mkdirs();

		File[] bladeFiles = bladeDir.listFiles(new FilenameFilter() {

			@SuppressLint("DefaultLocale")
			@Override
			public boolean accept(File dir, String filename) {
				return filename.toLowerCase().endsWith(".jar");
			}
		});
		// should guarantee same sorting for both arrays
		Arrays.sort(bladeFiles);

		List<BladeRef> bladesCaptions = new ArrayList<BladeRef>(
				bladeFiles.length);
		for (File f : bladeFiles) {
			BladeRef r = new BladeRef(f);
			bladesCaptions.add(r);
		}
		return bladesCaptions;
	}

	public static boolean deleteBlade(BladeRef blade) {
		Log.i(TAG,
				String.format("Deleting Blade with at location %s.",
						blade.getLocation()));
		return blade.getLocation().delete();
	}
	
	public static void installBlade(BladeRef blade, String appPackageName) throws IOException {
		File destFolder = new File(BASE_PATH + File.separator + appPackageName);
		
		File src = blade.getLocation();
		File dest = new File(destFolder, src.getName());
		copy(src, dest);
	}
	
	public static void copy(File src, File dst) throws IOException {
	    FileInputStream inStream = new FileInputStream(src);
	    FileOutputStream outStream = new FileOutputStream(dst);
	    FileChannel inChannel = inStream.getChannel();
	    FileChannel outChannel = outStream.getChannel();
	    inChannel.transferTo(0, inChannel.size(), outChannel);
	    inStream.close();
	    outStream.close();
	}
}
