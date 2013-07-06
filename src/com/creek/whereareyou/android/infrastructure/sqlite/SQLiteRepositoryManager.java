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
        whereAreYouDb = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);

        contactDataRepository = new SQLiteContactDataRepository(whereAreYouDb);
        contactRequestRepository = new SQLiteContactRequestRepository(whereAreYouDb);
        contactResponseRepository = new SQLiteContactResponseRepository(whereAreYouDb);
        locationRepository = new SQLiteContactLocationRepository(whereAreYouDb);

        if (!databaseExists()) {
            createDatabase();
            Log.d(TAG, "===================Database created");
        }
        
        // TODO remove this call
        createContactData();
    }

    public void closeDatabase() {
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
        Log.d(TAG, "-----: " + ((SQLiteContactDataRepository)contactDataRepository).getCreateTableCommand());
        whereAreYouDb.execSQL(((SQLiteContactRequestRepository)contactRequestRepository).getCreateTableCommand());
        Log.d(TAG, "-----: " + ((SQLiteContactRequestRepository)contactRequestRepository).getCreateTableCommand());
        whereAreYouDb.execSQL(((SQLiteContactResponseRepository)contactResponseRepository).getCreateTableCommand());
        Log.d(TAG, "-----: " + ((SQLiteContactResponseRepository)contactResponseRepository).getCreateTableCommand());
        whereAreYouDb.execSQL(((SQLiteContactLocationRepository)locationRepository).getCreateTableCommand());
        Log.d(TAG, "-----: " + ((SQLiteContactLocationRepository)locationRepository).getCreateTableCommand());
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
            Log.d(TAG, "------------db does not exist");
            return false;
        }
    }
    
    private void dropTable(String tableName) {
        whereAreYouDb.execSQL(String.format("%s %s", DROP_TABLE, tableName));
    }
    
    private void queryTable(String tableName) {
        whereAreYouDb.query(tableName, new String[] { AbstractSQLiteRepository.ID_FIELD_NAME }, null, null, null, null, null);
    }
    
    // TODO remove this method
    private void createContactData() {
        String email = "andrey.pereverzin@gmail.com";
        ContactData contactData = contactDataRepository.getContactDataByEmail(email);
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
