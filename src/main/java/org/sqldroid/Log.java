package org.sqldroid;

public class Log {

    public static int LEVEL = android.util.Log.WARN;
    public static ILog LOG = new AndroidLog();

    static void d(String message) {
        LOG.d(message);
    }

    static void e(String message) {
        LOG.e(message);
    }

    static void e(String message, Throwable t) {
        LOG.e(message, t);
    }

    static void i(String message) {
        LOG.i(message);
    }

    static void v(String message) {
        LOG.v(message);
    }

    public interface ILog {

        void d(String message);

        void e(String message);

        void e(String message, Throwable t);

        void i(String message);

        void v(String message);

    }

    public static class AndroidLog implements ILog {

        private static final String TAG = "SQLDroid";

        @Override
        public void d(String message) {
            if (LEVEL <= android.util.Log.DEBUG)
                android.util.Log.d(TAG, message);
        }

        @Override
        public void e(String message) {
            if (LEVEL <= android.util.Log.ERROR)
                android.util.Log.e(TAG, message);
        }

        @Override
        public void e(String message, Throwable t) {
            if (LEVEL <= android.util.Log.ERROR)
                android.util.Log.e(TAG, message, t);
        }

        @Override
        public void i(String message) {
            if (LEVEL <= android.util.Log.INFO)
                android.util.Log.i(TAG, message);
        }

        @Override
        public void v(String message) {
            if (LEVEL <= android.util.Log.VERBOSE)
                android.util.Log.v(TAG, message);
        }
    }
}
