package edu.uw.bladedroid.instrument;

import java.util.LinkedList;
import java.util.List;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

public class Main {
    private static Main instance = new Main();

    public static Main v() {
        return instance;
    }

    private Main() {
    }

    public void run() {

        List<String> process_dir = new LinkedList<String>();
        process_dir.add(Option.getApk());
        process_dir.add(Option.getLib());
        Options.v().set_process_dir(process_dir);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_output_format(Option.getOutputfomat());
        Options.v().set_allow_phantom_refs(true);
        Options.v().force_android_jar();
        Options.v().set_force_android_jar(Option.getAndroidJar());
        // Options.v().set_soot_classpath(Option.getApk() + ":" +
        // "libs/classes.jar"+":"+Option.getAndroidJar());
        Options.v().set_output_dir(Option.getOutputDir());
        Options.v().set_keep_line_number(true);

        Scene.v().loadNecessaryClasses();
        PackManager.v().runPacks();
        System.out.println("----------------------- converting apk to Jimple ends ----------------------");
        Util.init();
        // Insert instrumentation code here
        BladeDroid_Instrument.run();
        // for(SootClass c : Scene.v().getClasses())
        // {
        // System.out.println(c.getJavaPackageName()+"."+c.getJavaStyleName());
        // }

        System.out.println("Start to output Jimple code.");
        Option.clearDirectory(Option.getOutputDir());
        for (SootClass sootClass : Util.getActivities())
        {
            PackManager.v().writeClass(sootClass);
        }
        for (SootClass sootClass : Util.getBladeDroidClasses())
        {
            PackManager.v().writeClass(sootClass);
        }
        // PackManager.v().writeOutput();
    }

    public static void main(String[] args) {

        Log.setDebug(true);
        if (!Option.parseArgs(args)) {
            Option.usage();
            return;
        }

        Main m = Main.v();
        m.run();
    }
}
