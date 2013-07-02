package com.creek.whereareyou.android.infrastructure.sqlite;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyoumodel.repository.ContactDataRepository;
import com.creek.whereareyoumodel.repository.LocationRepository;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 * 
 */
public class SQLiteRepositoryManager {
    private static final String TAG = SQLiteRepositoryManager.class.getSimpleName();
    
    private static final SQLiteRepositoryManager instance = new SQLiteRepositoryManager();
    private boolean initialized = false;
    private SQLiteDatabase whereAreYouDb;
    
    private ContactDataRepository contactDataRepository;
    private ContactRequestRepository contactRequestRepository;
    private ContactResponseRepository<ContactResponseEntity> contactResponseRepository;
    private LocationRepository locationRepository;

    private static final String DATABASE_NAME = "whereareyou";
    private static final String DROP_TABLE = "drop table if exists ";

    private SQLiteRepositoryManager() {
        //
    }

    public static SQLiteRepositoryManager getInstance() {
        return instance;
    }

    public void initialise(Context ctx) {
        if (!initialized) {
            whereAreYouDb = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);

            contactDataRepository = new SQLiteContactDataRepository(whereAreYouDb);
            contactRequestRepository = new SQLiteContactRequestRepository(whereAreYouDb);
            contactResponseRepository = new SQLiteContactResponseRepository(whereAreYouDb);
            locationRepository = new SQLiteContactLocationRepository(whereAreYouDb);
            
            //if (!databaseExists()) {
            createDatabase();
            //}
    
            initialized = true;
        }
    }

    public void close() {
        whereAreYouDb.close();
    }

    public SQLiteDatabase getDatabase() {
        return whereAreYouDb;
    }

    public ContactDataRepository getContactDataRepository() {
        return contactDataRepository;
    }

    public ContactRequestRepository getContactRequestRepository() {
        return contactRequestRepository;
    }

    public ContactResponseRepository<ContactResponseEntity> getContactResponseRepository() {
        return contactResponseRepository;
    }

    public LocationRepository getLocationRepository() {
        return locationRepository;
    }

    private void createDatabase() {
        dropTable(AbstractSQLiteRepository.CONTACT_DATA_TABLE);
        dropTable(AbstractSQLiteRepository.CONTACT_REQUEST_TABLE);
        dropTable(AbstractSQLiteRepository.CONTACT_RESPONSE_TABLE);
        dropTable(AbstractSQLiteRepository.CONTACT_LOCATION_TABLE);
        whereAreYouDb.execSQL(((SQLiteContactDataRepository)contactDataRepository).getCreateTableCommand());
        System.out.println("-----: " + ((SQLiteContactDataRepository)contactDataRepository).getCreateTableCommand());
        whereAreYouDb.execSQL(((SQLiteContactRequestRepository)contactRequestRepository).getCreateTableCommand());
        System.out.println("-----: " + ((SQLiteContactRequestRepository)contactRequestRepository).getCreateTableCommand());
        whereAreYouDb.execSQL(((SQLiteContactResponseRepository)contactResponseRepository).getCreateTableCommand());
        System.out.println("-----: " + ((SQLiteContactResponseRepository)contactResponseRepository).getCreateTableCommand());
        whereAreYouDb.execSQL(((SQLiteContactLocationRepository)locationRepository).getCreateTableCommand());
        System.out.println("-----: " + ((SQLiteContactLocationRepository)locationRepository).getCreateTableCommand());
    }

    private boolean databaseExists() {
        try {
            queryTable(AbstractSQLiteRepository.CONTACT_DATA_TABLE);
            queryTable(AbstractSQLiteRepository.CONTACT_REQUEST_TABLE);
            queryTable(AbstractSQLiteRepository.CONTACT_REQUEST_TABLE);
            queryTable(AbstractSQLiteRepository.CONTACT_LOCATION_TABLE);
            Log.d(TAG, "db exists");
            return true;
        } catch (SQLiteException ex) {
            Log.d(TAG, "db does not exist");
            return false;
        }
    }
    
    private void dropTable(String tableName) {
        whereAreYouDb.execSQL(String.format("%s %s", DROP_TABLE, tableName));
    }
    
    private void queryTable(String tableName) {
        whereAreYouDb.query(tableName, new String[] { AbstractSQLiteRepository.ID_FIELD_NAME }, null, null, null, null, null);
    }
}
