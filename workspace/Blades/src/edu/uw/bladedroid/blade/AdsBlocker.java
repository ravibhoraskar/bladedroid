package edu.uw.bladedroid.blade;

import com.google.ads.AdView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

//import com.google.ads

public class AdsBlocker extends AbstractBlade {
	
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.wtf("ADSBLOCKER", "onCreate");
		View rootView = activity.findViewById(android.R.id.content);
		removeAllAdView(rootView);
	}
	
	private void removeAllAdView(View inputView){
		ViewGroup viewgroup=(ViewGroup)inputView;
		int childCount = viewgroup.getChildCount();
		for(int i = 0; i < childCount; i++){
			View v = viewgroup.getChildAt(i);
//			Log.wtf("ADSBLOCKER", v.getClass().getName());
			if(v instanceof AdView){
				((ViewGroup)v.getParent()).removeView(v);
				Log.wtf("ADSBLOCKER", "FOUND ADVIEW");
			}else if(v instanceof ViewGroup){
//				Log.wtf("ADSBLOCKER", "instance of viewgroup");
				removeAllAdView(v);
			}
			
		}
		
	}
}
