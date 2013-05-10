package com.creek.whereareyou.android.infrastructure.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 * 
 */
public class SQLiteDbManager {
    private static final SQLiteDbManager instance = new SQLiteDbManager();
    private boolean initialized = false;
    private SQLiteDatabase whereAreYouDb;

    private static final String DATABASE_NAME = "whereareyou";
    private static final String DROP_TABLE = "drop table if exists ";

    private SQLiteDbManager() {
        //
    }

    public static SQLiteDbManager getInstance() {
        return instance;
    }

    public void initialise(Context ctx) {
        if (!initialized) {
            whereAreYouDb = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (!databaseExists()) {
                createDatabase();
            }
            
            initialized = true;
        }
    }

    public void close() {
        whereAreYouDb.close();
    }

    public SQLiteDatabase getDatabase() {
        return whereAreYouDb;
    }

    private void createDatabase() {
        whereAreYouDb.execSQL(DROP_TABLE + AbstractSQLiteRepository.CONTACT_DATA_TABLE);
        whereAreYouDb.execSQL(DROP_TABLE + AbstractSQLiteRepository.CONTACT_LOCATION_DATA_TABLE);
        whereAreYouDb.execSQL(AbstractSQLiteRepository.CONTACT_DATA_TABLE);
        whereAreYouDb.execSQL(AbstractSQLiteRepository.CONTACT_LOCATION_DATA_TABLE);
    }

    private boolean databaseExists() {
        try {
            whereAreYouDb.query(AbstractSQLiteRepository.CONTACT_DATA_TABLE, 
                    new String[] { AbstractSQLiteRepository.ID_FIELD_NAME }, null, null, null, null, null);
            whereAreYouDb.query(AbstractSQLiteRepository.CONTACT_LOCATION_DATA_TABLE, 
                    new String[] { AbstractSQLiteRepository.ID_FIELD_NAME }, null, null, null, null, null);
            Log.d(getClass().getName(), "db exists");
            return true;
        } catch (SQLiteException ex) {
            Log.d(getClass().getName(), "db does not exist");
            return false;
        }
    }
}
