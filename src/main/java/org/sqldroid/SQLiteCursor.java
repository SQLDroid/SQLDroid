package org.sqldroid;

import java.sql.SQLException;

public interface SQLiteCursor {

    String getString(int columnIndex);

    byte[] getBlob(int columnIndex);


    short getShort(int columnIndex);

    int getInt(int columnIndex);

    long getLong(int columnIndex);

    float getFloat(int columnIndex);

    double getDouble(int columnIndex);

    boolean isNull(int columnIndex);


    boolean moveToPosition(int pos);

    boolean moveToFirst();

    boolean moveToNext();

    boolean moveToPrevious();

    boolean moveToLast();


    void close();

    boolean isClosed();

    int getCount();

    boolean isLast();

    boolean isFirst();

    boolean isBeforeFirst();

    boolean isAfterLast();


    int getPosition();

    boolean requery();

    int getColumnCount();

    int getColumnIndexOrThrow(String columnName) throws SQLException;

    String getColumnName(int i);


    int getType(int column);


}
