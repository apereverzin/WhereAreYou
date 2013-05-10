package com.creek.whereareyou.android.infrastructure.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author andreypereverzin
 */
public abstract class AbstractSQLiteRepository {
    protected final SQLiteDatabase whereAreYouDb;
    static final String ID_FIELD_NAME = "_id";    
    static final String ID_FIELD_EQUALS = ID_FIELD_NAME + "=";
    static final String CONTACT_DATA_TABLE = "contact_data";
    static final String CONTACT_LOCATION_DATA_TABLE = "contact_location";
    
    public AbstractSQLiteRepository(SQLiteDatabase whereAreYouDb) {
        this.whereAreYouDb = whereAreYouDb;
    }

    protected Cursor createCursor(String selection, String[] selectionArgs, String orderBy) {
        Log.d(getClass().getName(), "createCursor()");
        return whereAreYouDb.query(getTableName(), getFieldNames(), selection, selectionArgs, null, null, orderBy);
    }
    
    protected boolean delete(int id) {
        return whereAreYouDb.delete(getTableName(), ID_FIELD_EQUALS + id, null) > 0;
    }
    
    protected abstract String getTableName();
    
    protected abstract String[] getFieldNames();
}
