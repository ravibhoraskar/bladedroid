package edu.uw.bladedroid.blade;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

@BladeScope(activity="com.galisoft.cartoonquiz.ActivityQuizScreen")
public class CartoonQuiz extends AbstractBlade {

	private Timer t;
	
	private Object getField(Object obj, String name) {
		Field f;
		try {
			f = obj.getClass().getDeclaredField(name);
			f.setAccessible(true);
			return f.get(obj);
		} catch (Exception e) {
			Log.e("BLADE-DROID", "reflection went wrong", e);
		}
		return null;
	}

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		executePeriodically(activity);
	}
	
	@Override
	public void onPause(Activity activity) {
		t.cancel();
	}
	
	@Override
	public void onResume(Activity activity) {
		executePeriodically(activity);
	}
	
	@Override
	public void onStop(Activity activity) {
		t.cancel();
	}
	
	@SuppressWarnings("rawtypes")
	private void showAnswer(Activity activity) {
		// this.pregunta <= index of right answer
		int index = (Integer) getField(activity, "pregunta");
		Log.i("SOLUTION", "at index: " + index);
		
		List b = (List) getField(activity, "botonPreguntas");
		Button button = (Button) b.get(index - 1);
		if(button.getText().charAt(0) != '*') {
			button.setText(String.format("* %s *", button.getText()));
		}
	}
	
	private void executePeriodically(final Activity activity) {
		t = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showAnswer(activity);
					}  
				});
			}
		};
		t.schedule(task, 0L, 500L);
	}
}
