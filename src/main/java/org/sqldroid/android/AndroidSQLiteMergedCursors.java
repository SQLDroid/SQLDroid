package org.sqldroid.android;

import org.sqldroid.SQLiteCursor;

import java.lang.reflect.Method;
import java.sql.Types;

import android.database.Cursor;
import android.database.MergeCursor;

public class AndroidSQLiteMergedCursors implements SQLiteCursor {

    private MergeCursor cursor;

    public AndroidSQLiteMergedCursors(SQLiteCursor[] sqlCursors) {
        Cursor[] cursors = new Cursor[sqlCursors.length];
        for (int i = 0; i < cursors.length; i++) {
            cursors[i] = ((AndroidCursor)sqlCursors[i]).getCursor();
        }
        this.cursor = new MergeCursor(cursors);
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public boolean equals(Object o) {
        return cursor.equals(o);
    }

    @Override
    public byte[] getBlob(int column) {
        return cursor.getBlob(column);
    }

    @Override
    public int getColumnCount() {
        return cursor.getColumnCount();
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) {
        return cursor.getColumnIndexOrThrow(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return cursor.getColumnName(columnIndex);
    }

    private static Method getType;
    static {
        try {
            getType = Cursor.class.getMethod("getType", new Class[] {int.class});
        } catch (NoSuchMethodException e) {
            getType = null;
        }
    }

    @Override
    public int getType(int column) {
        if (getType != null) {
            try {
                return (Integer) getType.invoke(cursor, column);
            } catch (Exception e) {}
        }
        return Types.OTHER; // return something that can be used to understand that the type is unknown.
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public double getDouble(int column) {
        return cursor.getDouble(column);
    }


    @Override
    public float getFloat(int column) {
        return cursor.getFloat(column);
    }

    @Override
    public int getInt(int column) {
        return cursor.getInt(column);
    }

    @Override
    public long getLong(int column) {
        return cursor.getLong(column);
    }

    @Override
    public final int getPosition() {
        return cursor.getPosition();
    }

    @Override
    public short getShort(int column) {
        return cursor.getShort(column);
    }

    @Override
    public String getString(int column) {
        return cursor.getString(column);
    }


    @Override
    public int hashCode() {
        return cursor.hashCode();
    }

    @Override
    public final boolean isAfterLast() {
        return cursor.isAfterLast();
    }

    @Override
    public final boolean isBeforeFirst() {
        return cursor.isBeforeFirst();
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }

    @Override
    public final boolean isFirst() {
        return cursor.isFirst();
    }

    @Override
    public final boolean isLast() {
        return cursor.isLast();
    }

    @Override
    public boolean isNull(int column) {
        return cursor.isNull(column);
    }

    @Override
    public final boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    @Override
    public final boolean moveToLast() {
        return cursor.moveToLast();
    }

    @Override
    public final boolean moveToNext() {
        return cursor.moveToNext();
    }

    @Override
    public final boolean moveToPosition(int position) {
        return cursor.moveToPosition(position);
    }

    @Override
    public final boolean moveToPrevious() {
        return cursor.moveToPrevious();
    }

    @Override
    public boolean requery() {
        return cursor.requery();
    }

    @Override
    public String toString() {
        return cursor.toString();
    }

}
