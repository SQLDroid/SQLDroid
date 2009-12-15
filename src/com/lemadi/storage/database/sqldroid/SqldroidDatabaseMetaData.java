package com.lemadi.storage.database.sqldroid;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqldroidDatabaseMetaData implements DatabaseMetaData {


	SqldroidConnection con;
	
	public SqldroidDatabaseMetaData(SqldroidConnection con) {
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
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
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
		return con.sqlitedb.getVersion();
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
		
		return "Lemadi's Sqldroid";
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
	public ResultSet getPrimaryKeys(String catalog, String schema, String table)
			throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
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
		
		// TODO table is here :)
		
		PreparedStatement ps = con.prepareStatement("SELECT tbl_name FROM sqlite_master WHERE tbl_name = ?");
		ps.setString(1, tableNamePattern);
		ResultSet rs = ps.executeQuery();
		
		return rs;
	}

	@Override
	public String getTimeDateFunctions() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public ResultSet getTypeInfo() throws SQLException {
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return null;
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
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
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
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
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
		System.err.println(" ********************* not implemented @ "
				+ DebugPrinter.getFileName() + " line "
				+ DebugPrinter.getLineNumber());
		return false;
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

}
