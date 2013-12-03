package com.creek.whereareyou.android.infrastructure.sqlite;

import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteUtils.closeCursor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.RequestAllowance;
import com.creek.whereareyoumodel.repository.ContactDataRepository;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactDataRepository extends AbstractSQLiteRepository<ContactData> implements ContactDataRepository {
    private static final String TAG = SQLiteContactDataRepository.class.getSimpleName();
    
    static final String REQUEST_ALLOWANCE_FIELD_NAME = "allowance";
    static final String ALLOWANCE_DATE_FIELD_NAME = "allowance_date";

    private final String[] fieldNames = new String[] {
            REQUEST_ALLOWANCE_FIELD_NAME,
            ALLOWANCE_DATE_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "int not null", 
            "real not null"
        };

    @Override
    public final List<ContactData> getAllContactData() {
        Log.d(TAG, "getAllContactData()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            return createEntityListFromCursor(contactDataCursor);
        } finally {
            closeCursor(contactDataCursor);
        }
    }

    @Override
    public final Map<String, ContactData> getAllContactDataAsMap() {
        Log.d(TAG, "getAllContactData()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            return createContactDataMapFromCursor(contactDataCursor);
        } finally {
            closeCursor(contactDataCursor);
        }
    }

    @Override
    public final ContactData getContactDataByContactId(String contactId) {
        Log.d(TAG, "getContactDataByContactId()");
        return retrieveContactDataByCriteria(ANDR_CONT_ID_FIELD_NAME, contactId);
    }

    @Override
    public final ContactData getContactDataByEmail(String email) {
        Log.d(TAG, "getContactDataByEmail()");
        return retrieveContactDataByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    public final ContactData getContactDataById(int id) {
        Log.d(TAG, "getContactDataById()");
        return retrieveContactDataByCriteria(ID_FIELD_NAME, Integer.toString(id));
    }
    
    @Override
    protected final ContentValues getContentValues(ContactData contactData) {
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(contactData);
        values.put(REQUEST_ALLOWANCE_FIELD_NAME, contactData.getRequestAllowance().getCode());
        values.put(ALLOWANCE_DATE_FIELD_NAME, contactData.getAllowanceDate());
        return values;
    }
    
    @Override
    protected final ContactData createEntity(Cursor cursor) {
        ContactData contactData = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        contactData.setRequestAllowance(RequestAllowance.getRequestAllowance(cursor.getInt(numberOfFields++)));
        contactData.setAllowanceDate(cursor.getLong(numberOfFields++));
        Log.d(TAG, "createEntity(): " + contactData);
        return contactData;
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_DATA_TABLE;
    }
    
    @Override
    protected int getNumberOfFields() {
        return super.getNumberOfFields() + fieldNames.length;
    }

    @Override
    protected String[] getFieldNames() {
        return Util.concatArrays(super.getFieldNames(), fieldNames);
    }

    @Override
    protected String[] getFieldTypes() {
        return Util.concatArrays(super.getFieldTypes(), fieldTypes);
    }

    @Override
    protected final ContactData createEntityInstance() {
        return new ContactData();
    }

    private ContactData retrieveContactDataByCriteria(String fieldName, String fieldValue) {
        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(fieldName, fieldValue, null, null);
            return getSingleContactDataFromCursor(contactDataCursor);
        } finally {
            closeCursor(contactDataCursor);
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
