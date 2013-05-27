package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.repository.ContactDataRepository;

/**
 * 
 * @author andreypereverzin
 */
public final class SQLiteContactDataRepository extends AbstractSQLiteRepository<ContactData> implements ContactDataRepository {
    private static final String TAG = SQLiteContactDataRepository.class.getSimpleName();
    
    static final String CONTACT_ID_FIELD_NAME = "contact_id";
    static final String EMAIL_FIELD_NAME = "email";
    static final String DISPLAY_NAME_FIELD_NAME = "display_name";
    static final String REQUEST_ALLOWED_FIELD_NAME = "allowed";

    static final String CONTACT_DATA_TABLE_CREATE = 
            "create table " + CONTACT_DATA_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + CONTACT_ID_FIELD_NAME + " text not null, "
            + EMAIL_FIELD_NAME + " text, " 
            + DISPLAY_NAME_FIELD_NAME + " text not null, "
            + REQUEST_ALLOWED_FIELD_NAME + " int not null);";

    public SQLiteContactDataRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public List<ContactData> getAllContactData() {
        Log.d(TAG, "getAllContactData()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            return createEntityListFromCursor(contactDataCursor);
        } finally {
            SQLiteUtils.closeCursor(contactDataCursor);
        }
    }

    @Override
    public Map<String, ContactData> getAllContactDataAsMap() {
        Log.d(TAG, "getAllContactData()");

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
        Log.d(TAG, "getContactDataByContactId()");
        return retrieveContactDataByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public ContactData getContactDataByEmail(String email) {
        Log.d(TAG, "getContactDataByEmail()");
        return retrieveContactDataByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    public ContactData getContactDataById(int id) {
        Log.d(TAG, "getContactDataById()");
        return retrieveContactDataByCriteria(ID_FIELD_NAME, Integer.toString(id));
    }
    
    @Override
    protected ContentValues getContentValues(ContactData contactData) {
        ContentValues values = new ContentValues();
        values.put(EMAIL_FIELD_NAME, contactData.getContactCompoundId().getContactEmail());
        values.put(CONTACT_ID_FIELD_NAME, contactData.getContactCompoundId().getContactId());
        values.put(DISPLAY_NAME_FIELD_NAME, contactData.getDisplayName());
        values.put(REQUEST_ALLOWED_FIELD_NAME, contactData.isRequestAllowed() ? 1 : 0);
        return values;
    }
    
    @Override
    protected ContactData createEntityFromCursor(Cursor contactDataCursor) {
        ContactData contactData = new ContactData();
        contactData.setId(contactDataCursor.getInt(0));
        ContactCompoundId contactCompoundId = new ContactCompoundId(contactDataCursor.getString(1), contactDataCursor.getString(2));
        contactData.setContactCompoundId(contactCompoundId);
        contactData.setDisplayName(contactDataCursor.getString(3));
        contactData.setRequestAllowed(contactDataCursor.getInt(4) == 1);
        return contactData;
    }
    
    @Override
    protected String getTableName() {
        return CONTACT_DATA_TABLE;
    }
    
    @Override
    protected String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                EMAIL_FIELD_NAME,
                CONTACT_ID_FIELD_NAME,
                DISPLAY_NAME_FIELD_NAME,
                REQUEST_ALLOWED_FIELD_NAME
        };
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

    private Map<String, ContactData> createContactDataMapFromCursor(Cursor contactDataCursor) {
        Map<String, ContactData> contactDataMap = new HashMap<String, ContactData>();

        if (contactDataCursor != null && contactDataCursor.getCount() > 0) {
            contactDataCursor.moveToFirst();
            do {
                ContactData contactData = createEntityFromCursor(contactDataCursor);
                contactDataMap.put(contactData.getContactCompoundId().getContactId(), contactData);
            } while (contactDataCursor.moveToNext());
        }

        return contactDataMap;
    }
    
    private ContactData getSingleContactDataFromCursor(Cursor contactDataCursor) {
        if (contactDataCursor != null && contactDataCursor.getCount() > 0) {
            contactDataCursor.moveToFirst();
            return createEntityFromCursor(contactDataCursor);
        }
        return null;
    }
}
