package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creek.whereareyoumodel.domain.ContactLocationData;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.OwnerLocationData;
import com.creek.whereareyoumodel.repository.ContactLocationRepository;

/**
 * 
 * @author andreypereverzin
 */
public class SQLiteContactLocationRepository extends AbstractSQLiteRepository implements ContactLocationRepository {

    static final String SENDER_EMAIL_FIELD_NAME = "sender_email";
    static final String TIME_SENT_FIELD_NAME = "time_sent";
    static final String LOCATION_TIME_FIELD_NAME = "location_time";
    static final String ACCURACY_FIELD_NAME = "accuracy";
    static final String LATITUDE_FIELD_NAME = "latitude";
    static final String LONGITUDE_FIELD_NAME = "longitude";
    static final String SPEED_FIELD_NAME = "speed";
    static final String HAS_ACCURACY_FIELD_NAME = "has_accuracy";
    static final String HAS_SPEED_FIELD_NAME = "has_speed";
    static final String CONTACT_ID_FIELD_NAME = "contact_id";

    static final String CONTACT_LOCATION_DATA_TABLE_CREATE = 
            "create table " + CONTACT_LOCATION_DATA_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + SENDER_EMAIL_FIELD_NAME + " text not null, "
            + TIME_SENT_FIELD_NAME + " real not null, "
            + LOCATION_TIME_FIELD_NAME + " real not null, "
            + ACCURACY_FIELD_NAME + "accuracy real not null, "
            + LATITUDE_FIELD_NAME + " real not null, " 
            + LONGITUDE_FIELD_NAME + " real not null, "
            + SPEED_FIELD_NAME + " real not null, "
            + HAS_ACCURACY_FIELD_NAME + " int not null, " 
            + HAS_SPEED_FIELD_NAME + " int not null, "
            + CONTACT_ID_FIELD_NAME + " int not null);";

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
        values.put(LOCATION_TIME_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getLocationTime());
        values.put(SENDER_EMAIL_FIELD_NAME, contactLocationData.getOwnerLocationData().getSenderEmail());
        values.put(TIME_SENT_FIELD_NAME, contactLocationData.getOwnerLocationData().getTimeSent());
        values.put(ACCURACY_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getAccuracy());
        values.put(LATITUDE_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getLatitude());
        values.put(LONGITUDE_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getLongitude());
        values.put(SPEED_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getSpeed());
        values.put(HAS_ACCURACY_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().hasAccuracy() ? 1 : 0);
        values.put(HAS_SPEED_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().hasSpeed() ? 1 : 0);
        values.put(CONTACT_ID_FIELD_NAME, contactLocationData.getContactId());
        return values;
    }
    
    private ContactLocationData createContactLocationDataFromCursor(Cursor contactLocationDataCursor) {
        ContactLocationData contactLocationData = new ContactLocationData();
        contactLocationData.setId(contactLocationDataCursor.getInt(0));
        String senderEmail = contactLocationDataCursor.getString(1);
        long timeSent = contactLocationDataCursor.getLong(2);
        float accuracy = contactLocationDataCursor.getFloat(3);
        double latitude = contactLocationDataCursor.getDouble(4);
        double longitude = contactLocationDataCursor.getDouble(5);
        float speed = contactLocationDataCursor.getFloat(6);
        boolean hasAccuracy = contactLocationDataCursor.getInt(7) == 1;
        boolean hasSpeed = contactLocationDataCursor.getInt(8) == 1;
        LocationData locationData = new LocationData(accuracy, latitude, longitude, speed, hasAccuracy, hasSpeed);
        OwnerLocationData ownerLocationData = new OwnerLocationData(senderEmail, timeSent, locationData);
        contactLocationData.setOwnerLocationData(ownerLocationData);
        return contactLocationData;
    }
    
    protected String getTableName() {
        return CONTACT_DATA_TABLE;
    }
    
    protected String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                SENDER_EMAIL_FIELD_NAME,
                TIME_SENT_FIELD_NAME,
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
    
    private ContactLocationData getContactLocationDataFromCursor(Cursor contactLocationDataCursor) {
        if (contactLocationDataCursor != null && contactLocationDataCursor.getCount() > 0) {
            contactLocationDataCursor.moveToFirst();
            return createContactLocationDataFromCursor(contactLocationDataCursor);
        }
        return null;
    }
}
