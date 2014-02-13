package edu.uw.bladedroid.instrument;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Jimple;
import soot.util.Chain;
import soot.util.HashChain;

public class BladeDroid_Instrument
{
    private final static String bladedroid__class = "edu.uw.bladedroid.BladeDroid";
    private final static String bladedroid_onCreate__signature =
            "<edu.uw.bladedroid.BladeDroid: void onCreate(android.app.Activity,android.os.Bundle)>";
    private final static String bladedroid_onStart__signature =
            "<edu.uw.bladedroid.BladeDroid: void onStart(android.app.Activity)>";
    private final static String bladedroid_onResume__signature =
            "<edu.uw.bladedroid.BladeDroid: void onResume(android.app.Activity)>";
    private final static String bladedroid_onPause__signature =
            "<edu.uw.bladedroid.BladeDroid: void onPause(android.app.Activity)>";
    private final static String bladedroid_onStop__signature =
            "<edu.uw.bladedroid.BladeDroid: void onStop(android.app.Activity)>";
    private final static String bladedroid_onDestroy__signature =
            "<edu.uw.bladedroid.BladeDroid: void onDestroy(android.app.Activity)>";

    public static void run()
    {
        Chain<SootClass> appClassChain = Scene.v().getApplicationClasses();
        SootMethod bladedroid_onCreate = Util.getMethod(bladedroid_onCreate__signature, bladedroid__class);
        SootMethod bladedroid_onStart = Util.getMethod(bladedroid_onStart__signature, bladedroid__class);
        SootMethod bladedroid_onResume = Util.getMethod(bladedroid_onResume__signature, bladedroid__class);
        SootMethod bladedroid_onPause = Util.getMethod(bladedroid_onPause__signature, bladedroid__class);
        SootMethod bladedroid_onStop = Util.getMethod(bladedroid_onStop__signature, bladedroid__class);
        SootMethod bladedroid_onDestroy = Util.getMethod(bladedroid_onDestroy__signature, bladedroid__class);
        for (SootClass activity : Util.getActivities(appClassChain))
        {
            instrumentCallAtEnd(activity, Util.onCreateName, bladedroid_onCreate);
            instrumentCallAtEnd(activity, Util.onStartName, bladedroid_onStart);
            instrumentCallAtEnd(activity, Util.onResumeName, bladedroid_onResume);
            instrumentCallAtEnd(activity, Util.onPauseName, bladedroid_onPause);
            instrumentCallAtEnd(activity, Util.onStopName, bladedroid_onStop);
            instrumentCallAtEnd(activity, Util.onDestroyName, bladedroid_onDestroy);

            SootMethod constructor;
            try
            {
                constructor = activity.getMethodByName(Util.onCreateName);
            } catch (RuntimeException e)
            {
                // If onCreate not found
                constructor = null;
            }
            if (constructor == null)
            {
                continue;
            }
            Body body = constructor.retrieveActiveBody();
            Chain<Unit> toInsert = new HashChain<Unit>();
            Local thislocal = Util.findthislocal(body.getUnits());
        }
    }

    private static void instrumentCallAtEnd(SootClass activity, String methodName, SootMethod toCall)
    {
        SootMethod method;
        try
        {
            method = activity.getMethodByName(methodName);

        } catch (RuntimeException e)
        {
            // TODO: Create method if not exist
            return;
        }
        Body body = method.retrieveActiveBody();
        Chain<Unit> toInsert = new HashChain<Unit>();
        Local thislocal = Util.findthislocal(body.getUnits());
        Local paramlocal = null;
        try
        {
            paramlocal = Util.findparamlocal(body.getUnits());
            System.out.println("Got param local " + paramlocal);
            toInsert.add(Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(toCall.makeRef(), thislocal, paramlocal)));

        } catch (RuntimeException e)
        {
            // Could not find parameter local
            toInsert.add(Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(toCall.makeRef(), thislocal)));
        }
        Util.insertBeforeReturn(body.getUnits(), toInsert);
    }
}
