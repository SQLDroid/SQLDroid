package org.sqldroid;

public interface SQLiteMatrixCursor extends SQLiteCursor {

    void addRow(Object[] column);

}
