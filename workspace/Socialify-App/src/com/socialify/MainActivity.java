package com.socialify;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
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


	public void getName(View v) 
	{
		//		PackageInfo info;
		//		try {
		//			info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
		//			for (Signature signature : info.signatures) {
		//				MessageDigest md;
		//				md = MessageDigest.getInstance("SHA");
		//				md.update(signature.toByteArray());
		//				String something = new String(Base64.encode(md.digest(), 0));
		//				//String something = new String(Base64.encodeBytes(md.digest()));
		//				Log.e("hash key", something);
		//			}
		//		} catch (NameNotFoundException e1) {
		//			Log.e("name not found", e1.toString());
		//		} catch (NoSuchAlgorithmException e) {
		//			Log.e("no such an algorithm", e.toString());
		//		} catch (Exception e) {
		//			Log.e("exception", e.toString());
		//		}
//		NewPermissionsRequest r= new NewPermissionsRequest(this, "publish_actions");
//		r.setRequestCode(100);
//		Session.getActiveSession().requestNewPublishPermissions(r);		

		Request request = Request
				.newStatusUpdateRequest(Session.getActiveSession(), "y00000", new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						Log.wtf("Socialify", "Posted to wall! "+response.toString());
					}
				});
		request.executeAsync();

		Session session = Session.getActiveSession();
		Log.wtf("Socialify","Making me request!");
		Log.wtf("Socialify", "Accesstoken:"+session.getAccessToken());
		Log.wtf("Socialify", "AppID:"+session.getApplicationId());
		Log.wtf("Socialify", "State:"+session.getState().name());

		Request.newMeRequest(session,  new Request.GraphUserCallback() {

			// callback after Graph API response with user object
			@Override
			public void onCompleted(GraphUser user, Response response) {
				Log.wtf("Socialify", "Got response");
				if (user != null) {
					TextView welcome = (TextView) findViewById(R.id.welcome);
					welcome.setText("Hello " + user.getName() + "!");
				}
				Log.wtf("Socialify", "User: "+user);
			}
		}).executeAsync();
	}

}
