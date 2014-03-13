package edu.uw.bladedroid.blademanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import edu.uw.bladedroid.blademanager.adapter.AppListAdapter;

public class MainActivity extends Activity {
	private final static String TAG = "BladeManager";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ListView listview = (ListView) findViewById(R.id.applicationsList);

		List<BladeDroidPackageInfo> apps = getPackages();

		final AppListAdapter adapter = new AppListAdapter(this, apps);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Intent appDetailIntent = new Intent(view.getContext(), AppDetailActivity.class);
				TextView title = (TextView) view.findViewById(R.id.mainText);
				TextView tv = (TextView) view.findViewById(R.id.secondLine);
				appDetailIntent.putExtra("packageName", tv.getText());
				appDetailIntent.putExtra("appName", title.getText());
				startActivity(appDetailIntent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class BladeDroidPackageInfo implements
			Comparable<BladeDroidPackageInfo> {
		public String appname = "";
		public String pname = "";
		public String versionName = "";
		public int versionCode = 0;
		public Drawable icon;

		public void prettyPrint() {
			Log.v(TAG, appname + "\t" + pname + "\t" + versionName + "\t"
					+ versionCode);
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof BladeDroidPackageInfo) {
				BladeDroidPackageInfo other = (BladeDroidPackageInfo) o;
				return pname.equals(other.pname);
			}
			return false;
		}

		@Override
		public int compareTo(BladeDroidPackageInfo another) {
			return appname.compareToIgnoreCase(another.appname);
		}
	}

	private ArrayList<BladeDroidPackageInfo> getPackages() {
		ArrayList<BladeDroidPackageInfo> apps = getInstalledApps(false);
		Collections.sort(apps);
		final int max = apps.size();
		for (int i = 0; i < max; i++) {
			apps.get(i).prettyPrint();
		}
		return apps;
	}

	private ArrayList<BladeDroidPackageInfo> getInstalledApps(
			boolean getSysPackages) {
		ArrayList<BladeDroidPackageInfo> res = new ArrayList<BladeDroidPackageInfo>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((!getSysPackages) && (p.versionName == null)) {
				// ignore system packages
				continue;
			}
			BladeDroidPackageInfo newInfo = new BladeDroidPackageInfo();
			newInfo.appname = p.applicationInfo.loadLabel(getPackageManager())
					.toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
			res.add(newInfo);
		}
		return res;
	}

}
