package com.lemadi.storage.database.sqldroid;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqldroidConnection implements Connection {

	SQLiteDatabase sqlitedb;
	String url;

	public SqldroidConnection(String url, Properties info) {
		this.url = url;

		Log.i("SQLDRoid", "new sqlite jdbc from url '" + url + "'");
		
		 
		// Make a filename from url
		String dbQname = url.substring(SqldroidDriver.sqldroidPrefix.length());
		Log.i("SQlDRoid", "opening database " + dbQname);

		sqlitedb = SQLiteDatabase.openDatabase(dbQname, null,
				SQLiteDatabase.CREATE_IF_NECESSARY
				| SQLiteDatabase.OPEN_READWRITE);

	}

	public SQLiteDatabase getDb() {
		return sqlitedb;
	}

	@Override
	public void clearWarnings() throws SQLException {
	}

	@Override
	public void close() throws SQLException {

		if (sqlitedb != null)
			sqlitedb.close();
		
		sqlitedb = null;

	}

	@Override
	public void commit() throws SQLException {

		sqlitedb.endTransaction();

	}

	@Override
	public Statement createStatement() throws SQLException {

		return new SqldroidStatement(this);
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return false;
	}

	@Override
	public String getCatalog() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public int getHoldability() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return 0;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return 0;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return false;
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return false;
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		sqlitedb.execSQL(sql);
		return "SQLDROID: no return info available from sqlite";
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {

		
		
		return new SqldroidPreparedStatement(sql, this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void rollback() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

		return null;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());

	}

	
	@Override
	protected void finalize() throws Throwable {
		Log.i("SQLDRoid", " --- Finalize sqlDRoid, closing db.");
		if(sqlitedb != null)
			sqlitedb.close();
		
		sqlitedb = null;
		super.finalize();
	}
}
