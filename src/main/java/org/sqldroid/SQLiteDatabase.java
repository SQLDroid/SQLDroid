package org.sqldroid;

import java.sql.SQLException;

public interface SQLiteDatabase {

    void close() throws SQLException;

    String getDatabaseName();

    void setTransactionSuccessful() throws SQLException;

    void endTransaction() throws SQLException;

    void beginTransaction() throws SQLException;

    boolean isClosed();

    int getVersion();

    boolean inTransaction();

    SQLiteCursor rawQuery(String pragmaStatement, String[] strings) throws SQLException;

    SQLiteMatrixCursor createCursor(String[] columnNames, int count);

    SQLiteCursor mergeCursors(SQLiteCursor[] cursors);

    void execSQL(String string, Object[] makeArgListQueryObject) throws SQLException;

    void execSQL(String sql) throws SQLException;

}
