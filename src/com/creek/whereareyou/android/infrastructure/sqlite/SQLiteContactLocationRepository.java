package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.ContactLocationData;
import com.creek.whereareyoumodel.repository.ContactLocationRepository;

/**
 * 
 * @author andreypereverzin
 */
public class SQLiteContactLocationRepository extends AbstractSQLiteRepository implements ContactLocationRepository {

    static final String LOCATION_TIME_FIELD_NAME = "location_time";
    static final String ACCURACY_FIELD_NAME = "accuracy";
    static final String LATITUDE_FIELD_NAME = "latitude";
    static final String LONGITUDE_FIELD_NAME = "longitude";
    static final String SPEED_FIELD_NAME = "speed";
    static final String HAS_ACCURACY_FIELD_NAME = "has_accuracy";
    static final String HAS_SPEED_FIELD_NAME = "has_speed";
    static final String CONTACT_ID_FIELD_NAME = "contact_id";

    public SQLiteContactLocationRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public ContactLocationData saveContactLocationData(ContactLocationData contactLocationData) {
        return null;
    }

    @Override
    public boolean deleteContactLocationData(int id) {
        return super.delete(id) ;
    }

    @Override
    public List<ContactLocationData> getContactLocationDataByContactId(int email) {
        return null;
    }

    @Override
    public List<ContactLocationData> getContactLocationDataByEmail(String email) {
        return null;
    }

    @Override
    public List<ContactLocationData> getContactLocationDataByIdOfContact(String contactId) {
        return null;
    }

    @Override
    public ContactLocationData getLatestContactLocationDataByContactId(int contactId) {
        return null;
    }

    @Override
    public ContactLocationData getLatestContactLocationDataByEmail(String contactId) {
        return null;
    }

    @Override
    public ContactLocationData getLatestContactLocationDataByIdOfContact(String contactId) {
        return null;
    }
    
    private ContentValues getContactContentValues(ContactLocationData contactLocationData) {
        ContentValues values = new ContentValues();
        values.put(EMAIL_FIELD_NAME, contactLocationData.getEmail());
        values.put(CONTACT_ID_FIELD_NAME, contactLocationData.getContactId());
        values.put(DISPLAY_NAME_FIELD_NAME, contactLocationData.getDisplayName());
        values.put(LOCATION_REQUEST_ALLOWED_FIELD_NAME, contactLocationData.isLocationRequestAllowed() ? 1 : 0);
        values.put(LOCATION_REQUEST_RECEIVED_FIELD_NAME, contactLocationData.isLocationRequestReceived() ? 1 : 0);
        values.put(RECEIVED_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME, contactLocationData.getReceivedLocationRequestTimestamp());
        values.put(LOCATION_REQUEST_AGREED_FIELD_NAME, contactLocationData.isLocationRequestAgreed() ? 1 : 0);
        values.put(LOCATION_REQUEST_SENT_FIELD_NAME, contactLocationData.isLocationRequestSent() ? 1 : 0);
        values.put(SENT_LOCATION_REQUEST_TIMESTAMP_FIELD_NAME, contactLocationData.getSentLocationRequestTimestamp());
        return values;
    }
    
    private ContactLocationData createContactLocationDataFromCursor(Cursor contactDataCursor) {
        ContactLocationData contactLocationData = new ContactLocationData();
        contactLocationData.setId(contactDataCursor.getInt(0));
        contactLocationData.setContactId(contactDataCursor.getInt(1));
        contactLocationData.setDisplayName(contactDataCursor.getString(2));
        contactLocationData.setLocationRequestAllowed(contactDataCursor.getInt(3) == 1);
        contactLocationData.setLocationRequestReceived(contactDataCursor.getInt(4) == 1);
        contactLocationData.setReceivedLocationRequestTimestamp(contactDataCursor.getLong(5));
        contactLocationData.setLocationRequestAgreed(contactDataCursor.getInt(6) == 1);
        contactLocationData.setLocationRequestSent(contactDataCursor.getInt(7) == 1);
        contactLocationData.setSentLocationRequestTimestamp(contactDataCursor.getLong(8));
        contactLocationData.setDisplayName(contactDataCursor.getString(2));
        contactLocationData.setDisplayName(contactDataCursor.getString(2));
        contactLocationData.setDisplayName(contactDataCursor.getString(2));
        return contactLocationData;
    }
    
    protected String getTableName() {
        return SQLiteDbManager.CONTACT_DATA_TABLE;
    }
    
    protected String[] getFieldNames() {
        return new String[] {SQLiteDbManager.ID_FIELD_NAME,
                LOCATION_TIME_FIELD_NAME,
                ACCURACY_FIELD_NAME,
                LATITUDE_FIELD_NAME,
                LONGITUDE_FIELD_NAME,
                SPEED_FIELD_NAME,
                HAS_ACCURACY_FIELD_NAME,
                HAS_SPEED_FIELD_NAME,
                CONTACT_ID_FIELD_NAME
        };
    }
    
    private ContactData getContactDataFromCursor(Cursor contactDataCursor) {
        if (contactDataCursor != null && contactDataCursor.getCount() > 0) {
            contactDataCursor.moveToFirst();
            return createContactLocationDataFromCursor(contactDataCursor);
        }
        return null;
    }
}
