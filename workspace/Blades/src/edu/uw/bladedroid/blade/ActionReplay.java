package edu.uw.bladedroid.blade;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ActionReplay extends AbstractBlade{
    
    private final String LOG_TAG = "ACTION_REPLAY";
    private final Mode MODE = Mode.RECORD;
//    private final Mode MODE = Mode.REPLAY;
    private TouchingEventSaver saver;
 
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        Log.wtf(LOG_TAG, "in " + MODE + " mode");
        saver = TouchingEventSaver.getInstance(activity);
        if(MODE == Mode.RECORD){
            Toast.makeText(activity, "RECORDING NOW...", Toast.LENGTH_SHORT).show();
            monitorAllButtonsApproache(activity);
        }else if (MODE == Mode.REPLAY){
            Toast.makeText(activity, "REPLAY ACTIONS", Toast.LENGTH_SHORT).show();
            new ButtonClickAsync().execute(activity);
        }
    }
    
    private void monitorAllButtonsApproache(Activity activity) {
        ViewGroup rootView = (ViewGroup)activity.findViewById(android.R.id.content);  
        resetOnClickListenerOfAllButtons(rootView);
    }

    private void resetOnClickListenerOfAllButtons(ViewGroup rootView) {
        for(int i = 0; i < rootView.getChildCount(); i++){
            View v = rootView.getChildAt(i);
                final View.OnClickListener listener = getOnClickListener(v);
                if(listener != null){
                    v.setOnClickListener(new ClickMonitListener(listener, v.getId()));
                }
            if(v instanceof ViewGroup){
                resetOnClickListenerOfAllButtons((ViewGroup)v);
            }
        }
    }
    
    private class ClickMonitListener implements View.OnClickListener{
        
        private View.OnClickListener listener;
        private int viewID;
        
        public ClickMonitListener(View.OnClickListener listener, int viewID){
            this.listener = listener;
            this.viewID = viewID;
        }

        @Override
        public void onClick(View v) {
            try {
                saver.save(viewID, System.currentTimeMillis());
            } catch (IOException e) {
                Log.wtf(LOG_TAG, "failed to save");
                e.printStackTrace();
            }
            Log.wtf(LOG_TAG, "click action saved");
            listener.onClick(v);
        }
    }
    
    private class ButtonClickAsync extends AsyncTask<Activity, Void, Void>{

        @Override
        protected Void doInBackground(Activity... arg0) {
            Activity activity = arg0[0];
            try {
                //give sometime to render UI
                List<ViewIDTouchTime> idTimes = saver.load();
                if(idTimes.isEmpty()) return null;
                Thread.sleep(1500);
                
                long lastStartTime = 0;
                for(int i = 0; i < idTimes.size(); i++){
                    ViewIDTouchTime idTime = idTimes.get(i);
                    if(i > 0){
                        long interval = idTime.getTime() - idTimes.get(i - 1).getTime();
                        long currentTime = System.currentTimeMillis();
                        long idleTime = interval - (currentTime - lastStartTime) ;
                        if(idleTime > 0){
                            try {
                                Thread.sleep(idleTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    final Button btn = (Button) activity.findViewById(idTime.id);
                    lastStartTime = System.currentTimeMillis();
                    if(btn != null){
                        //perform click on main thread
                        activity.runOnUiThread(new Runnable(){
                            
                            @Override
                            public void run() {
                                btn.performClick();
                            }
                            
                        });
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        
    }
    
    private static class TouchingEventSaver{
        
        private String fileName;
        
        private static TouchingEventSaver instance;
        private Context context;
        private BufferedWriter writer;
        private List<ViewIDTouchTime> touchingSeq;
        
        private TouchingEventSaver(Context context){
            this.context = context;
            fileName = context.getClass().getName() + "_TOUCH_EVENT";
        }
        
        public static TouchingEventSaver getInstance(Context context){
            if (instance == null){
                instance = new TouchingEventSaver(context);
            }
            return instance;
        }
        
        public void save(int viewID, long timeInterval) throws IOException{
            if(writer == null){
                //clean up existing file
                context.deleteFile(fileName);
                
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND | Context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(fos));
            }
            
            writer.write("" + viewID + " " + timeInterval + "\n");
            writer.flush();
        }
        
        public List<ViewIDTouchTime> load() throws FileNotFoundException{
            if(touchingSeq == null){
                
                FileInputStream fis = context.openFileInput(fileName);
                Scanner sc = new Scanner(fis);
                touchingSeq = new LinkedList<ViewIDTouchTime>();
                while(sc.hasNext()){
                    touchingSeq.add(new ViewIDTouchTime(sc.nextInt(), sc.nextLong()));
                }
                Collections.sort(touchingSeq);
                sc.close();
            }
            return touchingSeq;
        }
        
    }
    
    private static class ViewIDTouchTime implements Comparable<ViewIDTouchTime>{
        
        private int id;
        private long time;
        
        public ViewIDTouchTime(int id, long time) {
            this.id = id;
            this.time = time;
        }
        
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public long getTime() {
            return time;
        }
        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public int compareTo(ViewIDTouchTime another) {
            return (int) (time - another.getTime());
        }

        
    }
    
    /**
     * Returns the current View.OnClickListener for the given View
     * @param view the View whose click listener to retrieve
     * @return the View.OnClickListener attached to the view; null if it could not be retrieved
     */
    public View.OnClickListener getOnClickListener(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    //Used for APIs lower than ICS (API 14)
    private View.OnClickListener getOnClickListenerV(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        Field field;

        try {
            field = Class.forName(viewStr).getDeclaredField("mOnClickListener");
            retrievedListener = (View.OnClickListener) field.get(view);
        } catch (NoSuchFieldException ex) {
            Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
    }

    //Used for new ListenerInfo class structure used beginning with API 14 (ICS)
    private View.OnClickListener getOnClickListenerV14(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        String lInfoStr = "android.view.View$ListenerInfo";

        try {
            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
                
            }
        } catch (NoSuchFieldException ex) {
            Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
    }

    public enum Mode{
        RECORD, REPLAY, NONE
    }
}
