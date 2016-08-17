package org.sqldroid;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLDroidDatabaseMetaData implements DatabaseMetaData {

	private final static Map<String, Integer> RULE_MAP = new HashMap<String, Integer>();
	private static final int SQLITE_DONE       =  101;
	private static final String VIEW_TYPE = "VIEW";
	private static final String TABLE_TYPE = "TABLE";
	
	private PreparedStatement
	getTableTypes        = null,   getCatalogs          = null,
	getUDTs              = null,   getSuperTypes        = null,
	getTablePrivileges   = null,   getProcedures        = null,
	getProcedureColumns   = null,   getAttributes        = null,
	getBestRowIdentifier  = null,   getVersionColumns    = null,
	getColumnPrivileges   = null;
	
	SQLDroidConnection con;

	/**
	 * Pattern used to extract a named primary key.
	 */
	protected final static Pattern FK_NAMED_PATTERN =
			Pattern.compile(".* constraint +(.*?) +foreign +key *\\((.*?)\\).*", Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern used to extract column order for an unnamed primary key.
	 */
	protected final static Pattern PK_UNNAMED_PATTERN =
			Pattern.compile(".* primary +key *\\((.*?,+.*?)\\).*", Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern used to extract a named primary key.
	 */
	protected final static Pattern PK_NAMED_PATTERN =
			Pattern.compile(".* constraint +(.*?) +primary +key *\\((.*?)\\).*", Pattern.CASE_INSENSITIVE);

	public SQLDroidDatabaseMetaData(SQLDroidConnection con) {
		this.con = con;
	}
	
	@Override
	public boolean allProceduresAreCallable() throws SQLException {
		return false;
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		return true;
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		return false;
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		return false;
	}

	@Override
	public boolean deletesAreDetected(int type) throws SQLException {
		return false;
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		return false;
	}

	@Override
	public ResultSet getAttributes(String catalog, String schemaPattern,
			String typeNamePattern, String attributeNamePattern)
			throws SQLException {
		if (getAttributes == null) {
			getAttributes = con.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, " +
					"null as TYPE_NAME, null as ATTR_NAME, null as DATA_TYPE, " +
					"null as ATTR_TYPE_NAME, null as ATTR_SIZE, null as DECIMAL_DIGITS, " +
					"null as NUM_PREC_RADIX, null as NULLABLE, null as REMARKS, null as ATTR_DEF, " +
					"null as SQL_DATA_TYPE, null as SQL_DATETIME_SUB, null as CHAR_OCTET_LENGTH, " +
					"null as ORDINAL_POSITION, null as IS_NULLABLE, null as SCOPE_CATALOG, " +
					"null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE limit 0;");
		}

		return getAttributes.executeQuery();
	}

	@Override
	public ResultSet getBestRowIdentifier(String catalog, String schema,
			String table, int scope, boolean nullable) throws SQLException {
		if (getBestRowIdentifier == null) {
			getBestRowIdentifier = con.prepareStatement("select null as SCOPE, null as COLUMN_NAME, " +
					"null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, " +
					"null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
		}

		return getBestRowIdentifier.executeQuery();
	}

	@Override
	public String getCatalogSeparator() throws SQLException {
		return ".";
	}

	@Override
	public String getCatalogTerm() {
		return "catalog";
	}

	@Override
	public ResultSet getCatalogs() throws SQLException {
		if (getCatalogs == null) {
			getCatalogs = con.prepareStatement("select null as TABLE_CAT limit 0;");
		}

		return getCatalogs.executeQuery();
	}

	@Override
	public ResultSet getColumnPrivileges(String c, String s, String t, String colPat) throws SQLException {
		if (getColumnPrivileges == null) {
			getColumnPrivileges = con.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, " +
					"null as TABLE_NAME, null as COLUMN_NAME, null as GRANTOR, null as GRANTEE, " +
					"null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
		}

		return getColumnPrivileges.executeQuery();
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
		return con;
	}

	@Override
	public ResultSet getCrossReference(String pc, String ps, String pt, String fc, String fs, String ft) throws SQLException {
		if (pt == null) {
			return getExportedKeys(fc, fs, ft);
		}

		if (ft == null) {
			return getImportedKeys(pc, ps, pt);
		}

		StringBuilder query = new StringBuilder();
		query.append("select ").append(quote(pc)).append(" as PKTABLE_CAT, ")
		.append(quote(ps)).append(" as PKTABLE_SCHEM, ").append(quote(pt)).append(" as PKTABLE_NAME, ")
		.append("'' as PKCOLUMN_NAME, ").append(quote(fc)).append(" as FKTABLE_CAT, ")
		.append(quote(fs)).append(" as FKTABLE_SCHEM, ").append(quote(ft)).append(" as FKTABLE_NAME, ")
		.append("'' as FKCOLUMN_NAME, -1 as KEY_SEQ, 3 as UPDATE_RULE, 3 as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, ")
		.append(Integer.toString(importedKeyInitiallyDeferred)).append(" as DEFERRABILITY limit 0 ");

		return con.createStatement().executeQuery(query.toString());
	}
	@Override
	public int getDatabaseMajorVersion() throws SQLException {
		return con.getDb().getSqliteDatabase().getVersion();
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException {
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
		return Connection.TRANSACTION_SERIALIZABLE;
	}

	@Override
	public int getDriverMajorVersion() {
		return 1;
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

	static {
		RULE_MAP.put("NO ACTION", importedKeyNoAction);
		RULE_MAP.put("CASCADE", importedKeyCascade);
		RULE_MAP.put("RESTRICT", importedKeyRestrict);
		RULE_MAP.put("SET NULL", importedKeySetNull);
		RULE_MAP.put("SET DEFAULT", importedKeySetDefault);
	}

	@Override
	public ResultSet getExportedKeys(String catalog, String schema, String table)
			throws SQLException {
		PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
		String[] pkColumns = pkFinder.getColumns();
		Statement stat = con.createStatement();

		catalog = (catalog != null) ? quote(catalog) : null;
		schema = (schema != null) ? quote(schema) : null;

		StringBuilder exportedKeysQuery = new StringBuilder(512);

		int count = 0;
		if (pkColumns != null) {
			// retrieve table list
			ResultSet rs = stat.executeQuery("select name from sqlite_master where type = 'table'");
			ArrayList<String> tableList = new ArrayList<String>();

			while (rs.next()) {
				tableList.add(rs.getString(1));
			}

			rs.close();

			ResultSet fk = null;
			String target = table.toLowerCase();
			// find imported keys for each table
			for (String tbl : tableList) {
				try {
					fk = stat.executeQuery("pragma foreign_key_list('" + escape(tbl) + "')");
				} catch (SQLException e) {
					if (e.getErrorCode() == SQLITE_DONE)
						continue; // expected if table has no foreign keys

					throw e;
				}

				Statement stat2 = null;
				try {
					stat2 = con.createStatement();

					while(fk.next()) {
						int keySeq = fk.getInt(2) + 1;
						String PKTabName = fk.getString(3).toLowerCase();

						if (PKTabName == null || !PKTabName.equals(target)) {
							continue;
						}

						String PKColName = fk.getString(5);
						PKColName = (PKColName == null) ? pkColumns[0] : PKColName.toLowerCase();

						exportedKeysQuery
						.append(count > 0 ? " union all select " : "select ")
						.append(Integer.toString(keySeq)).append(" as ks, lower('")
						.append(escape(tbl)).append("') as fkt, lower('")
						.append(escape(fk.getString(4))).append("') as fcn, '")
						.append(escape(PKColName)).append("' as pcn, ")
						.append(RULE_MAP.get(fk.getString(6))).append(" as ur, ")
						.append(RULE_MAP.get(fk.getString(7))).append(" as dr, ");

						rs = stat2.executeQuery("select sql from sqlite_master where" +
								" lower(name) = lower('" + escape(tbl) + "')");

						if (rs.next()) {
							Matcher matcher = FK_NAMED_PATTERN.matcher(rs.getString(1));

							if (matcher.find()){
								exportedKeysQuery.append("'").append(escape(matcher.group(1).toLowerCase())).append("' as fkn");
							}
							else {
								exportedKeysQuery.append("'' as fkn");
							}
						}

						rs.close();
						count++;
					}
				}
				finally {
					try{
						if (rs != null) rs.close();
					}catch(SQLException e) {}
					try{
						if (stat2 != null) stat2.close();
					}catch(SQLException e) {}
					try{
						if (fk != null) fk.close();
					}catch(SQLException e) {}
				}
			}
		}

		boolean hasImportedKey = (count > 0);
		StringBuilder sql = new StringBuilder(512);
		sql.append("select ")
		.append(catalog).append(" as PKTABLE_CAT, ")
		.append(schema).append(" as PKTABLE_SCHEM, ")
		.append(quote(table)).append(" as PKTABLE_NAME, ")
		.append(hasImportedKey ? "pcn" : "''").append(" as PKCOLUMN_NAME, ")
		.append(catalog).append(" as FKTABLE_CAT, ")
		.append(schema).append(" as FKTABLE_SCHEM, ")
		.append(hasImportedKey ? "fkt" : "''").append(" as FKTABLE_NAME, ")
		.append(hasImportedKey ? "fcn" : "''").append(" as FKCOLUMN_NAME, ")
		.append(hasImportedKey ? "ks" : "-1").append(" as KEY_SEQ, ")
		.append(hasImportedKey ? "ur" : "3").append(" as UPDATE_RULE, ")
		.append(hasImportedKey ? "dr" : "3").append(" as DELETE_RULE, ")
		.append(hasImportedKey ? "fkn" : "''").append(" as FK_NAME, ")
		.append(pkFinder.getName() != null ? pkFinder.getName() : "''").append(" as PK_NAME, ")
		.append(Integer.toString(importedKeyInitiallyDeferred)) // FIXME: Check for pragma foreign_keys = true ?
		.append(" as DEFERRABILITY ");

		if (hasImportedKey) {
			sql.append("from (").append(exportedKeysQuery).append(") order by fkt");
		}
		else {
			sql.append("limit 0");
		}

		return stat.executeQuery(sql.toString());
	}

	@Override
	public String getExtraNameCharacters() throws SQLException {
		return "";
	}

	@Override
	public String getIdentifierQuoteString() throws SQLException {
		return " ";
	}

	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table)
			throws SQLException {
		ResultSet rs = null;
		Statement stat = con.createStatement();
		StringBuilder sql = new StringBuilder(700);

		sql.append("select ").append(quote(catalog)).append(" as PKTABLE_CAT, ")
		.append(quote(schema)).append(" as PKTABLE_SCHEM, ")
		.append("ptn as PKTABLE_NAME, pcn as PKCOLUMN_NAME, ")
		.append(quote(catalog)).append(" as FKTABLE_CAT, ")
		.append(quote(schema)).append(" as FKTABLE_SCHEM, ")
		.append(quote(table)).append(" as FKTABLE_NAME, ")
		.append("fcn as FKCOLUMN_NAME, ks as KEY_SEQ, ur as UPDATE_RULE, dr as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, ")
		.append(Integer.toString(importedKeyInitiallyDeferred)).append(" as DEFERRABILITY from (");

		// Use a try catch block to avoid "query does not return ResultSet" error
		try {
			rs = stat.executeQuery("pragma foreign_key_list('" + escape(table) + "');");
		}
		catch (SQLException e) {
			sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ")
			.append(importedKeyNoAction).append(" as ur, ")
			.append(importedKeyNoAction).append(" as dr) limit 0;");

			return stat.executeQuery(sql.toString());
		}

		boolean rsHasNext = false;
		
		for (int i = 0; rs.next(); i++) {
			rsHasNext = true;
			int keySeq = rs.getInt(2) + 1;
			String PKTabName = rs.getString(3);
			String FKColName = rs.getString(4);
			String PKColName = rs.getString(5);

			if (PKColName == null) {
				PKColName = new PrimaryKeyFinder(PKTabName).getColumns()[0];
			}

			String updateRule = rs.getString(6);
			String deleteRule = rs.getString(7);

			if (i > 0) {
				sql.append(" union all ");
			}

			sql.append("select ").append(keySeq).append(" as ks,")
			.append("'").append(escape(PKTabName)).append("' as ptn, '")
			.append(escape(FKColName)).append("' as fcn, '")
			.append(escape(PKColName)).append("' as pcn,")
			.append("case '").append(escape(updateRule)).append("'")
			.append(" when 'NO ACTION' then ").append(importedKeyNoAction)
			.append(" when 'CASCADE' then ").append(importedKeyCascade)
			.append(" when 'RESTRICT' then ").append(importedKeyRestrict)
			.append(" when 'SET NULL' then ").append(importedKeySetNull)
			.append(" when 'SET DEFAULT' then ").append(importedKeySetDefault).append(" end as ur, ")
			.append("case '").append(escape(deleteRule)).append("'")
			.append(" when 'NO ACTION' then ").append(importedKeyNoAction)
			.append(" when 'CASCADE' then ").append(importedKeyCascade)
			.append(" when 'RESTRICT' then ").append(importedKeyRestrict)
			.append(" when 'SET NULL' then ").append(importedKeySetNull)
			.append(" when 'SET DEFAULT' then ").append(importedKeySetDefault).append(" end as dr");
		}
		rs.close();
		
		if(!rsHasNext){
			sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ")
			.append(importedKeyNoAction).append(" as ur, ")
			.append(importedKeyNoAction).append(" as dr) limit 0;");
		}

		return stat.executeQuery(sql.append(");").toString());
	}

	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table,
			boolean unique, boolean approximate) throws SQLException {
		ResultSet rs = null;
		Statement stat = con.createStatement();
		StringBuilder sql = new StringBuilder(500);

		sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '")
		.append(escape(table)).append("' as TABLE_NAME, un as NON_UNIQUE, null as INDEX_QUALIFIER, n as INDEX_NAME, ")
		.append(Integer.toString(tableIndexOther)).append(" as TYPE, op as ORDINAL_POSITION, ")
		.append("cn as COLUMN_NAME, null as ASC_OR_DESC, 0 as CARDINALITY, 0 as PAGES, null as FILTER_CONDITION from (");

		// Use a try catch block to avoid "query does not return ResultSet" error
		try {
			rs = stat.executeQuery("pragma index_list('" + escape(table) + "');");
		}
		catch (SQLException e) {
			sql.append("select null as un, null as n, null as op, null as cn) limit 0;");

			return stat.executeQuery(sql.toString());
		}

		ArrayList<ArrayList<Object>> indexList = new ArrayList<ArrayList<Object>>();
		while (rs.next()) {
			indexList.add(new ArrayList<Object>());
			indexList.get(indexList.size() - 1).add(rs.getString(2));
			indexList.get(indexList.size() - 1).add(rs.getInt(3));
		}
		rs.close();

		int i = 0;
		Iterator<ArrayList<Object>> indexIterator = indexList.iterator();
		ArrayList<Object> currentIndex;

		while (indexIterator.hasNext()) {
			currentIndex = indexIterator.next();
			String indexName = currentIndex.get(0).toString();
			rs = stat.executeQuery("pragma index_info('" + escape(indexName) + "');");

			while(rs.next()) {
				if (i++ > 0) {
					sql.append(" union all ");
				}

				sql.append("select ").append(Integer.toString(1 - (Integer)currentIndex.get(1))).append(" as un,'")
				.append(escape(indexName)).append("' as n,")
				.append(Integer.toString(rs.getInt(1) + 1)).append(" as op,'")
				.append(escape(rs.getString(3))).append("' as cn");
			}

			rs.close();
		}

		return stat.executeQuery(sql.append(");").toString());
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException {
		return 2;
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException {
		return 1;
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		return 0;
	}

	@Override
	public int getMaxColumnsInSelect() {
		return 0;
	}

	@Override
	public int getMaxColumnsInTable() {
		return 0;
	}

	@Override
	public int getMaxConnections() {
		return 0;
	}

	@Override
	public int getMaxCursorNameLength() {
		return 0;
	}

	@Override
	public int getMaxIndexLength() {
		return 0;
	}

	@Override
	public int getMaxProcedureNameLength() {
		return 0;
	}

	@Override
	public int getMaxRowSize() {
		return 0;
	}

	@Override
	public int getMaxSchemaNameLength() {
		return 0;
	}

	@Override
	public int getMaxStatementLength() {
		return 0;
	}

	@Override
	public int getMaxStatements() {
		return 0;
	}

	@Override
	public int getMaxTableNameLength() {
		return 0;
	}

	@Override
	public int getMaxTablesInSelect() {
		return 0;
	}

	@Override
	public int getMaxUserNameLength() {
		return 0;
	}

	@Override
	public String getNumericFunctions() throws SQLException {
		return "";
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
	public ResultSet getProcedureColumns(String c, String s, String p, String colPat) throws SQLException {
		if (getProcedures == null) {
			getProcedureColumns = con.prepareStatement("select null as PROCEDURE_CAT, " +
					"null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as COLUMN_NAME, " +
					"null as COLUMN_TYPE, null as DATA_TYPE, null as TYPE_NAME, null as PRECISION, " +
					"null as LENGTH, null as SCALE, null as RADIX, null as NULLABLE, " +
					"null as REMARKS limit 0;");
		}
		return getProcedureColumns.executeQuery();

	}

	@Override
	public String getProcedureTerm() throws SQLException {
		return "not_implemented";
	}

	@Override
	public ResultSet getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		if (getProcedures == null) {
			getProcedures = con.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, " +
					"null as PROCEDURE_NAME, null as UNDEF1, null as UNDEF2, null as UNDEF3, " +
					"null as REMARKS, null as PROCEDURE_TYPE limit 0;");
		}
		return getProcedures.executeQuery();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public String getSQLKeywords() throws SQLException {
		return "";
	}

	@Override
	public int getSQLStateType() throws SQLException {
		return sqlStateSQL99;
	}

	@Override
	public String getSchemaTerm() throws SQLException {
		return "schema";
	}

	@Override
	public ResultSet getSchemas() throws SQLException {
	  throw new UnsupportedOperationException("getSchemas not supported by SQLite");
	}

	@Override
	public String getSearchStringEscape() throws SQLException {
		return null;
	}

	@Override
	public String getStringFunctions() throws SQLException {
		return "";
	}

	@Override
	public ResultSet getSuperTables(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		if (getSuperTypes == null) {
			getSuperTypes = con.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, " +
					"null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, " +
					"null as SUPERTYPE_NAME limit 0;");
		}
		return getSuperTypes.executeQuery();
	}

	@Override
	public ResultSet getSuperTypes(String catalog, String schemaPattern,
			String typeNamePattern) throws SQLException {
		if (getSuperTypes == null) {
			getSuperTypes = con.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, " +
					"null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, " +
					"null as SUPERTYPE_NAME limit 0;");
		}
		return getSuperTypes.executeQuery();
	}

	@Override
	public String getSystemFunctions() throws SQLException {
		return "";
	}

	@Override
	public ResultSet getTablePrivileges(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		if (getTablePrivileges == null) {
			getTablePrivileges = con.prepareStatement("select  null as TABLE_CAT, "
					+ "null as TABLE_SCHEM, null as TABLE_NAME, null as GRANTOR, null "
					+ "GRANTEE,  null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
		}
		return getTablePrivileges.executeQuery();
	}

	@Override
	public ResultSet getTableTypes() throws SQLException {
		checkOpen();
		if (getTableTypes == null) {
			getTableTypes = con.prepareStatement("select 'TABLE' as TABLE_TYPE "
					+ "union select 'VIEW' as TABLE_TYPE;");
		}
		getTableTypes.clearParameters();
		return getTableTypes.executeQuery();
	}

	@Override
	public ResultSet getTables(String catalog, String schemaPattern,
			String tableNamePattern, String[] types) throws SQLException {

		if(tableNamePattern == null){
			tableNamePattern = "%";
		}
		
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
		return "";
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
		if (getUDTs == null) {
			getUDTs = con.prepareStatement("select  null as TYPE_CAT, null as TYPE_SCHEM, "
					+ "null as TYPE_NAME,  null as CLASS_NAME,  null as DATA_TYPE, null as REMARKS, "
					+ "null as BASE_TYPE " + "limit 0;");
		}

		getUDTs.clearParameters();
		return getUDTs.executeQuery();
	}

	@Override
	public String getURL() throws SQLException {
		return con.url();
	}

	@Override
	public String getUserName() throws SQLException {
		return null;
	}

	@Override
	public ResultSet getVersionColumns(String catalog, String schema,
			String table) throws SQLException {
		if (getVersionColumns == null) {
			getVersionColumns = con.prepareStatement("select null as SCOPE, null as COLUMN_NAME, "
					+ "null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, "
					+ "null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
		}
		return getVersionColumns.executeQuery();
	}

	@Override
	public boolean insertsAreDetected(int type) throws SQLException {
		return false;
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException {
		return true;
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return con.isReadOnly();
	}

	@Override
	public boolean locatorsUpdateCopy() throws SQLException {
		return false;
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		return true;
	}

	@Override
	public boolean nullsAreSortedAtEnd() {
		return !nullsAreSortedAtStart();
	}

	@Override
	public boolean nullsAreSortedAtStart() {
		return true;
	}

	@Override
	public boolean nullsAreSortedHigh() {
		return true;
	}

	@Override
	public boolean nullsAreSortedLow() {
		return !nullsAreSortedHigh();
	}

	@Override
	public boolean othersDeletesAreVisible(int type) {
		return false;
	}

	@Override
	public boolean othersInsertsAreVisible(int type) {
		return false;
	}

	@Override
	public boolean othersUpdatesAreVisible(int type) {
		return false;
	}

	@Override
	public boolean ownDeletesAreVisible(int type) {
		return false;
	}

	@Override
	public boolean ownInsertsAreVisible(int type) {
		return false;
	}

	@Override
	public boolean ownUpdatesAreVisible(int type) {
		return false;
	}

	@Override
	public boolean storesLowerCaseIdentifiers() {
		return false;
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() {
		return false;
	}

	@Override
	public boolean storesMixedCaseIdentifiers() {
		return true;
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() {
		return false;
	}

	@Override
	public boolean storesUpperCaseIdentifiers() {
		return false;
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() {
		return false;
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() {
		return false;
	}

	@Override
	public boolean supportsANSI92FullSQL() {
		return false;
	}
	
	@Override
	public boolean supportsANSI92IntermediateSQL() {
		return false;
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() {
		return false;
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() {
		return false;
	}

	@Override
	public boolean supportsBatchUpdates() {
		return true;
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() {
		return false;
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() {
		return false;
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() {
		return false;
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() {
		return false;
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() {
		return false;
	}

	@Override
	public boolean supportsColumnAliasing() {
		return true;
	}

	@Override
	public boolean supportsConvert() {
		return false;
	}

	@Override
	public boolean supportsConvert(int fromType, int toType) {
		return false;
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		return true;
	}

	@Override
	public boolean supportsCorrelatedSubqueries() {
		return false;
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() {
		return true;
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() {
		return false;
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() {
		return false;
	}

	@Override
	public boolean supportsExpressionsInOrderBy() {
		return true;
	}

	@Override
	public boolean supportsExtendedSQLGrammar() {
		return false;
	}

	@Override
	public boolean supportsFullOuterJoins() {
		return false;
	}

	@Override
	public boolean supportsGetGeneratedKeys(){
		return true;
	}

	@Override
	public boolean supportsGroupBy() {
		return true;
	}

	@Override
	public boolean supportsGroupByBeyondSelect() {
		return false;
	}

	@Override
	public boolean supportsGroupByUnrelated() {
		return false;
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() {
		return false;
	}

	@Override
	public boolean supportsLikeEscapeClause() {
		return false;
	}

	@Override
	public boolean supportsLimitedOuterJoins() {
		return true;
	}

	@Override
	public boolean supportsMinimumSQLGrammar() {
		return true;
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() {
		return true;
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() {
		return false;
	}

	@Override
	public boolean supportsMultipleOpenResults() {
		return false;
	}

	@Override
	public boolean supportsMultipleResultSets() {
		return false;
	}

	@Override
	public boolean supportsMultipleTransactions() {
		return true;
	}

	@Override
	public boolean supportsNamedParameters() {
		return true;
	}

	@Override
	public boolean supportsNonNullableColumns() {
		return true;
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() {
		return false;
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() {
		return false;
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() {
		return false;
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() {
		return false;
	}

	@Override
	public boolean supportsOrderByUnrelated() {
		return false;
	}

	@Override
	public boolean supportsOuterJoins() {
		return true;
	}

	@Override
	public boolean supportsPositionedDelete() {
		return false;
	}

	@Override
	public boolean supportsPositionedUpdate() {
		return false;
	}

	@Override
	public boolean supportsResultSetConcurrency(int t, int c) {
		return t == ResultSet.TYPE_FORWARD_ONLY && c == ResultSet.CONCUR_READ_ONLY;
	}

	@Override
	public boolean supportsResultSetHoldability(int h) {
		return h == ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public boolean supportsResultSetType(int t) {
		return t == ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public boolean supportsSavepoints() {
		return false;
	}

	@Override
	public boolean supportsSchemasInDataManipulation() {
		return false;
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() {
		return false;
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() {
		return false;
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() {
		return false;
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() {
		return false;
	}

	@Override
	public boolean supportsSelectForUpdate() {
		return false;
	}

	@Override
	public boolean supportsStatementPooling() {
		return false;
	}

	@Override
	public boolean supportsStoredProcedures() {
		return false;
	}

	@Override
	public boolean supportsSubqueriesInComparisons() {
		return false;
	}

	@Override
	public boolean supportsSubqueriesInExists() {
		return true;
	} // TODO: check

	@Override
	public boolean supportsSubqueriesInIns() {
		return true;
	} // TODO: check

	@Override
	public boolean supportsSubqueriesInQuantifieds() {
		return false;
	}

	@Override
	public boolean supportsTableCorrelationNames() {
		return false;
	}
	
	@Override
	public boolean supportsTransactionIsolationLevel(int level) {
		return level == Connection.TRANSACTION_SERIALIZABLE;
	}

	@Override
	public boolean supportsTransactions() {
		return true;
	}

	@Override
	public boolean supportsUnion() {
		return true;
	}

	@Override
	public boolean supportsUnionAll() {
		return true;
	}

	@Override
	public boolean updatesAreDetected(int type) {
		return false;
	}

	@Override
	public boolean usesLocalFilePerTable() {
		return false;
	}

	@Override
	public boolean usesLocalFiles() {
		return true;
	}
	
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface != null && iface.isAssignableFrom(getClass());
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (isWrapperFor(iface)) {
      return (T) this;
    }
    throw new SQLException(getClass() + " does not wrap " + iface);
  }

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
	  // TODO Evaluate if this is a sufficient implementation (if so, remove this comment)
	  return false;
	}

	@Override
	public ResultSet getClientInfoProperties() throws SQLException {
	  // TODO Evaluate if this is a sufficient implementation (if so, remove this comment)
		return null;
	}

	@Override
	public ResultSet getFunctionColumns(String catalog, String schemaPattern,
			String functionNamePattern, String columnNamePattern)
			throws SQLException {
	  throw new UnsupportedOperationException("getFunctionColumns not supported");
	}

	@Override
	public ResultSet getFunctions(String catalog, String schemaPattern,
			String functionNamePattern) throws SQLException {
	  throw new UnsupportedOperationException("getFunctions not implemented yet");
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException {
		return RowIdLifetime.ROWID_UNSUPPORTED;
	}

	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
	  throw new UnsupportedOperationException("getSchemas not implemented yet");
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
	  // TODO Evaluate if this is a sufficient implementation (if so, remove this comment)
		return false;
	}
	
  // methods added for JDK7 compilation

  public boolean generatedKeyAlwaysReturned() throws SQLException {
      throw new UnsupportedOperationException("generatedKeyAlwaysReturned not implemented yet");
  }

  public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
      throw new UnsupportedOperationException("getPseudoColumns not implemented yet");
  }
  
  /**
	 * Adds SQL string quotes to the given string.
	 * @param tableName The string to quote.
	 * @return The quoted string.
	 */
	private static String quote(String tableName) {
		if (tableName == null) {
			return "null";
		}
		else {
			return String.format("'%s'", tableName);
		}
	}
	
	/**
	 * Applies SQL escapes for special characters in a given string.
	 * @param val The string to escape.
	 * @return The SQL escaped string.
	 */
	private String escape(final String val) {
		// TODO: this function is ugly, pass this work off to SQLite, then we
		//       don't have to worry about Unicode 4, other characters needing
		//       escaping, etc.
		int len = val.length();
		StringBuilder buf = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			if (val.charAt(i) == '\'') {
				buf.append('\'');
			}
			buf.append(val.charAt(i));
		}
		return buf.toString();
	}
	
	/**
	 * @throws SQLException
	 */
	private void checkOpen() throws SQLException {
		if (con == null) {
			throw new SQLException("connection closed");
		}
	}
	
	/**
	 * Parses the sqlite_master table for a table's primary key
	 */
	class PrimaryKeyFinder {
		/** The table name. */
		String table;

		/** The primary key name. */
		String pkName = null;

		/** The column(s) for the primary key. */
		String pkColumns[] = null;

		/**
		 * Constructor.
		 * @param table The table for which to get find a primary key.
		 * @throws SQLException
		 */
		public PrimaryKeyFinder(String table) throws SQLException {
			this.table = table;

			if (table == null || table.trim().length() == 0) {
				throw new SQLException("Invalid table name: '" + this.table + "'");
			}

			Statement stat = null;
			ResultSet rs = null;

			try {
				stat = con.createStatement();
				// read create SQL script for table
				rs = stat.executeQuery("select sql from sqlite_master where" +
						" lower(name) = lower('" + escape(table) + "') and type = 'table'");

				if (!rs.next())
					throw new SQLException("Table not found: '" + table + "'");

				Matcher matcher = PK_NAMED_PATTERN.matcher(rs.getString(1));
				if (matcher.find()){
					pkName = '\'' + escape(matcher.group(1).toLowerCase()) + '\'';
					pkColumns = matcher.group(2).split(",");
				}
				else {
					matcher = PK_UNNAMED_PATTERN.matcher(rs.getString(1));
					if (matcher.find()){
						pkColumns = matcher.group(1).split(",");
					}
				}

				if (pkColumns == null) {
					rs = stat.executeQuery("pragma table_info('" + escape(table) + "');");
					while(rs.next()) {
						if (rs.getBoolean(6))
							pkColumns = new String[]{rs.getString(2)};
					}
				}

				if (pkColumns != null)
					for (int i = 0; i < pkColumns.length; i++) {
						pkColumns[i] = pkColumns[i].toLowerCase().trim();
					}
			}
			finally {
				try {
					if (rs != null) rs.close();
				} catch (Exception e) {}
				try {
					if (stat != null) stat.close();
				} catch (Exception e) {}
			}
		}

		/**
		 * @return The primary key name if any.
		 */
		public String getName() {
			return pkName;
		}

		/**
		 * @return Array of primary key column(s) if any.
		 */
		public String[] getColumns() {
			return pkColumns;
		}
	}


}
