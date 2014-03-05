package edu.uw.bladedroid.blade;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class SocialifyBlade extends AbstractBlade{
	Activity activity = null;
	
	
	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) 
	{
		this.activity=activity;
	}
	
	
// TODO: Add this to the AbstractBlade
//	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			postStuff();
			return true;
		}
		return false;
	}
	
	
	
	public void postStuff()
	{
		Intent i = new Intent("android.intent.action.MAIN");
		i.setComponent(ComponentName.unflattenFromString("com.socialify/.PostActivity"));
		i.addCategory("android.intent.category.LAUNCHER");
		i.putExtra("data", "I love "+activity.getApplicationContext().getPackageName());
		activity.startActivity(i);
	}
	
	
	
}
