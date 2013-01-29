package org.sqldroid;

import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import android.database.Cursor;

public class SQLDroidResultSetMetaData implements ResultSetMetaData {

	private final Cursor cursor;
    private static Method getType;
    static {
        try {
            getType = Cursor.class.getMethod("getType", new Class[] {int.class});
        } catch (Exception e) {
            getType = null;
        }
    }

    public SQLDroidResultSetMetaData(Cursor cursor) {
      if (cursor == null) {
          throw new NullPointerException("Cursor required to be not null.");
      }		
      this.cursor = cursor;
    }

    static int getType(Cursor cursor, int column) {
        if (getType != null) {
            try {
                return (Integer) getType.invoke(cursor, column);
            } catch (Exception e) {}
        }
        return Types.OTHER; // return something that can be used to understand that the type is unknown.
    }

	@Override
	public String getCatalogName(int column) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getColumnCount() throws SQLException {
		int columnCount = cursor.getColumnCount();
		return columnCount;
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
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
        int nativeType = getType(cursor, column - 1);
        int type;
        switch (nativeType) {
        case 0: // Cursor.FIELD_TYPE_NULL:
            type = Types.NULL;
            break;
        case 1: // Cursor.FIELD_TYPE_INTEGER:
            type = Types.INTEGER;
            break;
        case 2: // Cursor.FIELD_TYPE_FLOAT:
            type = Types.FLOAT;
            break;
        case 3: // Cursor.FIELD_TYPE_STRING:
            type = Types.VARCHAR;
            break;
        case 4: // Cursor.FIELD_TYPE_BLOB:
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
