package edu.uw.bladedroid.instrument;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ReturnVoidStmt;
import soot.util.Chain;
import soot.util.HashChain;

public class Util {

	private static final String ActivityClassName = "android.app.Activity";
	public static final String onCreateName = "onCreate";

	private static SootClass activityClass = null;



	public static void init()
	{
		activityClass = Scene.v().getSootClass(ActivityClassName);
	}

	public static Chain<SootClass> getActivities(Chain<SootClass> appClassChain)
	{
		Chain<SootClass> activities = new HashChain<SootClass>();
		for(SootClass clazz : appClassChain)
		{
			if(isActivity(clazz))
			{
				activities.add(clazz);
			}
		}
		return activities;
	}

	public static boolean isActivity(SootClass clazz) 
	{
		if(getSuperClasses(clazz).contains(activityClass))
		{
			System.out.println(clazz.getJavaStyleName()+" is an activity");

			return true;
		}
		else
		{
			System.out.println(clazz.getJavaStyleName()+" is not an activity");
			return false;
		}
	}

	private static Set<SootClass> getSuperClasses(SootClass clazz) 
	{
		Set<SootClass> superclasses = new HashSet<SootClass>();
		while(clazz.hasSuperclass())
		{
			superclasses.add(clazz);
			clazz=clazz.getSuperclass();
		}
		return superclasses;
	}

	public static void insertBeforeReturn(Chain<Unit> units,
			Chain<Unit> toInsert) 
	{
		Unit point= null;
		Iterator<Unit> unitIt = units.snapshotIterator();
		while(unitIt.hasNext())
		{
			Unit unit = unitIt.next();
			if (unit instanceof ReturnVoidStmt)
			{
				point = unit;

				break;
			}	
		}
		if(point == null)
		{
			units.addAll(toInsert);
		}
		else
		{
			units.insertBefore(toInsert, point);
		}
	}

	public static void insertBeforeReturn (Chain<Unit> units, Unit toInsert)
	{
		HashChain<Unit> ti = new HashChain<Unit>();
		ti.add(toInsert);
		insertBeforeReturn(units, ti);
	}

	public static Local findthislocal(Chain<Unit> units) 
	{
		//TODO: HACK! assume the first identity stmt is always for 'this'
		for(Unit unit : units)
		{
			if(unit instanceof IdentityStmt)
			{
				IdentityStmt istmt = (IdentityStmt)unit;
				return (Local) istmt.getLeftOp();
			}
		}
		return null;
	}

	public static Local findparamlocal(PatchingChain<Unit> units) {
		// TODO HACK! assume the second identity stmt is always for the required parameter
		boolean thislocalseen=false;
		for(Unit unit: units)
		{
			if(unit instanceof IdentityStmt)
			{
				if(thislocalseen)
				{
					IdentityStmt istmt = (IdentityStmt)unit;
					return (Local) istmt.getLeftOp();
				}
				else
				{
					thislocalseen=true;
				}
			}
		}
		throw new RuntimeException("Parameter Local not found (maybe method has no parameters?)");
	}

	public static SootMethod getMethod(String methodSignature,String className) 
	{
		Scene.v().forceResolve(className, SootClass.SIGNATURES);
		return Scene.v().getMethod(methodSignature);
	}

	public static SootClass getClass(String clazz) {
		return Scene.v().getSootClass(clazz);
	}




}
