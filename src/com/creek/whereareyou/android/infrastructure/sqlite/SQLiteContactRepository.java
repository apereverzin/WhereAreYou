package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.repository.ContactRepository;

/**
 * 
 * @author andreypereverzin
 */
public class SQLiteContactRepository extends AbstractSQLiteRepository implements ContactRepository {
    private static final String TAG = SQLiteContactRepository.class.getSimpleName();
    
    static final String CONTACT_ID_FIELD_NAME = "contact_id";
    static final String EMAIL_FIELD_NAME = "email";
    static final String DISPLAY_NAME_FIELD_NAME = "display_name";
    static final String LOCATION_REQUEST_ALLOWED_FIELD_NAME = "location_request_allowed";
    static final String LOCATION_REQUEST_RECEIVED_FIELD_NAME = "location_request_received";
    static final String RECEIVED_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME = "received_location_request_timestamp";
    static final String LOCATION_REQUEST_AGREED_FIELD_NAME = "location_request_agreed";
    static final String LOCATION_REQUEST_SENT_FIELD_NAME = "location_request_sent";
    static final String SENT_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME = "sent_location_request_timestamp";

    static final String CONTACT_DATA_TABLE_CREATE = 
            "create table " + CONTACT_DATA_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + CONTACT_ID_FIELD_NAME + " text not null, "
            + EMAIL_FIELD_NAME + " text, " 
            + DISPLAY_NAME_FIELD_NAME + " text not null, "
            + LOCATION_REQUEST_ALLOWED_FIELD_NAME + " int not null, "
            + LOCATION_REQUEST_RECEIVED_FIELD_NAME + " int not null, "
            + RECEIVED_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME + " real not null, "
            + LOCATION_REQUEST_AGREED_FIELD_NAME + " int not null, "
            + LOCATION_REQUEST_SENT_FIELD_NAME + " int not null, "
            + SENT_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME + " real not null);";

