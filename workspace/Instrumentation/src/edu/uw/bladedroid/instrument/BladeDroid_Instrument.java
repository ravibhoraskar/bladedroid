package edu.uw.bladedroid.instrument;

import soot.Body;
import soot.BooleanType;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.util.Chain;
import soot.util.HashChain;

public class BladeDroid_Instrument
{

    public static void run()
    {
        SootMethod bladedroid_onCreate = Util.getMethod(Info.bladedroid_onCreate__signature, Info.bladedroid__class);
        SootMethod bladedroid_onStart = Util.getMethod(Info.bladedroid_onStart__signature, Info.bladedroid__class);
        SootMethod bladedroid_onResume = Util.getMethod(Info.bladedroid_onResume__signature, Info.bladedroid__class);
        SootMethod bladedroid_onPause = Util.getMethod(Info.bladedroid_onPause__signature, Info.bladedroid__class);
        SootMethod bladedroid_onStop = Util.getMethod(Info.bladedroid_onStop__signature, Info.bladedroid__class);
        SootMethod bladedroid_onDestroy = Util.getMethod(Info.bladedroid_onDestroy__signature, Info.bladedroid__class);
        SootMethod bladedroid_onKeyLongPress = Util.getMethod(Info.bladedroid_onKeyLongPress__signature, Info.bladedroid__class);
        SootMethod bladedroid_onKeyDown = Util.getMethod(Info.bladedroid_onKeyDown__signature, Info.bladedroid__class);
        SootMethod bladedroid_onKeyUp = Util.getMethod(Info.bladedroid_onKeyUp__signature, Info.bladedroid__class);
        for (SootClass activity : Util.getActivities())
        {
            createMethodsIfDontExist(activity);

            instrumentCallAtEnd(activity, Info.onCreateName, bladedroid_onCreate);
            instrumentCallAtEnd(activity, Info.onStartName, bladedroid_onStart);
            instrumentCallAtEnd(activity, Info.onResumeName, bladedroid_onResume);
            instrumentCallAtEnd(activity, Info.onPauseName, bladedroid_onPause);
            instrumentCallAtEnd(activity, Info.onStopName, bladedroid_onStop);
            instrumentCallAtEnd(activity, Info.onDestroyName, bladedroid_onDestroy);
            instrumentOnKey(activity, Info.onKeyLongPressName, bladedroid_onKeyLongPress);
            instrumentOnKey(activity, Info.onKeyDownName, bladedroid_onKeyDown);
            instrumentOnKey(activity, Info.onKeyUpName, bladedroid_onKeyUp);
        }
    }

    private static void createMethodsIfDontExist(SootClass activity) {
        if (!activity.declaresMethodByName(Info.onCreateName))
        {
            Util.addMethodToClass(activity, Info.onCreateName, Info.onCreateParams(), Info.onCreateReturn);
        }
        if (!activity.declaresMethodByName(Info.onStartName))
        {
            Util.addMethodToClass(activity, Info.onStartName, Info.onStartParams(), Info.onStartReturn);
        }
        if (!activity.declaresMethodByName(Info.onResumeName))
        {
            Util.addMethodToClass(activity, Info.onResumeName, Info.onResumeParams(), Info.onResumeReturn);
        }
        if (!activity.declaresMethodByName(Info.onPauseName))
        {
            Util.addMethodToClass(activity, Info.onPauseName, Info.onPauseParams(), Info.onPauseReturn);
        }
        if (!activity.declaresMethodByName(Info.onStopName))
        {
            Util.addMethodToClass(activity, Info.onStopName, Info.onStopParams(), Info.onStopReturn);
        }
        if (!activity.declaresMethodByName(Info.onDestroyName))
        {
            Util.addMethodToClass(activity, Info.onDestroyName, Info.onDestroyParams(), Info.onDestroyReturn);
        }
        if (!activity.declaresMethodByName(Info.onKeyLongPressName))
        {
            Util.addMethodToClass(activity, Info.onKeyLongPressName, Info.onKeyLongPressParams(), Info.onKeyLongPressReturn);
        }
        if (!activity.declaresMethodByName(Info.onKeyDownName))
        {
            Util.addMethodToClass(activity, Info.onKeyDownName, Info.onKeyDownParams(), Info.onKeyDownReturn);
        }
        if (!activity.declaresMethodByName(Info.onKeyUpName))
        {
            Util.addMethodToClass(activity, Info.onKeyUpName, Info.onKeyUpParams(), Info.onKeyUpReturn);
        }

    }

    private static void instrumentOnKey(SootClass activity, String methodName, SootMethod toCall)
    {
        SootMethod method;
        try
        {
            method = activity.getMethodByName(methodName);
        } catch (RuntimeException e)
        {
            throw new RuntimeException("Method not found: " + methodName);
        }
        Body body = method.retrieveActiveBody();
        Chain<Unit> toInsert = new HashChain<Unit>();
        Local thislocal = Util.findthislocal(body.getUnits());
        Local firstparam = Util.findparamlocal(body.getUnits());
        Local secondparam = Util.findsecondparamlocal(body.getUnits());

        Local returnvalue = Jimple.v().newLocal("returnvalue", BooleanType.v());
        body.getLocals().add(returnvalue);
        toInsert.add(Jimple.v().
                newAssignStmt(returnvalue, Jimple.v().
                        newStaticInvokeExpr(toCall.makeRef(), thislocal, firstparam, secondparam)));

        NopStmt jumpTarget = Jimple.v().newNopStmt();
        toInsert.add(Jimple.v().
                newIfStmt(Jimple.v().
                        newEqExpr(
                                returnvalue,
                                IntConstant.v(0)), jumpTarget));
        toInsert.add(Jimple.v().newReturnStmt(returnvalue));
        toInsert.add(jumpTarget);
        Util.insertAfterIdentityStmt(body.getUnits(), toInsert);
    }

    private static void instrumentCallAtEnd(SootClass activity, String methodName, SootMethod toCall)
    {
        SootMethod method;
        try
        {
            method = activity.getMethodByName(methodName);

        } catch (RuntimeException e)
        {
            throw new RuntimeException("Method not found: " + methodName);
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
