package edu.uw.bladedroid.instrument;

import java.io.PrintStream;

public class Log {

    private static boolean debug_on = false;
    private static PrintStream out = System.out;

    public static void setDebug(boolean flag) {
        debug_on = flag;
    }

    public static void setOutputStream(PrintStream output) {
        out = output;
    }

    public static void print(String str) {
        if (debug_on)
            out.print(str);
    }

    public static void print(int val) {
        if (debug_on)
            out.print(val);
    }

    public static void print(Object obj) {
        if (debug_on)
            out.print(obj.toString());
    }

    public static void println(String str) {
        if (debug_on)
            out.println(str);
    }

    public static void println(int val) {
        if (debug_on)
            out.println(val);
    }

    public static void println(Object obj) {
        if (debug_on)
            out.println(obj.toString());
    }

    public static void println() {
        if (debug_on)
            out.println();
    }
}
