package com.creek.whereareyou.android.infrastructure.sqlite;

import java.io.File;
import java.util.List;

import com.creek.whereareyou.WhereAreYouApplication;
import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import static com.creek.whereareyoumodel.domain.RequestAllowance.ALWAYS;
import com.creek.whereareyoumodel.repository.ContactDataRepository;
import com.creek.whereareyoumodel.repository.LocationRepository;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;

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
    private DBFileManager dbFileManager;

    private static final String DATABASE_PATH = "/data/data/com.creek.whereareyou/databases/";
    private static final String DATABASE_NAME = "whereareyou.db";
    private static final String DROP_TABLE = "drop table if exists %s";

    private SQLiteRepositoryManager() {
        dbFileManager = new DBFileManager();
    }

    public static SQLiteRepositoryManager getInstance() {
        return instance;
    }

    public void openDatabase() {
        Log.d(TAG, "++++++++++++openDatabase");
        whereAreYouDb = WhereAreYouApplication.getDatabase();
        Log.d(TAG, "++++++++++++isOpen: " + whereAreYouDb.isOpen());
    }

    public void closeDatabase() {
        Log.d(TAG, "++++++++++++closeDatabase");
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

    public SQLiteDatabase createDatabaseIfDoesNotExist() {
        Log.d(TAG, "createDatabaseIfDoesNotExist");
        File db = new File(DATABASE_PATH);
        if (db.exists()) {
            db.delete();
        }
        if (!db.exists()) {
            db.mkdirs();
        }
        whereAreYouDb = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE + SQLiteDatabase.CREATE_IF_NECESSARY);

        if (!databaseTablesExist()) {
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
            
            persistReservedData();

            // TODO remove this call
            createContactData();
        }
        
        //closeDatabase();
        return whereAreYouDb;
    }

    private void persistReservedData() {
        DBFileManager dbFileManager = new DBFileManager();
        List<ContactData> contactDataList = dbFileManager.retrieveReservedContactData();
        for (int i = 0; i < contactDataList.size(); i++) {
            ContactData contactData = contactDataList.get(i);
            contactDataRepository.create(contactData);
        }
    }

    private boolean databaseTablesExist() {
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
            contactData.setRequestAllowance(ALWAYS);
            contactDataRepository.create(contactData);
        }
    }
}
