package org.sqldroid;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;

public class SQLDroidDatabaseMetaData implements DatabaseMetaData {

	private static final String VIEW_TYPE = "VIEW";
	private static final String TABLE_TYPE = "TABLE";
	
	SQLDroidConnection con;
    
	public SQLDroidDatabaseMetaData(SQLDroidConnection con) {
		this.con = con;
	}
	
	@Override
	public boolean allProceduresAreCallable() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean deletesAreDetected(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public ResultSet getAttributes(String catalog, String schemaPattern,
			String typeNamePattern, String attributeNamePattern)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getBestRowIdentifier(String catalog, String schema,
			String table, int scope, boolean nullable) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getCatalogSeparator() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getCatalogTerm() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getCatalogs() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getColumnPrivileges(String catalog, String schema,
			String table, String columnNamePattern) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getColumns(String catalog, String schemaPattern,
	    String tableNamePattern, String columnNamePattern) throws SQLException {

	  // get the list of tables matching the pattern (getTables)
	  // create a Matrix Cursor for each of the tables
	  // create a merge cursor from all the Matrix Cursors
	  // and return the columname and type from:
	  //	"PRAGMA table_info(tablename)"
	  // which returns data like this:
	  //		sqlite> PRAGMA lastyear.table_info(gross_sales); 
	  //		cid|name|type|notnull|dflt_value|pk 
	  //		0|year|INTEGER|0|'2006'|0 
	  //		1|month|TEXT|0||0 
	  //		2|monthlygross|REAL|0||0 
	  //		3|sortcol|INTEGER|0||0 
	  //		sqlite>

	  // and then make the cursor have these columns
	  //		TABLE_CAT String => table catalog (may be null)
	  //		TABLE_SCHEM String => table schema (may be null)
	  //		TABLE_NAME String => table name
	  //		COLUMN_NAME String => column name
	  //		DATA_TYPE int => SQL type from java.sql.Types
	  //		TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
	  //		COLUMN_SIZE int => column size.
	  //		BUFFER_LENGTH is not used.
	  //		DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
	  //		NUM_PREC_RADIX int => Radix (typically either 10 or 2)
	  //		NULLABLE int => is NULL allowed.
	  //		columnNoNulls - might not allow NULL values
	  //		columnNullable - definitely allows NULL values
	  //		columnNullableUnknown - nullability unknown
	  //		REMARKS String => comment describing column (may be null)
	  //		COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
	  //		SQL_DATA_TYPE int => unused
	  //		SQL_DATETIME_SUB int => unused
	  //		CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
	  //		ORDINAL_POSITION int => index of column in table (starting at 1)
	  //		IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
	  //		YES --- if the parameter can include NULLs
	  //		NO --- if the parameter cannot include NULLs
	  //		empty string --- if the nullability for the parameter is unknown
	  //		SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
	  //		SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
	  //		SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
	  //		SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
	  //		IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
	  //		YES --- if the column is auto incremented
	  //		NO --- if the column is not auto incremented
	  //		empty string --- if it cannot be determined whether the column is auto incremented parameter is unknown
	  final String[] columnNames = new String [] {"TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", 
	      "DATA_TYPE",  "TYPE_NAME",  "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", 
	      "NULLABLE", "REMARKS","COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", 
	      "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE", 
	  "IS_AUTOINCREMENT"};
	  final Object[] columnValues = new Object[] {null, null, null, null, null, null, null, null, null, Integer.valueOf(10), 
	          Integer.valueOf(2) /* columnNullableUnknown */, null, null, null, null, Integer.valueOf(-1), Integer.valueOf(-1), "",
	      null, null, null, null, ""};

	  SQLiteDatabase db = con.getDb();
	  final String[] types = new String[] {TABLE_TYPE, VIEW_TYPE};
	  ResultSet rs = null;
	  List<Cursor> cursorList = new ArrayList<Cursor>();
	  try {
	    rs = getTables(catalog, schemaPattern,	tableNamePattern, types);
	    while ( rs.next() ) {
	      Cursor c = null;
	      try {
	        String tableName = rs.getString(3);
	        String pragmaStatement = "PRAGMA table_info('"+ tableName + "')";   // ?)";  substitutions don't seem to work in a pragma statment...
	        c = db.rawQuery(pragmaStatement, new String[] {});
	        MatrixCursor mc = new MatrixCursor (columnNames,c.getCount());
	        while (c.moveToNext() ) {
	          Object[] column = columnValues.clone();
	          column[2] = tableName;
	          column[3] = c.getString(1);
	          String type = c.getString(2);
	          column[5] = type;
	          type = type.toUpperCase();
	          // types are (as far as I can tell, the pragma document is not specific):
	          if ( type.equals("TEXT" ) || type.startsWith("CHAR") ) {
	            column[4] = java.sql.Types.VARCHAR;
	          }
	          else if ( type.equals("NUMERIC") ) {
	            column[4] = java.sql.Types.NUMERIC;
	          }
	          else if ( type.startsWith("INT") ) {
	            column[4] = java.sql.Types.INTEGER;
	          }
	          else if ( type.equals("REAL") ) {
	            column[4] = java.sql.Types.REAL;
	          }
	          else if ( type.equals("BLOB") ) {
	            column[4] = java.sql.Types.BLOB;
	          }
	          else {  // manufactured columns, eg select 100 as something from tablename, may not have a type.
	            column[4] = java.sql.Types.NULL;
	          }
	          int nullable = c.getInt(3);
	          //public static final int columnNoNulls   0
	          //public static final int columnNullable  1
	          //public static final int columnNullableUnknown   2
	          if ( nullable == 0 ) {
	            column[10] = Integer.valueOf(1);
	          } else if ( nullable == 1 ) {
	            column[10] = Integer.valueOf(0);
	          }
	          column[12] = c.getString(4);  // we should check the type for this, but I'm not going to.
	          mc.addRow(column);
	        }
	        cursorList.add(mc);
	      } catch (SQLException e) {
	        // failure of one query will no affect the others...
	        // this will already have been printed.  e.printStackTrace();
	      } finally {
	        if ( c != null ) {
	          c.close();
	        }
	      }
	    }
	  } finally {
	    if ( rs != null ) {
	      try {
	        rs.close();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	  }

	  SQLDroidResultSet resultSet;
	  Cursor[] cursors = new Cursor[cursorList.size()];
	  cursors = cursorList.toArray(cursors);

	  if ( cursors.length == 0 ) {
	    resultSet = new SQLDroidResultSet(new MatrixCursor(columnNames,0));
	  } else if ( cursors.length == 1 ) {
	    resultSet = new SQLDroidResultSet(cursors[0]);
	  } else {
	    resultSet = new SQLDroidResultSet(new MergeCursor( cursors )); 
	  }
	  return resultSet;
	}

	@Override
	public Connection getConnection() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getCrossReference(String primaryCatalog,
			String primarySchema, String primaryTable, String foreignCatalog,
			String foreignSchema, String foreignTable) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getDatabaseMajorVersion() throws SQLException {
		return con.getDb().getSqliteDatabase().getVersion();
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public String getDatabaseProductName() throws SQLException {
		
		return "SQLite for Android";
	}

	@Override
	public String getDatabaseProductVersion() throws SQLException {
		return "";
	}

	@Override
	public int getDefaultTransactionIsolation() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getDriverMajorVersion() {
		return 0;
	}

	@Override
	public int getDriverMinorVersion() {
		return 1;
	}

	@Override
	public String getDriverName() throws SQLException {
		
		return "SQLDroid";
	}

	@Override
	public String getDriverVersion() throws SQLException {
		return "0.0.1 alpha";
	}

	@Override
	public ResultSet getExportedKeys(String catalog, String schema, String table)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getExtraNameCharacters() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getIdentifierQuoteString() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table,
			boolean unique, boolean approximate) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxColumnsInSelect() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxColumnsInTable() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxConnections() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxCursorNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxIndexLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxProcedureNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxRowSize() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxSchemaNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxStatementLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxStatements() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxTableNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxTablesInSelect() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int getMaxUserNameLength() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public String getNumericFunctions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
	  final String[] columnNames = new String [] {"TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "KEY_SEQ", "PK_NAME"};
	  final Object[] columnValues = new Object[] {null, null, null, null, null, null};
	  SQLiteDatabase db = con.getDb();

	  Cursor c = db.rawQuery("pragma table_info('" + table + "')", new String[] {});
	  MatrixCursor mc = new MatrixCursor(columnNames);
	  while (c.moveToNext()) {
	    if(c.getInt(5) > 0) {
	      Object[] column = columnValues.clone();
	      column[2] = table;
	      column[3] = c.getString(1);
	      mc.addRow(column);
	    }
	  }
	  // The matrix cursor should be sorted by column name, but isn't
	  c.close();
	  return new SQLDroidResultSet(mc);
	}

	@Override
	public ResultSet getProcedureColumns(String catalog, String schemaPattern,
			String procedureNamePattern, String columnNamePattern)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getProcedureTerm() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public String getSQLKeywords() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getSQLStateType() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public String getSchemaTerm() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getSchemas() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getSearchStringEscape() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getStringFunctions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getSuperTables(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getSuperTypes(String catalog, String schemaPattern,
			String typeNamePattern) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getSystemFunctions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getTablePrivileges(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getTableTypes() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getTables(String catalog, String schemaPattern,
			String tableNamePattern, String[] types) throws SQLException {

	  if ( types == null ) {
	    types = new String[] {TABLE_TYPE};
	  }
		//		.tables command from here:
		//			http://www.sqlite.org/sqlite.html		
		//		
		//	  SELECT name FROM sqlite_master 
		//		WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%'
		//		UNION ALL 
		//		SELECT name FROM sqlite_temp_master 
		//		WHERE type IN ('table','view') 
		//		ORDER BY 1

		// Documentation for getTables() mandates a certain format for the returned result set.
		// To make the return here compatible with the standard, the following statement is
		// executed.  Note that the only returned value of any consequence is still the table name
		// but now it's the third column in the result set and all the other columns are present
		// The type, which can be 'view', 'table' (maybe also 'index') is returned as the type.
		// The sort will be wrong if multiple types are selected.  The solution would be to select
		// one time with type = ('table' | 'view' ), etc. but I think these would have to be 
		// substituted by hand (that is, I don't think a ? option could be used - but I could be wrong about that.
		final String selectStringStart = "SELECT null AS TABLE_CAT,null AS TABLE_SCHEM, tbl_name as TABLE_NAME, '";
		final String selectStringMiddle = "' as TABLE_TYPE, 'No Comment' as REMARKS, null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SELF_REFERENCING_COL_NAME, null as REF_GENERATION" +
		" FROM sqlite_master WHERE tbl_name LIKE ? AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_metadata' AND upper(type) = ?" +
		" UNION ALL SELECT null AS TABLE_CAT,null AS TABLE_SCHEM, tbl_name as TABLE_NAME, '";
		final String selectStringEnd = "' as TABLE_TYPE, 'No Comment' as REMARKS, null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SELF_REFERENCING_COL_NAME, null as REF_GENERATION" +
		" FROM sqlite_temp_master WHERE tbl_name LIKE ? AND name NOT LIKE 'android_metadata' AND upper(type) = ? ORDER BY 3";

		SQLiteDatabase db = con.getDb();
		List<Cursor> cursorList = new ArrayList<Cursor>();
		for ( String tableType : types ) {
			StringBuffer selectString = new StringBuffer ();
			selectString.append(selectStringStart);
			selectString.append(tableType);
			selectString.append(selectStringMiddle);
			selectString.append(tableType);
			selectString.append(selectStringEnd);
			Cursor c = db.rawQuery(selectString.toString(), new String[] {
					tableNamePattern, tableType.toUpperCase(),
					tableNamePattern, tableType.toUpperCase() });
			cursorList.add(c);
		}
		SQLDroidResultSet resultSet;
		Cursor[] cursors = new Cursor[cursorList.size()];
		cursors = cursorList.toArray(cursors);

		if ( cursors.length == 0 ) {
			resultSet = null;  // is this a valid return?? I think this can only occur on a SQL exception
		}
		else if ( cursors.length == 1 ) {
			resultSet = new SQLDroidResultSet(cursors[0]);
		}
		else {
			resultSet = new SQLDroidResultSet(new MergeCursor( cursors )); 
		}
		return resultSet;
	}

	@Override
	public String getTimeDateFunctions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

    public ResultSet getTypeInfo() throws SQLException {
        String sql = "select "
                + "tn as TYPE_NAME, "
                + "dt as DATA_TYPE, "
                + "0 as PRECISION, "
                + "null as LITERAL_PREFIX, "
                + "null as LITERAL_SUFFIX, "
                + "null as CREATE_PARAMS, "
                + typeNullable + " as NULLABLE, "
                + "1 as CASE_SENSITIVE, "
                + typeSearchable + " as SEARCHABLE, "
                + "0 as UNSIGNED_ATTRIBUTE, "
                + "0 as FIXED_PREC_SCALE, "
                + "0 as AUTO_INCREMENT, "
                + "null as LOCAL_TYPE_NAME, "
                + "0 as MINIMUM_SCALE, "
                + "0 as MAXIMUM_SCALE, "
                + "0 as SQL_DATA_TYPE, "
                + "0 as SQL_DATETIME_SUB, "
                + "10 as NUM_PREC_RADIX from ("
                + "    select 'BLOB' as tn, " + Types.BLOB + " as dt union"
                + "    select 'NULL' as tn, " + Types.NULL + " as dt union"
                + "    select 'REAL' as tn, " + Types.REAL+ " as dt union"
                + "    select 'TEXT' as tn, " + Types.VARCHAR + " as dt union"
                + "    select 'INTEGER' as tn, "+ Types.INTEGER +" as dt"
                + ") order by TYPE_NAME";

        //      if (getTypeInfo == null) {
//      getTypeInfo = con.prepareStatement(sql);
//            
//        }
//
//        getTypeInfo.clearParameters();
//        return getTypeInfo.executeQuery();

        return new SQLDroidResultSet(con.getDb().rawQuery(sql, new String[0]));
    }

	
	
	
	
	
	
	@Override
	public ResultSet getUDTs(String catalog, String schemaPattern,
			String typeNamePattern, int[] types) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getURL() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getUserName() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getVersionColumns(String catalog, String schema,
			String table) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public boolean insertsAreDetected(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException {
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
	public boolean locatorsUpdateCopy() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean nullsAreSortedAtStart() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean nullsAreSortedHigh() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean nullsAreSortedLow() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean othersDeletesAreVisible(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean othersInsertsAreVisible(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean ownDeletesAreVisible(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean ownInsertsAreVisible(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		return false;
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsANSI92FullSQL() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsBatchUpdates() throws SQLException {
		return true;
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsColumnAliasing() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsConvert() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsConvert(int fromType, int toType)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsFullOuterJoins() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws SQLException {
		return true;
	}

	@Override
	public boolean supportsGroupBy() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsGroupByUnrelated() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsLikeEscapeClause() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsMultipleOpenResults() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsMultipleResultSets() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsMultipleTransactions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsNamedParameters() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsNonNullableColumns() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsOrderByUnrelated() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsOuterJoins() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsPositionedDelete() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsPositionedUpdate() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsResultSetConcurrency(int type, int concurrency)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsResultSetHoldability(int holdability)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsResultSetType(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSavepoints() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSelectForUpdate() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsStatementPooling() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsStoredProcedures() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSubqueriesInExists() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSubqueriesInIns() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsTableCorrelationNames() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsTransactionIsolationLevel(int level)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsTransactions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsUnion() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean supportsUnionAll() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean updatesAreDetected(int type) throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean usesLocalFilePerTable() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public boolean usesLocalFiles() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
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
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet getClientInfoProperties() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getFunctionColumns(String catalog, String schemaPattern,
			String functionNamePattern, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getFunctions(String catalog, String schemaPattern,
			String functionNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
  // methods added for JDK7 compilation

  public boolean generatedKeyAlwaysReturned() throws SQLException {
      // TODO Auto-generated method stub
      return false;
  }

  public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
      // TODO Auto-generated method stub
      return null;
  }


}