    public SQLiteContactRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public ContactData createContactData(ContactData contactData) {
        ContentValues values = getContactContentValues(contactData);
        try {
            long id = whereAreYouDb.insert(CONTACT_DATA_TABLE, null, values);
            contactData.setId((int) id);
            Log.d(getClass().getName(), "---------------------ContactData created: " + contactData.getId());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return contactData;
    }

    @Override
    public boolean deleteContactData(int id) {
        return super.delete(id) ;
    }

    @Override
    public List<ContactData> getAllContactData() {
        Log.d(getClass().getName(), "getAllContactData()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            return createContactDataListFromCursor(contactDataCursor);
        } finally {
            SQLiteUtils.closeCursor(contactDataCursor);
        }
    }

    @Override
    public Map<String, ContactData> getAllContactDataAsMap() {
        Log.d(getClass().getName(), "getAllContactData()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            return createContactDataMapFromCursor(contactDataCursor);
        } finally {
            SQLiteUtils.closeCursor(contactDataCursor);
        }
    }

    @Override
    public ContactData getContactDataByContactId(String contactId) {
        Log.d(getClass().getName(), "getContactDataByContactId()");
        return retrieveContactDataByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public ContactData getContactDataByEmail(String email) {
        Log.d(getClass().getName(), "getContactDataByEmail()");
        return retrieveContactDataByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    public ContactData getContactDataById(int id) {
        Log.d(getClass().getName(), "getContactDataById()");
        return retrieveContactDataByCriteria(ID_FIELD_NAME, Integer.toString(id));
    }

    @Override
    public boolean updateContactData(ContactData contactData) {
        Log.d(getClass().getName(), "updateContactData: " + contactData.getId());
        ContentValues values = getContactContentValues(contactData);
        return whereAreYouDb.update(CONTACT_DATA_TABLE, values, createWhereCriteria(ID_FIELD_NAME, Integer.toString(contactData.getId())), null) > 0;
    }

    private ContactData retrieveContactDataByCriteria(String fieldName, String fieldValue) {
        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(fieldName, fieldValue, null, null);
            return getSingleContactDataFromCursor(contactDataCursor);
        } finally {
            SQLiteUtils.closeCursor(contactDataCursor);
        }
    }

    private ContentValues getContactContentValues(ContactData contactData) {
        ContentValues values = new ContentValues();
        values.put(EMAIL_FIELD_NAME, contactData.getEmail());
        values.put(CONTACT_ID_FIELD_NAME, contactData.getContactId());
        Log.d(TAG, "------------contactData.getDisplayName()111: " + contactData.getDisplayName());
        values.put(DISPLAY_NAME_FIELD_NAME, contactData.getDisplayName());
        Log.d(TAG, "------------contactData.getDisplayName(): " + contactData.getDisplayName());
        values.put(LOCATION_REQUEST_ALLOWED_FIELD_NAME, contactData.isLocationRequestAllowed() ? 1 : 0);
        Log.d(TAG, "------------contactData.getDisplayName(): " + contactData.isLocationRequestAllowed());
        values.put(LOCATION_REQUEST_RECEIVED_FIELD_NAME, contactData.isLocationRequestReceived() ? 1 : 0);
        values.put(RECEIVED_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME, contactData.getReceivedLocationRequestTimestamp());
        values.put(LOCATION_REQUEST_AGREED_FIELD_NAME, contactData.isLocationRequestAgreed() ? 1 : 0);
        values.put(LOCATION_REQUEST_SENT_FIELD_NAME, contactData.isLocationRequestSent() ? 1 : 0);
        values.put(SENT_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME, contactData.getSentLocationRequestTimestamp());
        return values;
    }
    
    private Map<String, ContactData> createContactDataMapFromCursor(Cursor contactDataCursor) {
        Map<String, ContactData> contactDataMap = new HashMap<String, ContactData>();

        if (contactDataCursor != null && contactDataCursor.getCount() > 0) {
            contactDataCursor.moveToFirst();
            do {
                ContactData contactData = createContactDataFromCursor(contactDataCursor);
                Log.d(getClass().getName(), "---------------------contactData: " + contactData.getId());
                contactDataMap.put(contactData.getContactId(), contactData);
            } while (contactDataCursor.moveToNext());
        }

        return contactDataMap;
    }
    
    private List<ContactData> createContactDataListFromCursor(Cursor contactDataCursor) {
        List<ContactData> contactDataList = new ArrayList<ContactData>();

        if (contactDataCursor != null && contactDataCursor.getCount() > 0) {
            contactDataCursor.moveToFirst();
            do {
                ContactData contactData = createContactDataFromCursor(contactDataCursor);
                Log.d(getClass().getName(), "---------------------contactData: " + contactData.getId());
                contactDataList.add(contactData);
            } while (contactDataCursor.moveToNext());
        }

        return contactDataList;
    }
    
    private ContactData createContactDataFromCursor(Cursor contactDataCursor) {
        ContactData contactData = new ContactData();
        contactData.setId(contactDataCursor.getInt(0));
        contactData.setContactId(contactDataCursor.getString(1));
        contactData.setEmail(contactDataCursor.getString(2));
        contactData.setDisplayName(contactDataCursor.getString(3));
        contactData.setLocationRequestAllowed(contactDataCursor.getInt(4) == 1);
        contactData.setLocationRequestReceived(contactDataCursor.getInt(5) == 1);
        contactData.setReceivedLocationRequestTimestamp(contactDataCursor.getLong(6));
        contactData.setLocationRequestAgreed(contactDataCursor.getInt(7) == 1);
        contactData.setLocationRequestSent(contactDataCursor.getInt(8) == 1);
        contactData.setSentLocationRequestTimestamp(contactDataCursor.getLong(9));
        return contactData;
    }
    
    protected String getTableName() {
        return CONTACT_DATA_TABLE;
    }
    
    protected String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                EMAIL_FIELD_NAME,
                CONTACT_ID_FIELD_NAME,
                DISPLAY_NAME_FIELD_NAME,
                LOCATION_REQUEST_ALLOWED_FIELD_NAME,
                LOCATION_REQUEST_RECEIVED_FIELD_NAME,
                RECEIVED_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME,
                LOCATION_REQUEST_AGREED_FIELD_NAME,
                LOCATION_REQUEST_SENT_FIELD_NAME,
                SENT_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME
        };
    }
    
    private ContactData getSingleContactDataFromCursor(Cursor contactDataCursor) {
        if (contactDataCursor != null && contactDataCursor.getCount() > 0) {
            contactDataCursor.moveToFirst();
            return createContactDataFromCursor(contactDataCursor);
        }
        return null;
    }
}
