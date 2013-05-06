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
    private static SQLiteDbManager instance = new SQLiteDbManager();
    private SQLiteDatabase whereAreYouDb;

    private static final String DATABASE_NAME = "whereareyou";

    static final String ID_FIELD_NAME = "_id";
    static final String CONTACT_DATA_TABLE = "contactdata";
    static final String CONTACT_LOCATION_DATA_TABLE = "contactlocationdata";

    private static final String CONTACT_DATA_TABLE_CREATE = 
            "create table " + CONTACT_DATA_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + SQLiteContactRepository.EMAIL_FIELD_NAME + " text not null, " 
            + SQLiteContactRepository.CONTACT_ID_FIELD_NAME + " text not null, "
            + SQLiteContactRepository.DISPLAY_NAME_FIELD_NAME + " text not null, "
            + SQLiteContactRepository.LOCATION_REQUEST_ALLOWED_FIELD_NAME + " int not null, "
            + SQLiteContactRepository.LOCATION_REQUEST_RECEIVED_FIELD_NAME + " int not null, "
            + SQLiteContactRepository.RECEIVED_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME + " real not null, "
            + SQLiteContactRepository.LOCATION_REQUEST_AGREED_FIELD_NAME + " int not null, "
            + SQLiteContactRepository.LOCATION_REQUEST_SENT_FIELD_NAME + " int not null, "
            + SQLiteContactRepository.SENT_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME + " real not null);";
    private static final String CONTACT_LOCATION_DATA_TABLE_CREATE = 
            "create table " + CONTACT_LOCATION_DATA_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + SQLiteContactLocationRepository.LOCATION_TIME_FIELD_NAME + " real not null, "
            + SQLiteContactLocationRepository.ACCURACY_FIELD_NAME + "accuracy real not null, "
            + SQLiteContactLocationRepository.LATITUDE_FIELD_NAME + " real not null, " 
            + SQLiteContactLocationRepository.LONGITUDE_FIELD_NAME + " real not null, "
            + SQLiteContactLocationRepository.SPEED_FIELD_NAME + " real not null, "
            + SQLiteContactLocationRepository.HAS_ACCURACY_FIELD_NAME + " int not null, " 
            + SQLiteContactLocationRepository.HAS_SPEED_FIELD_NAME + " int not null, "
            + SQLiteContactLocationRepository.CONTACT_ID_FIELD_NAME + " int not null);";

    /** Ensures singleton pattern. */
    private SQLiteDbManager() {
        //
    }

    /** Provides instance of singleton. */
    public static SQLiteDbManager getInstance() {
        return instance;
    }

    public void initialise(Context ctx) {
        whereAreYouDb = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (!databaseExists()) {
            createDatabase();
        }
    }

    public void close() {
        whereAreYouDb.close();
    }

    public SQLiteDatabase getDatabase() {
        return whereAreYouDb;
    }

    private void createDatabase() {
        whereAreYouDb.execSQL("drop table if exists " + CONTACT_DATA_TABLE);
        whereAreYouDb.execSQL("drop table if exists " + CONTACT_LOCATION_DATA_TABLE);
        whereAreYouDb.execSQL(CONTACT_DATA_TABLE_CREATE);
        whereAreYouDb.execSQL(CONTACT_LOCATION_DATA_TABLE_CREATE);
    }

    private boolean databaseExists() {
        try {
            whereAreYouDb.query(CONTACT_DATA_TABLE, new String[] { "_id" }, null, null, null, null, null);
            whereAreYouDb.query(CONTACT_LOCATION_DATA_TABLE, new String[] { "_id" }, null, null, null, null, null);
            Log.d(getClass().getName(), "db exists");
            return true;
        } catch (SQLiteException ex) {
            Log.d(getClass().getName(), "db does not exist");
            return false;
        }
    }
}
