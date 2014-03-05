package com.socialify;

import javax.security.auth.Destroyable;

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
	
	private boolean posting;
	private String dataToPost;

	@Override
	protected void onCreate(Bundle bundle) 
	{
		super.onCreate(bundle);
	
		
		Bundle data = getIntent().getExtras();
		dataToPost=data.getString("data");
		
		setContentView(R.layout.activity_post);
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
					doPost();
				}
			}
		});
	}

	protected void doPost() {
		if(!posting)
		{
			posting=true;
			Request request = Request
					.newStatusUpdateRequest(Session.getActiveSession(), dataToPost, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							//response.getError().getCategory().
							if(response.getError()==null)
							{
							Log.wtf("Socialify", "Posted to wall! "+response.toString());
							finish();
							}
							else
							{
								showError();
								posting=false;
							}
						}
					});
			request.executeAsync();
		}
	}

	protected void showError() {
		TextView tv = (TextView) findViewById(R.id.tv1);
		tv.setText("Error. Could not post");
		Log.wtf("Socialify", "Error. Could not post");
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
