package org.sqldroid.android;

import org.sqldroid.SQLiteCursor;

import java.lang.reflect.Method;
import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class AndroidCursor implements SQLiteCursor {

    protected Cursor cursor;

    public AndroidCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public void close() {
        cursor.close();
    }

    public void copyStringToBuffer(int arg0, CharArrayBuffer arg1) {
        cursor.copyStringToBuffer(arg0, arg1);
    }

    public void deactivate() {
        cursor.deactivate();
    }

    @Override
    public byte[] getBlob(int arg0) {
        return cursor.getBlob(arg0);
    }

    @Override
    public int getColumnCount() {
        return cursor.getColumnCount();
    }

    public int getColumnIndex(String arg0) {
        return cursor.getColumnIndex(arg0);
    }

    @Override
    public int getColumnIndexOrThrow(String arg0) throws IllegalArgumentException {
        return cursor.getColumnIndexOrThrow(arg0);
    }

    @Override
    public String getColumnName(int arg0) {
        return cursor.getColumnName(arg0);
    }

    public String[] getColumnNames() {
        return cursor.getColumnNames();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public double getDouble(int arg0) {
        return cursor.getDouble(arg0);
    }

    public Bundle getExtras() {
        return cursor.getExtras();
    }

    @Override
    public float getFloat(int arg0) {
        return cursor.getFloat(arg0);
    }

    @Override
    public int getInt(int arg0) {
        return cursor.getInt(arg0);
    }

    @Override
    public long getLong(int arg0) {
        return cursor.getLong(arg0);
    }

    public Uri getNotificationUri() {
        return cursor.getNotificationUri();
    }

    @Override
    public int getPosition() {
        return cursor.getPosition();
    }

    @Override
    public short getShort(int arg0) {
        return cursor.getShort(arg0);
    }

    @Override
    public String getString(int arg0) {
        return cursor.getString(arg0);
    }

    /*
    static int getType(SQLiteCursor cursor, int column) {
        if (getType != null) {
            try {
                return (Integer) getType.invoke(cursor, column);
            } catch (Exception e) {}
        }
        return Types.OTHER; // return something that can be used to understand that the type is unknown.
    }
    */

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
        return cursor.getType(column);
//        if (getType != null) {
//            try {
//                return (Integer) getType.invoke(cursor, column);
//            } catch (Exception e) {}
//        }
//        return Types.OTHER; // return something that can be used to understand that the type is unknown.
    }

    public boolean getWantsAllOnMoveCalls() {
        return cursor.getWantsAllOnMoveCalls();
    }

    @Override
    public boolean isAfterLast() {
        return cursor.isAfterLast();
    }

    @Override
    public boolean isBeforeFirst() {
        return cursor.isBeforeFirst();
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }

    @Override
    public boolean isFirst() {
        return cursor.isFirst();
    }

    @Override
    public boolean isLast() {
        return cursor.isLast();
    }

    @Override
    public boolean isNull(int arg0) {
        return cursor.isNull(arg0);
    }

    public boolean move(int arg0) {
        return cursor.move(arg0);
    }

    @Override
    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    @Override
    public boolean moveToLast() {
        return cursor.moveToLast();
    }

    @Override
    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    @Override
    public boolean moveToPosition(int arg0) {
        return cursor.moveToPosition(arg0);
    }

    @Override
    public boolean moveToPrevious() {
        return cursor.moveToPrevious();
    }

    public void registerContentObserver(ContentObserver arg0) {
        cursor.registerContentObserver(arg0);
    }

    public void registerDataSetObserver(DataSetObserver arg0) {
        cursor.registerDataSetObserver(arg0);
    }

    @Override
    public boolean requery() {
        return cursor.requery();
    }

    public Bundle respond(Bundle arg0) {
        return cursor.respond(arg0);
    }

    public void setNotificationUri(ContentResolver arg0, Uri arg1) {
        cursor.setNotificationUri(arg0, arg1);
    }

    public void unregisterContentObserver(ContentObserver arg0) {
        cursor.unregisterContentObserver(arg0);
    }

    public void unregisterDataSetObserver(DataSetObserver arg0) {
        cursor.unregisterDataSetObserver(arg0);
    }

    Cursor getCursor() {
        return cursor;
    }

}
