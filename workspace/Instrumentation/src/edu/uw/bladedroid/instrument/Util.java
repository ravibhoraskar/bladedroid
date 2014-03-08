package edu.uw.bladedroid.instrument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.util.Chain;
import soot.util.HashChain;

public class Util {

    private static SootClass activityClass = null;

    public static void init()
    {
        activityClass = Scene.v().getSootClass(Info.ActivityClassName);
    }

    public static Chain<SootClass> getActivities()
    {
        Chain<SootClass> appClassChain = Scene.v().getApplicationClasses();
        Chain<SootClass> activities = new HashChain<SootClass>();
        for (SootClass clazz : appClassChain)
        {
            if (isActivity(clazz))
            {
                activities.add(clazz);
            }
        }
        return activities;
    }

    public static Chain<SootClass> getBladeDroidClasses()
    {
        Chain<SootClass> toReturn = new HashChain<SootClass>();
        for (SootClass sootClass : Scene.v().getClasses())
        {
            if (sootClass.getJavaPackageName().contains("edu.uw.bladedroid"))
            {
                toReturn.add(sootClass);
            }
        }

        return toReturn;
    }

    public static boolean isActivity(SootClass clazz)
    {
        if (getSuperClasses(clazz).contains(activityClass))
        {
            System.out.println(clazz.getJavaStyleName() + " is an activity");

            return true;
        }
        else
        {
            System.out.println(clazz.getJavaStyleName() + " is not an activity");
            return false;
        }
    }

