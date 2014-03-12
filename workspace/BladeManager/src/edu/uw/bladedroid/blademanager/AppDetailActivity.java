package edu.uw.bladedroid.blademanager;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AppDetailActivity extends BladeActivity {

	private String appPackageName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appPackageName = getIntent().getCharSequenceExtra("packageName").toString();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	List<BladeRef> getBlades(Intent intent) {
		return BladeUtil.getInstalledBlades(appPackageName);
	}
	
	@Override
	protected String getTitle(Intent intent) {
		return (intent.getCharSequenceExtra("appName") + " " + getString(R.string.blades));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.installed_blade_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			deleteBlade((int) info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.install_blades:
			// install new blades: start InstallBladeActivity
			Intent appDetailIntent = new Intent(AppDetailActivity.this, InstallBladeActivity.class);
			appDetailIntent.putExtra("packageName", appPackageName);
			startActivityForResult(appDetailIntent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
