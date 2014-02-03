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

    public static void run()
    {
        Chain<SootClass> appClassChain = Scene.v().getApplicationClasses();
        SootMethod bladedroid_onCreate = Util.getMethod(bladedroid_onCreate__signature, bladedroid__class);
        for (SootClass activity : Util.getActivities(appClassChain))
        {
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
            Local paramlocal = Util.findparamlocal(body.getUnits());
            toInsert.add(Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(bladedroid_onCreate.makeRef(), thislocal, paramlocal)));
            Util.insertBeforeReturn(body.getUnits(), toInsert);
        }
    }
}
