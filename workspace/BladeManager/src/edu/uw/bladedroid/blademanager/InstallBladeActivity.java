package edu.uw.bladedroid.blademanager;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class InstallBladeActivity extends BladeActivity {
	private String appPackageName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appPackageName = getIntent().getCharSequenceExtra("packageName").toString();
		super.onCreate(savedInstanceState);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {

				BladeRef blade = bladesCaptions.get((int) id);
				DialogClickListener dialogClickListener =  new DialogClickListener(blade);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(InstallBladeActivity.this);
				String msg = getResources().getString(R.string.do_you_want_to_install_s_for_s_, blade, appPackageName);
				builder.setMessage(msg).setPositiveButton(R.string.yes, dialogClickListener)
				    .setNegativeButton(R.string.no, dialogClickListener).show();
				
	        }
	    });
	}

	@Override
	List<BladeRef> getBlades(Intent intent) {
		List<BladeRef> blades = BladeUtil.getAvailableBlades();
		List<BladeRef> installed = BladeUtil.getInstalledBlades(intent
				.getCharSequenceExtra("packageName").toString());
		// remove already installed Blades
		blades.removeAll(installed);
		return blades;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.install_blade, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	private class DialogClickListener implements DialogInterface.OnClickListener {
		
		 private BladeRef blade;

		public DialogClickListener(BladeRef blade) {
			this.blade = blade;
		}

		@Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	try{
						BladeUtil.installBlade(blade, appPackageName);
						removeBladeFromView(blade);
					} catch (IOException e) {
						Log.e(TAG, "Failed to install blade " + blade + " to app " + appPackageName, e);
					}
		        	break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked: ignore
		            break;
		        }
		    }
	}
	
}
