package org.sqldroid;

import java.lang.reflect.Constructor;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class SQLDroidConnection implements Connection {
    /**
    * A map to a single instance of a SQLiteDatabase per DB.
    */
    private static final Map<String, SQLiteDatabase> dbMap =
            new HashMap<String, SQLiteDatabase>();

    /**
    * A map from a connection to a SQLiteDatabase instance.
    * Used to track the use of each instance, and close the database when last conneciton is closed.
    */
    private static final Map<SQLDroidConnection, SQLiteDatabase> clientMap =
            new HashMap<SQLDroidConnection, SQLiteDatabase>();

    /** The Android sqlitedb. */
    private SQLiteDatabase sqlitedb;

    private boolean autoCommit = true;

    /** Will have the value 9 or greater the version of SQLException has the constructor:
     * SQLException(Throwable theCause) otherwise false.
     * API levels 9 or greater have this constructor.
     * If the value is positive and less than 9 then the SQLException does not have the constructor.
     * If the value is &lt; 0 then the capabilities of SQLException have not been determined.
     */
    protected static int sqlThrowable = -1;

    /** Connect to the database with the given url and properties.
     *
     * @param url the URL string, typically something like
     * "jdbc:sqlite:/data/data/your-package/databasefilename" so for example:
     *  "jdbc:sqlite:/data/data/org.sqldroid.examples/databases/sqlite.db"
     * @param info Properties object with options.  Supported options are "timeout", "retry", and "shared".
     */
    public SQLDroidConnection(String url, Properties info) throws SQLException {
        Log.v("SQLDroidConnection: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
        Log.v("New sqlite jdbc from url '" + url + "', " + "'" + info + "'");

        // Make a filename from url
        String dbQname;
        if(url.startsWith(SQLDroidDriver.xerialPrefix)) {
            dbQname = url.substring(SQLDroidDriver.xerialPrefix.length());
        } else {
            // there does not seem to be any possibility of error handling.
            // So we could check that the url starts with SQLDroidDriver.sqldroidPrefix
            // but if it doesn't there's nothing we can do (no Exception is specified)
            // so it has to be assumed that the URL is valid when passed to this method.
            dbQname = url.substring(SQLDroidDriver.sqldroidPrefix.length());
        }
        long timeout = 0;  // default to no retries to be consistent with other JDBC implemenations.
        long retryInterval = 50; // this was 1000 in the original code.  1 second is too long for each loop.
        int queryPart = dbQname.indexOf('?');

        // if there's a query part, we accept "timeout=xxx" and "retry=yyy"
        if ( queryPart > 0 ) {
            dbQname = dbQname.substring(0, queryPart);
            String options = dbQname.substring(queryPart);
            while (options.length() > 0) {
                int optionEnd = options.indexOf('&');
                if (optionEnd == -1) {
                    optionEnd = options.length();
                }
                int equals = options.lastIndexOf('=', optionEnd);
                String optionName = options.substring(0, equals).trim();
                String optionValueString = options.substring(equals+1, optionEnd).trim();
                long optionValue;
                try {
                    optionValue = Long.parseLong(optionValueString);
                    if (optionName.equals("timeout")) {
                        timeout = optionValue;
                    } else if (optionName.equals("retry")) {
                        timeout = optionValue;
                        retryInterval = optionValue;
                    }
                    Log.v("Timeout: " + timeout);
                } catch ( NumberFormatException nfe ) {
                    // print and ignore
                    Log.e("Error Parsing URL \"" + url + "\" Timeout String \"" + optionValueString + "\" is not a valid long", nfe);
                }
                options = options.substring(optionEnd + 1);
            }
        }
        Log.v("opening database " + dbQname);
        int flags = android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY
                | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
                | android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS;
        if ( info != null ) {
            if ( info.getProperty(SQLDroidDriver.DATABASE_FLAGS) != null ) {

                try {
                    flags = Integer.parseInt(info.getProperty(SQLDroidDriver.DATABASE_FLAGS));
                } catch ( NumberFormatException nfe ) {
                    Log.e("Error Parsing DatabaseFlags \"" + info.getProperty(SQLDroidDriver.DATABASE_FLAGS) + " not a number ", nfe);
                }
            } else if ( info != null && info.getProperty(SQLDroidDriver.ADDITONAL_DATABASE_FLAGS) != null ) {
                try {
                    int extraFlags = Integer.parseInt(info.getProperty(SQLDroidDriver.ADDITONAL_DATABASE_FLAGS));
                    flags |= extraFlags;
                } catch ( NumberFormatException nfe ) {
                    Log.e("Error Parsing DatabaseFlags \"" + info.getProperty(SQLDroidDriver.ADDITONAL_DATABASE_FLAGS) + " not a number ", nfe);
                }
            }
        }
        synchronized(dbMap) {
            sqlitedb = dbMap.get(dbQname);
            if (sqlitedb == null) {
                Log.i("SQLDroidConnection: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this + " Opening new database: " + dbQname);
                sqlitedb = new SQLiteDatabase(dbQname, timeout, retryInterval, flags);
                dbMap.put(dbQname, sqlitedb);
                clientMap.put(this, sqlitedb);
            }
        }
    }

    /** Returns the delegate SQLiteDatabase. */
    public SQLiteDatabase getDb() {
        return sqlitedb;
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    /** This will create and return an exception.  For API levels less than 9 this will return
     * a SQLDroidSQLException, for later APIs it will return a SQLException.
     */
    public static SQLException chainException(android.database.SQLException sqlException) {
        if ( sqlThrowable < 0 || sqlThrowable >= 9 ) {
            try {
                sqlThrowable = 9;
                //return new SQLException (sqlException);
                // creating by reflection is significantly slower, but since Exceptions should be unusual
                // this should not be a performance issue.
                final Constructor<?> c = SQLException.class.getDeclaredConstructor(new Class[] {Throwable.class});
                return (SQLException)c.newInstance(new Object[]{sqlException});
            } catch ( Exception e) {
                sqlThrowable = 1;
            }
        }
        // if the code above worked correctly, then the exception will have been returned.  Otherwise, we need
        // to go through this clause and create a SQLDroidSQLException
        try {
            // avoid a direct reference to the sqldroidSQLException so that app > API level 9 do not need that class.
            final Constructor<?> c = SQLDroidConnection.class.getClassLoader().loadClass("org.sqldroid.SQLDroidSQLException").getDeclaredConstructor(new Class[] {android.database.SQLException.class});
            // SQLDroidSQLException is an instance of (direct subclass of) SQLException, so the cast below is correct although
            // the instance created will always be a SQLDroidSQLException
            return (SQLException)c.newInstance(new Object[]{sqlException});
        } catch (Exception e) {
            return new SQLException ("Unable to Chain SQLException " + sqlException.getMessage());
        }
    }

    @Override
    public void close() throws SQLException {
        Log.v("SQLDroidConnection.close(): " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
        if (sqlitedb != null) {
            synchronized(dbMap) {
                clientMap.remove(this);
                if (!clientMap.containsValue(sqlitedb)) {
                    Log.i("SQLDroidConnection.close(): " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this + " Closing the database since since last connection was closed.");
                    sqlitedb.close();
                    dbMap.remove(sqlitedb.dbQname);
                }
            }
            sqlitedb = null;
        } else {
            Log.e("SQLDroidConnection.close(): " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this + " Duplicate close!");
        }
    }

    @Override
    public void commit() throws SQLException {
        if (autoCommit) {
            throw new SQLException("database in auto-commit mode");
        }
        sqlitedb.setTransactionSuccessful();
        Log.d("END TRANSACTION  (commit) " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
        sqlitedb.endTransaction();
        Log.d("BEGIN TRANSACTION (after commit) " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
        sqlitedb.beginTransaction();
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new SQLDroidStatement(this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());

        return null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());

        return null;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return autoCommit;
    }

    @Override
    public String getCatalog() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public int getHoldability() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return 0;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return new SQLDroidDatabaseMetaData(this);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return 0;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        // assuming that "isOpen" doesn't throw a locked exception..
        return sqlitedb == null || sqlitedb.getSqliteDatabase() == null ||
                !sqlitedb.getSqliteDatabase().isOpen();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        sqlitedb.execSQL(sql);
        return "SQLDroid: no return info available from sqlite";
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new SQLDroidPreparedStatement(sql, this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareStatement (sql );
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    }

    @Override
    public void rollback() throws SQLException {
        if (autoCommit) {
            throw new SQLException("database in auto-commit mode");
        }
        Log.d("END TRANSACTION (rollback) " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
        sqlitedb.endTransaction();
        Log.d("BEGIN TRANSACTION (after rollback) " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
        sqlitedb.beginTransaction();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (this.autoCommit == autoCommit) {
            return;
        }
        this.autoCommit = autoCommit;
        if (autoCommit) {
            sqlitedb.setTransactionSuccessful();
            Log.d("END TRANSACTION (autocommit on) " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
            sqlitedb.endTransaction();
        } else {
            Log.d("BEGIN TRANSACTION (autocommit off) " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this);
            sqlitedb.beginTransaction();
        }
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "+ DebugPrinter.getLineNumber());
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "+ DebugPrinter.getLineNumber());
    }

    @Override
    protected void finalize() throws Throwable {
        Log.v(" --- Finalize SQLDroid.");
        if (!isClosed()) {
            close();
        }
        super.finalize();
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        // TODO Auto-generated method stub

    }

    // methods added for JDK7 compilation

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        // TODO Auto-generated method stub
    }

    public int getNetworkTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void abort(Executor executor) throws SQLException {
        // TODO Auto-generated method stub
    }

    public String getSchema() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSchema(String schema) throws SQLException {
        // TODO Auto-generated method stub
    }
}
