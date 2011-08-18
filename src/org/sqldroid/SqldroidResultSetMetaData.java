package org.sqldroid;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import android.database.Cursor;

public class SqldroidResultSetMetaData implements ResultSetMetaData {
    private final Cursor cursor;

    public SqldroidResultSetMetaData(Cursor cursor) {
        if (cursor == null) {
            throw new RuntimeException("Cursor required to be not null.");
        }
        this.cursor = cursor;
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public int getColumnCount() throws SQLException {
        int columnCount = cursor.getColumnCount();
        return columnCount;
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return 0;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return cursor.getColumnName(column - 1);
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return cursor.getColumnName(column - 1);
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        int oldPos = cursor.getPosition();
        boolean moved = false;
        if (cursor.isBeforeFirst() || cursor.isAfterLast()) {
            boolean resultSetEmpty = cursor.getCount() == 0 || cursor.isAfterLast();
            if (resultSetEmpty) {
                return Types.NULL;
            }
            cursor.moveToFirst();
            moved = true;
        }
        int nativeType = cursor.getType(column - 1);
        int type;
        switch (nativeType) {
        case Cursor.FIELD_TYPE_NULL:
            type = Types.NULL;
            break;
        case Cursor.FIELD_TYPE_INTEGER:
            type = Types.INTEGER;
            break;
        case Cursor.FIELD_TYPE_FLOAT:
            type = Types.FLOAT;
            break;
        case Cursor.FIELD_TYPE_STRING:
            type = Types.VARCHAR;
            break;
        case Cursor.FIELD_TYPE_BLOB:
            type = Types.BLOB;
            break;
        default:
            type = Types.NULL;
            break;
        }
        if (moved) {
            cursor.moveToPosition(oldPos);
        }
        return type;
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return 0;
    }

    @Override
    public int getScale(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return 0;
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public String getTableName(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return 0;
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

}
