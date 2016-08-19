package org.sqldroid.android;

import org.sqldroid.SQLiteMatrixCursor;

import android.database.MatrixCursor;

public class AndroidSQLiteMatrixCursor extends AndroidCursor implements SQLiteMatrixCursor {

    public AndroidSQLiteMatrixCursor(String[] columnNames, int count) {
        super(new MatrixCursor(columnNames, count));
    }

    public void addRow(Iterable<?> columnValues) {
        ((MatrixCursor)cursor).addRow(columnValues);
    }

    @Override
    public void addRow(Object[] columnValues) {
        ((MatrixCursor)cursor).addRow(columnValues);
    }
}
