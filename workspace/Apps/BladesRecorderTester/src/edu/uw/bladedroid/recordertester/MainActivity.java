package edu.uw.bladedroid.recordertester;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.uw.bladedroid.BladeDroid;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.bta);
        b.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Log.wtf("MAIN ACTIVITY", "clicked!");
            }
            
        });
        BladeDroid.onCreate(this, savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onResume (){
        super.onResume();
        BladeDroid.onResume(this);
    }
}
