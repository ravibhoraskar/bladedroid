package edu.uw.bladedroid.instrument;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import soot.options.Options;

public class Option {

    private static String apk = null;
    private static String lib = null;
    private static String output_dir = "output";
    private static int bufferNum = -1;
    private static int output_format = Options.output_format_J;
    private static String androidJar = "libs/raviandroid.jar";

    public static String getApk() {
        return apk;
    }

    public static String getLib()
    {
        return lib;
    }

    public static String getOutputDir() {
        return output_dir;
    }

    public static int getOutputfomat() {
        return output_format;
    }

    public static int getBufferNum() {
        return bufferNum;
    }

    public static String getAndroidJar()
    {
        return androidJar;
    }

    public static void clearDirectory(String directory) {
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            File files[] = dir.listFiles();
            for (int i = 0; i < files.length; ++i)
                files[i].delete();
        }
        dir.delete();
    }

    public static void usage() {
        System.out.println("\nGeneral Options:");
        System.out.println("  -h -help\t\t\tDisplay help and exit");
        System.out.println("  -apk\t\t\t\tApk file (Required)");
        System.out.println("  -lib\t\t\t\tJar file to add (Required)");
        System.out.println("  -aj \t\t\t\tPath of Android Jar lib (default:" + androidJar + ")");
        System.out.println("  -of \t\t\t\tOutput format (jimple or dex) (default:jimple)");
        // System.out.println("  -n -num\t\t\tInput the number to buffer ");
        System.out.println("  -d DIR -output-dir DIR\tStore output files in DIR (default: output)");
        System.out.println("  -log\t\t\tTurn on the log (default: on)");
        System.out.println("  -log-out std/FILE\t\tDesignate the file to dump the log");
        System.out.println("\t\t\t\tstd means standard output. (default:std)");
    }

    public static boolean parseArgs(String[] args) {
        List<String> argList = new ArrayList<String>(Arrays.asList(args));
        Iterator<String> it = argList.iterator();

        while (it.hasNext()) {
            String arg = it.next().toLowerCase().trim();
            while (arg.charAt(0) == '-')
                arg = arg.substring(1);

            if (arg.equals("apk")) {
                if (!it.hasNext())
                    return false;
                apk = it.next();
            }
            else if (arg.equals("lib"))
            {
                if (!it.hasNext())
                    return false;
                lib = it.next();
            }
            else if (arg.equals("h") || arg.equals("help")) {
                return false;
            } else if (arg.equals("d") || arg.equals("output-dir")) {
                if (!it.hasNext())
                    return false;
                output_dir = it.next();
                while (output_dir.endsWith("/")) {
                    output_dir = output_dir.substring(0, output_dir.length() - 1);
                }
            } else if (arg.equals("aj")) {
                if (!it.hasNext())
                    return false;
                androidJar = it.next();
            } else if (arg.equals("of"))
            {
                if (!it.hasNext())
                    return false;
                String output_format = it.next();
                if (output_format.equals("dex"))
                {
                    Option.output_format = Options.output_format_dex;
                }
                else if (output_format.toLowerCase().equals("jimple"))
                {
                    Option.output_format = Options.output_format_J;

                }
                System.out.println("Output format = " + Option.output_format);

            } else if (arg.equals("n") || arg.equals("num")) {
                if (!it.hasNext())
                    return false;
                bufferNum = Integer.parseInt(it.next());
            } else if (arg.equals("log")) {
                Log.setDebug(true);
            } else if (arg.equals("log-out")) {
                if (!it.hasNext())
                    return false;
                String file = it.next().toLowerCase();
                if (!file.equals("std")) {
                    try {
                        PrintStream output = new PrintStream(new File(file));
                        Log.setOutputStream(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.setOutputStream(System.out);
                }
            }
        }
        if (apk == null)
            return false;
        if (lib == null)
            return false;
        return true;
    }

}
