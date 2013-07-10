package com.creek.whereareyou.android.infrastructure.sqlite;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
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
    private SQLiteDatabase whereAreYouDb;
    
    private SQLiteContactDataRepository contactDataRepository;
    private SQLiteContactRequestRepository contactRequestRepository;
    private SQLiteContactResponseRepository contactResponseRepository;
    private SQLiteContactLocationRepository locationRepository;

    private static final String DATABASE_NAME = "whereareyou";
    private static final String DROP_TABLE = "drop table if exists %s";

    private SQLiteRepositoryManager() {
        //
    }

    public static SQLiteRepositoryManager getInstance() {
        return instance;
    }

    public void openDatabase(Context ctx) {
        Log.d(TAG, "------------openDatabase");
        whereAreYouDb = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
        Log.d(TAG, "------------isOpen: " + whereAreYouDb.isOpen());
    }

    public void closeDatabase() {
        Log.d(TAG, "------------closeDatabase");
        whereAreYouDb.close();
    }

    public SQLiteDatabase getDatabase() {
        return whereAreYouDb;
    }

    public ContactDataRepository getContactDataRepository() {
        if (contactDataRepository == null) {
            contactDataRepository = new SQLiteContactDataRepository();
        }

        contactDataRepository.setDatabase(whereAreYouDb);
        return contactDataRepository;
    }

    public ContactRequestRepository getContactRequestRepository() {
        if (contactRequestRepository == null) {
            Log.d(TAG, "------------getContactRequestRepository isOpen: " + whereAreYouDb.isOpen());
            contactRequestRepository = new SQLiteContactRequestRepository();
        }

        contactRequestRepository.setDatabase(whereAreYouDb);
        return contactRequestRepository;
    }

    public ContactResponseRepository<ContactResponseEntity> getContactResponseRepository() {
        if (contactResponseRepository == null) {
            contactResponseRepository = new SQLiteContactResponseRepository();
        }
        
        contactResponseRepository.setDatabase(whereAreYouDb);
        return contactResponseRepository;
    }

    public LocationRepository getLocationRepository() {
        if (locationRepository == null) {
            locationRepository = new SQLiteContactLocationRepository();
        }
        
        locationRepository.setDatabase(whereAreYouDb);
        return locationRepository;
    }

    public void createDatabaseIfDoesNotExist(Context ctx) {
        Log.d(TAG, "------------createDatabaseIfDoesNotExist");
        whereAreYouDb = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);

        if (!databaseExists()) {
            contactDataRepository = new SQLiteContactDataRepository();
            contactRequestRepository = new SQLiteContactRequestRepository();
            contactResponseRepository = new SQLiteContactResponseRepository();
            locationRepository = new SQLiteContactLocationRepository();

            dropTable(AbstractSQLiteRepository.CONTACT_DATA_TABLE);
            dropTable(AbstractSQLiteRepository.CONTACT_REQUEST_TABLE);
            dropTable(AbstractSQLiteRepository.CONTACT_RESPONSE_TABLE);
            dropTable(AbstractSQLiteRepository.CONTACT_LOCATION_TABLE);
            whereAreYouDb.execSQL(((SQLiteContactDataRepository) contactDataRepository).getCreateTableCommand());
            whereAreYouDb.execSQL(((SQLiteContactRequestRepository) contactRequestRepository).getCreateTableCommand());
            whereAreYouDb.execSQL(((SQLiteContactResponseRepository) contactResponseRepository).getCreateTableCommand());
            whereAreYouDb.execSQL(((SQLiteContactLocationRepository) locationRepository).getCreateTableCommand());
            Log.d(TAG, "===================Database created");

            // TODO remove this call
            createContactData();
        }
        
        closeDatabase();
    }

    private boolean databaseExists() {
        try {
            queryTable(AbstractSQLiteRepository.CONTACT_DATA_TABLE);
            queryTable(AbstractSQLiteRepository.CONTACT_REQUEST_TABLE);
            queryTable(AbstractSQLiteRepository.CONTACT_REQUEST_TABLE);
            queryTable(AbstractSQLiteRepository.CONTACT_LOCATION_TABLE);
            Log.d(TAG, "------------db exists");
            return true;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            Log.d(TAG, "------------db does not exist");
            return false;
        }
    }
    
    private void dropTable(String tableName) {
        whereAreYouDb.execSQL(String.format(DROP_TABLE, tableName));
    }
    
    private void queryTable(String tableName) {
        whereAreYouDb.query(tableName, new String[] { AbstractSQLiteRepository.ID_FIELD_NAME }, null, null, null, null, null);
    }
    
    // TODO remove this method
    private void createContactData() {
        String email = "andrey.pereverzin@googlemail.com";
        ContactData contactData = getContactDataRepository().getContactDataByEmail(email);
        if (contactData == null) {
            contactData = new ContactData();
            ContactCompoundId contactCompoundId = new ContactCompoundId("18", email);
            contactData.setContactCompoundId(contactCompoundId);
            contactData.setDisplayName("Adam Leaf");
            contactData.setRequestAllowed(true);
            contactDataRepository.create(contactData);
        }
    }
}
