package org.sqldroid;

public class Log {
    public static int LEVEL = android.util.Log.WARN;
    private static final String TAG = "SQLDroid";

    static void d(String message) {
        if (LEVEL<=android.util.Log.DEBUG) android.util.Log.d(TAG, message);
    }

    static void e(String message) {
        if (LEVEL<=android.util.Log.ERROR) android.util.Log.e(TAG, message);
    }

    static void e(String message, Throwable t) {
        if (LEVEL<=android.util.Log.ERROR) android.util.Log.e(TAG, message, t);
    }

    static void i(String message) {
        if (LEVEL<=android.util.Log.INFO) android.util.Log.i(TAG, message);
    }

    static void v(String message) {
        if (LEVEL<=android.util.Log.VERBOSE) android.util.Log.v(TAG, message);
    }

}
