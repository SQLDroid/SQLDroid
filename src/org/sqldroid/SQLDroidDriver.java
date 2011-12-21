package org.sqldroid;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class SQLDroidDriver implements java.sql.Driver {

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
