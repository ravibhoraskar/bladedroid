package com.socialify;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class PostActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle data) 
	{
		super.onCreate(data);
		
		setContentView(R.layout.activity_main);
		Log.wtf("Socialify","Opening session");
		// start Facebook Login
		
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			
			// callback when session changes state
			@Override
			public void call(Session s, SessionState state,
					Exception exception) {
				Log.wtf("Socialify", "Session Changed!");
				if (s.isOpened()) {
					Log.wtf("Socialify", "Session is opened");
					
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}


}