    private static Set<SootClass> getSuperClasses(SootClass clazz)
    {
        Set<SootClass> superclasses = new HashSet<SootClass>();
        while (clazz.hasSuperclass())
        {
            superclasses.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return superclasses;
    }

    public static void insertBeforeReturn(Chain<Unit> units,
            Chain<Unit> toInsert)
    {
        Unit point = null;
        Iterator<Unit> unitIt = units.snapshotIterator();
        while (unitIt.hasNext())
        {
            Unit unit = unitIt.next();
            if (unit instanceof ReturnVoidStmt)
            {
                point = unit;

                break;
            }
        }
        if (point == null)
        {
            units.addAll(toInsert);
        }
        else
        {
            units.insertBefore(toInsert, point);
        }
    }

    public static void insertBeforeReturn(Chain<Unit> units, Unit toInsert)
    {
        HashChain<Unit> ti = new HashChain<Unit>();
        ti.add(toInsert);
        insertBeforeReturn(units, ti);
    }

    public static void insertAfterIdentityStmt(PatchingChain<Unit> units,
            Chain<Unit> toInsert) {
        Unit point = null;
        Iterator<Unit> unitIt = units.snapshotIterator();
        boolean foundIdentityStmt = false;
        while (unitIt.hasNext())
        {
            Unit unit = unitIt.next();
            if (!(unit instanceof IdentityStmt) && !(unit instanceof EnterMonitorStmt))
            {
                point = units.getPredOf(unit);
                break;
            }
            else
            {
                foundIdentityStmt = true;
            }
        }
        if (point == null && foundIdentityStmt)
        {
            point = units.getLast();
        }
        else if (point == null && !foundIdentityStmt)
        {
            units.insertBefore(toInsert, units.getFirst());
        }
        units.insertAfter(toInsert, point);
    }

    public static Local findthislocal(Chain<Unit> units)
    {
        // TODO: HACK! assume the first identity stmt is always for 'this'
        for (Unit unit : units)
        {
            if (unit instanceof IdentityStmt)
            {
                IdentityStmt istmt = (IdentityStmt) unit;
                return (Local) istmt.getLeftOp();
            }
        }
        return null;
    }

    public static Local findparamlocal(PatchingChain<Unit> units) {
        // TODO HACK! assume the second identity stmt is always for the required
        // parameter
        boolean thislocalseen = false;
        for (Unit unit : units)
        {
            if (unit instanceof IdentityStmt)
            {
                if (thislocalseen)
                {
                    IdentityStmt istmt = (IdentityStmt) unit;
                    return (Local) istmt.getLeftOp();
                }
                else
                {
                    thislocalseen = true;
                }
            }
        }
        throw new RuntimeException("Parameter Local not found (maybe method has no parameters?)");
    }

    public static Local findsecondparamlocal(PatchingChain<Unit> units)
    {
        // TODO HACK! assume the second identity stmts are in the order
        // "this","param1","param2"...
        int localsseen = 0;
        for (Unit unit : units)
        {
            if (unit instanceof IdentityStmt)
            {
                if (localsseen == 2)
                {
                    IdentityStmt istmt = (IdentityStmt) unit;
                    return (Local) istmt.getLeftOp();
                }

                localsseen++;
            }
        }
        throw new RuntimeException("Parameter Local not found (maybe method has no parameters?)");
    }

    public static SootMethod getMethod(String methodSignature, String className)
    {
        Scene.v().forceResolve(className, SootClass.SIGNATURES);
        return Scene.v().getMethod(methodSignature);
    }

    public static SootClass getClass(String clazz) {
        return Scene.v().getSootClass(clazz);
    }

    public static void addMethodToClass(SootClass activity, String methodSubSignature, List<Type> params, Type returnType) {
        if (activity.isPhantom() || activity.isPhantomClass() || activity.isLibraryClass())
            return;
        if (!activity.getSuperclass().declaresMethod(methodSubSignature))
        {
            addMethodToClass(activity.getSuperclass(), methodSubSignature, params, returnType);
        }

        System.out.println("Adding method: " + methodSubSignature + " to class: " + activity);
        String methodName = subSignatureToName(methodSubSignature);
        SootMethod method = new SootMethod(methodName, params, returnType);
        method.setDeclaringClass(activity);
        try // Hack to avoid weird error.
        {
            activity.addMethod(method);
        } catch (RuntimeException e)
        {
            System.err.println("Weird error: " + e.getMessage());
            return;
        }
        System.out.println("Added method: " + method + " to class: " + activity);

        method.setActiveBody(Jimple.v().newBody(method));

        Local thislocal = Jimple.v().newLocal("thislocal", activity.getType());
        method.getActiveBody().getLocals().add(thislocal);

        method.getActiveBody().getUnits().add(Jimple.v().newIdentityStmt(
                thislocal, Jimple.v().newThisRef(activity.getType())));

        List<Local> paramlocals = new ArrayList<Local>();
        for (int i = 0; i < method.getParameterCount(); i++)
        {
            Local l = Jimple.v().newLocal("local" + i, method.getParameterType(i));
            method.getActiveBody().getLocals().add(l);
            method.getActiveBody().getUnits().add(Jimple.v().newIdentityStmt(
                    l, Jimple.v().newParameterRef(method.getParameterType(i), i)));
            paramlocals.add(l);
        }

        if (returnType instanceof VoidType)
        {
            try
            {
                method.getActiveBody().getUnits().add(Jimple.v().
                        newInvokeStmt(Jimple.v().newSpecialInvokeExpr(
                                thislocal,
                                activity.getSuperclass().getMethod(method.getSubSignature()).makeRef(),
                                paramlocals)));
            } catch (RuntimeException e)
            {
                System.out.println("Superclass of " + activity + " no have " + method.getSubSignature());
            }
            method.getActiveBody().getUnits().add(Jimple.v().
                    newReturnVoidStmt());
        }
        else
        {
            Local retLocal = Jimple.v().newLocal("returnlocal", returnType);
            method.getActiveBody().getLocals().add(retLocal);
            try
            {
                method.getActiveBody().getUnits().add(Jimple.v().
                        newAssignStmt(retLocal, Jimple.v().newSpecialInvokeExpr(
                                thislocal,
                                activity.getSuperclass().getMethod(method.getSubSignature()).makeRef(),
                                paramlocals)));
            } catch (RuntimeException e)
            {
                System.out.println("Superclass of " + activity + " no have " + method.getSubSignature());
                method.getActiveBody().getUnits().add(Jimple.v().
                        newAssignStmt(retLocal, getDefaultValue(returnType)));
            }
            method.getActiveBody().getUnits().add(Jimple.v().
                    newReturnStmt(retLocal));
        }
    }

    public static Value getDefaultValue(Type type) {

        if ((type instanceof BooleanType)
                || (type instanceof ByteType)
                || (type instanceof CharType)
                || (type instanceof DoubleType)
                || (type instanceof FloatType)
                || (type instanceof IntType)
                || (type instanceof LongType)
                || (type instanceof ShortType))
        {
            return IntConstant.v(0);
        }
        else
        {
            return NullConstant.v();
        }
    }

    public static String subSignatureToName(String subSignature) {
        int index = subSignature.indexOf('(');
        if (index < 0)
        {
            throw new RuntimeException("Invalid subSignature");
        }
        subSignature = subSignature.substring(0, index);
        index = subSignature.indexOf(' ');
        return subSignature.substring(index + 1);
    }
}
