package edu.uw.bladedroid.instrument;

import java.util.ArrayList;
import java.util.List;

import soot.BooleanType;
import soot.IntType;
import soot.Scene;
import soot.Type;
import soot.VoidType;

public class Info {
    public final static String bladedroid__class = "edu.uw.bladedroid.BladeDroid";
    public final static String bladedroid_onCreate__signature =
            "<edu.uw.bladedroid.BladeDroid: void onCreate(android.app.Activity,android.os.Bundle)>";
    public final static String bladedroid_onStart__signature =
            "<edu.uw.bladedroid.BladeDroid: void onStart(android.app.Activity)>";
    public final static String bladedroid_onResume__signature =
            "<edu.uw.bladedroid.BladeDroid: void onResume(android.app.Activity)>";
    public final static String bladedroid_onPause__signature =
            "<edu.uw.bladedroid.BladeDroid: void onPause(android.app.Activity)>";
    public final static String bladedroid_onStop__signature =
            "<edu.uw.bladedroid.BladeDroid: void onStop(android.app.Activity)>";
    public final static String bladedroid_onDestroy__signature =
            "<edu.uw.bladedroid.BladeDroid: void onDestroy(android.app.Activity)>";
    public final static String bladedroid_onKeyLongPress__signature =
            "<edu.uw.bladedroid.BladeDroid: boolean onKeyLongPress(android.app.Activity,int,android.view.KeyEvent)>";
    public final static String bladedroid_onKeyDown__signature =
            "<edu.uw.bladedroid.BladeDroid: boolean onKeyDown(android.app.Activity,int,android.view.KeyEvent)>";
    public final static String bladedroid_onKeyUp__signature =
            "<edu.uw.bladedroid.BladeDroid: boolean onKeyUp(android.app.Activity,int,android.view.KeyEvent)>";

    public static final String ActivityClassName = "android.app.Activity";
    public static final String BundleClassName = "android.os.Bundle";
    public static final String KeyEventClassName = "android.view.KeyEvent";
    public static final String onCreateName = "onCreate";
    public static final String onStartName = "onStart";
    public static final String onResumeName = "onResume";
    public static final String onPauseName = "onPause";
    public static final String onStopName = "onStop";
    public static final String onDestroyName = "onDestroy";
    public static final String onKeyLongPressName = "onKeyLongPress";
    public static final String onKeyDownName = "onKeyDown";
    public static final String onKeyUpName = "onKeyUp";
    public static Type onCreateReturn = VoidType.v();
    public static Type onStartReturn = VoidType.v();
    public static Type onResumeReturn = VoidType.v();
    public static Type onPauseReturn = VoidType.v();
    public static Type onStopReturn = VoidType.v();
    public static Type onDestroyReturn = VoidType.v();
    public static Type onKeyLongPressReturn = BooleanType.v();
    public static Type onKeyDownReturn = BooleanType.v();
    public static Type onKeyUpReturn = BooleanType.v();

    public static List<Type> onCreateParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        toReturn.add(Scene.v().getSootClass(BundleClassName).getType());
        return toReturn;
    }

    public static List<Type> onStartParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        return toReturn;
    }

    public static List<Type> onResumeParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        return toReturn;
    }

    public static List<Type> onPauseParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        return toReturn;
    }

    public static List<Type> onStopParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        return toReturn;
    }

    public static List<Type> onDestroyParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        return toReturn;
    }

    public static List<Type> onKeyLongPressParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        toReturn.add(IntType.v());
        toReturn.add(Scene.v().getSootClass(KeyEventClassName).getType());
        return toReturn;
    }

    public static List<Type> onKeyDownParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        toReturn.add(IntType.v());
        toReturn.add(Scene.v().getSootClass(KeyEventClassName).getType());
        return toReturn;
    }

    public static List<Type> onKeyUpParams()
    {
        List<Type> toReturn = new ArrayList<Type>();
        toReturn.add(IntType.v());
        toReturn.add(Scene.v().getSootClass(KeyEventClassName).getType());
        return toReturn;
    }

}
