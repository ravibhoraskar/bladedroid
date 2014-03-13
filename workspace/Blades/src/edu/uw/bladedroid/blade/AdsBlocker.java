package edu.uw.bladedroid.blade;

import java.util.Arrays;
import java.util.HashSet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class AdsBlocker extends AbstractBlade {

	private static HashSet<String> adViews = new HashSet<String>(Arrays.asList(
			"com.google.ads.AdView", "com.google.android.gms.ads.AdView",
			"com.mopub.mobileads.MoPubView"));

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.wtf("ADSBLOCKER", "onCreate");
		View rootView = activity.findViewById(android.R.id.content);
		hideAllAdViews(rootView);
	}

	private void hideAllAdViews(View inputView) {
		ViewGroup viewgroup = (ViewGroup) inputView;
		int childCount = viewgroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View v = viewgroup.getChildAt(i);
			try {
				String viewname = v.getClass().getName();
				if (adViews.contains(viewname)) {
//					v.setVisibility(View.INVISIBLE);
					((ViewGroup) v.getParent()).removeView(v);

					Log.wtf("ADSBLOCKER", "FOUND ADVIEW " + viewname);
					childCount--;
					i--;
					continue;
				} else {
					Log.wtf("ADSBLOCKER", viewname + " IS NOT AN ADVIEW");
				}
			} catch (NoClassDefFoundError e) {
			} catch (NullPointerException e) {
				Log.wtf("ADSBLOCKER", e);
			}

			if (v instanceof ViewGroup) {
				hideAllAdViews(v);
			}
		}
	}
}
