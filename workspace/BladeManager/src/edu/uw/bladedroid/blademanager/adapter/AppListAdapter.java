package edu.uw.bladedroid.blademanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.uw.bladedroid.blademanager.MainActivity.BladeDroidPackageInfo;
import edu.uw.bladedroid.blademanager.R;

public class AppListAdapter extends ArrayAdapter<BladeDroidPackageInfo> {
	private final Context context;
	private final List<BladeDroidPackageInfo> values;

	public AppListAdapter(Context context, List<BladeDroidPackageInfo> objects) {

		super(context, R.layout.app_line, objects);
		this.context = context;
		this.values = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_line, parent, false);
		TextView caption = (TextView) rowView.findViewById(R.id.mainText);
		TextView textView = (TextView) rowView.findViewById(R.id.secondLine);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

		BladeDroidPackageInfo appInfo = values.get(position);
		caption.setText(appInfo.appname);
		// set the app icon
		if (appInfo.icon != null) {
			imageView.setImageDrawable(appInfo.icon);
		}
		textView.setText(appInfo.pname);

		return rowView;
	}
}
