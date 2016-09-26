package org.sqldroid;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class SQLDroidDriver implements java.sql.Driver {

    /** Key passed when the SQLDroidConnection is created.  The value of this key should be an
     * String containing a numeric value which is the complete value of the flags to be passed to the Android SQLite database
     * when the database is opened. If this key is not set, or the passed properties are null then the values<p>
     * android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE<p>
     * will be passed.
     * @see android.database.sqlite.SQLiteDatabase#openDatabase(String, android.database.sqlite.SQLiteDatabase.CursorFactory, int)
     */
    public static final String DATABASE_FLAGS = "DatabaseFlags";

    /** Key passed when the SQLDroidConnection is created.  The value of this key should be an
     * String containing a numeric value which is the value of any additional flags to be passed to the 
     * Android SQLite database when the database is opened. Additional flags will be added to this set then the values<p>
     * android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE<p>
     * If the passed properties are null then just these default keys will be used.
     * @see android.database.sqlite.SQLiteDatabase#openDatabase(String, android.database.sqlite.SQLiteDatabase.CursorFactory, int)
     */
    public static final String ADDITONAL_DATABASE_FLAGS = "AdditionalDatabaseFlags";

    // TODO(uwe):  Allow jdbc:sqlite: url as well
    public static String sqldroidPrefix = "jdbc:sqldroid:";
    /** Provide compatibility with the SQLite JDBC driver from Xerial: <p> 
     * http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC <p>
     * by allowing the URLs to be jdbc:sqlite:
     */
    // this used to be "sqlitePrefix" but it looks too similar to sqldroidPrefix
    // making the code hard to read and easy to mistype.
    public static String xerialPrefix = "jdbc:sqlite:";

    static {
        try {
            java.sql.DriverManager.registerDriver(new SQLDroidDriver());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** Will accept any string that starts with sqldroidPrefix ("jdbc:sqldroid:") or
     * sqllitePrefix ("jdbc:sqlite"). 
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException {

        if(url.startsWith(sqldroidPrefix) || url.startsWith(xerialPrefix)) {
            return true;
        }

        return false;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new SQLDroidConnection(url, info);
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        // TODO Evaluate if implementation is sufficient (if so, delete comment and log)
        Log.e(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return new DriverPropertyInfo[0];
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    // methods added for JDK7 compilation

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException("getParentLogger not supported");
    }
}
