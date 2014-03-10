package edu.uw.bladedroid.instrument;

import soot.Body;
import soot.BooleanType;
import soot.Local;
import soot.Modifier;
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

            instrumentCallAtEnd(activity, Info.onCreateSubSignature, bladedroid_onCreate);
            instrumentCallAtEnd(activity, Info.onStartSubSignature, bladedroid_onStart);
            instrumentCallAtEnd(activity, Info.onResumeSubSignature, bladedroid_onResume);
            instrumentCallAtEnd(activity, Info.onPauseSubSignature, bladedroid_onPause);
            instrumentCallAtEnd(activity, Info.onStopSubSignature, bladedroid_onStop);
            instrumentCallAtEnd(activity, Info.onDestroySubSignature, bladedroid_onDestroy);
            instrumentOnKey(activity, Info.onKeyLongPressSubSignature, bladedroid_onKeyLongPress);
            instrumentOnKey(activity, Info.onKeyDownSubSignature, bladedroid_onKeyDown);
            instrumentOnKey(activity, Info.onKeyUpSubSignature, bladedroid_onKeyUp);
        }
    }

    private static void createMethodsIfDontExist(SootClass activity) {
        if (!activity.declaresMethod(Info.onCreateSubSignature))
        {
            System.out.println("subsig: " + Info.onCreateSubSignature);
            Util.addMethodToClass(activity, Info.onCreateSubSignature, Info.onCreateParams(), Info.onCreateReturn);
        }
        if (!activity.declaresMethod(Info.onStartSubSignature))
        {
            Util.addMethodToClass(activity, Info.onStartSubSignature, Info.onStartParams(), Info.onStartReturn);
        }
        if (!activity.declaresMethod(Info.onResumeSubSignature))
        {
            Util.addMethodToClass(activity, Info.onResumeSubSignature, Info.onResumeParams(), Info.onResumeReturn);
        }
        if (!activity.declaresMethod(Info.onPauseSubSignature))
        {
            Util.addMethodToClass(activity, Info.onPauseSubSignature, Info.onPauseParams(), Info.onPauseReturn);
        }
        if (!activity.declaresMethod(Info.onStopSubSignature))
        {
            Util.addMethodToClass(activity, Info.onStopSubSignature, Info.onStopParams(), Info.onStopReturn);
        }
        if (!activity.declaresMethod(Info.onDestroySubSignature))
        {
            Util.addMethodToClass(activity, Info.onDestroySubSignature, Info.onDestroyParams(), Info.onDestroyReturn);
        }
        if (!activity.declaresMethod(Info.onKeyLongPressSubSignature))
        {
            Util.addMethodToClass(activity, Info.onKeyLongPressSubSignature, Info.onKeyLongPressParams(), Info.onKeyLongPressReturn);
        }
        if (!activity.declaresMethod(Info.onKeyDownSubSignature))
        {
            Util.addMethodToClass(activity, Info.onKeyDownSubSignature, Info.onKeyDownParams(), Info.onKeyDownReturn);
        }
        if (!activity.declaresMethod(Info.onKeyUpSubSignature))
        {
            Util.addMethodToClass(activity, Info.onKeyUpSubSignature, Info.onKeyUpParams(), Info.onKeyUpReturn);
        }

        for (SootMethod m : activity.getMethods())
        {
            System.out.println("Method: " + m);
        }

    }

    private static void instrumentOnKey(SootClass activity, String methodSubSignature, SootMethod toCall)
    {
        SootMethod method;
        try
        {
            method = activity.getMethod(methodSubSignature);
        } catch (RuntimeException e)
        {
            throw new RuntimeException("Method not found: " + methodSubSignature + " in class: " + activity.getJavaStyleName() + "\n" + e.getMessage());
        }
        method.setModifiers(method.getModifiers() & (~Modifier.FINAL));
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

    private static void instrumentCallAtEnd(SootClass activity, String methodSubSignature, SootMethod toCall)
    {
        SootMethod method;
        try
        {
            method = activity.getMethod(methodSubSignature);

        } catch (RuntimeException e)
        {
            throw new RuntimeException("Method not found: " + methodSubSignature + " in class: " + activity.getJavaStyleName() + "\n" + e.getMessage());
        }
        method.setModifiers(method.getModifiers() & (~Modifier.FINAL));
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
