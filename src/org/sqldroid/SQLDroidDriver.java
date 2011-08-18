package org.sqldroid;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class SQLDroidDriver implements java.sql.Driver {

    // TODO(uwe): Allow jdbc:sqlite: url as well
    public static String sqldroidPrefix = "jdbc:sqldroid:";

    static {
        try {
            java.sql.DriverManager.registerDriver(new SQLDroidDriver());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {

        if (url.startsWith(sqldroidPrefix))
            return true;

        return false;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {

        return new SqldroidConnection(url, info);
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 2;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

}
