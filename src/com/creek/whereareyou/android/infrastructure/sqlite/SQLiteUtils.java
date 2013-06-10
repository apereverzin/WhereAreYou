package com.creek.whereareyou.android.infrastructure.sqlite;

import android.database.Cursor;

/**
 * 
 * @author Andrey Pereverzin
 */
public class SQLiteUtils {
    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
