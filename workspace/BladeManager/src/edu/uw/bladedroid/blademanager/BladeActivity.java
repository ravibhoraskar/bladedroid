package edu.uw.bladedroid.blademanager;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class BladeActivity extends Activity {
	protected static final String TAG = "BladeManager";

	protected List<BladeRef> bladesCaptions;
	protected ArrayAdapter<BladeRef> adapter;

	protected void deleteBlade(int id) {
		BladeRef blade = bladesCaptions.get(id);

		if (BladeUtil.deleteBlade(blade)) {
			removeBladeFromView(blade);
			Log.i(TAG, "deletion successful");
		} else {
			Log.e(TAG, "deletion failed!");
		}
	}
	
	protected void removeBladeFromView(BladeRef blade) {
		bladesCaptions.remove(blade);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail);
		
		// retrieve intent data
		Intent intent = getIntent();
		setTitle(getTitle(intent));

		// set up list view with installed Blades for application
		final ListView listView = (ListView) findViewById(R.id.bladeList);

		bladesCaptions = getBlades(intent);

		adapter = new ArrayAdapter<BladeRef>(this,
				android.R.layout.simple_selectable_list_item, bladesCaptions);
		listView.setAdapter(adapter);

		// set up menu
		registerForContextMenu(listView);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	abstract List<BladeRef> getBlades(Intent intent);
	
	protected String getTitle(Intent intent) {
		return getString(R.string.blades);
	}
	
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
