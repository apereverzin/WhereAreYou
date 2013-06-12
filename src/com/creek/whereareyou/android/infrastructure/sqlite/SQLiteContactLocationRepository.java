package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.repository.ContactLocationRepository;
import com.creek.whereareyoumodel.domain.sendable.ContactLocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.valueobject.SendableLocationData;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactLocationRepository extends AbstractSQLiteRepository<ContactLocationData> implements ContactLocationRepository {

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
    public final List<ContactLocationData> getContactLocationDataByContactId(int email) {
        return null;
    }

    @Override
    public final List<ContactLocationData> getContactLocationDataByEmail(String email) {
        return null;
    }

    @Override
    public final List<ContactLocationData> getContactLocationDataByIdOfContact(String contactId) {
        return null;
    }

    @Override
    public final ContactLocationData getLatestContactLocationDataByContactId(int contactId) {
        return null;
    }

    @Override
    public final ContactLocationData getLatestContactLocationDataByEmail(String contactId) {
        return null;
    }

    @Override
    public final ContactLocationData getLatestContactLocationDataByIdOfContact(String contactId) {
        return null;
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_LOCATION_TABLE;
    }
    
    @Override
    protected final String[] getFieldNames() {
        return Util.concatArrays(super.getFieldNames(), new String[] {
            LOCATION_TIME_FIELD_NAME,
            ACCURACY_FIELD_NAME,
            LATITUDE_FIELD_NAME,
            LONGITUDE_FIELD_NAME,
            SPEED_FIELD_NAME,
            HAS_ACCURACY_FIELD_NAME,
            HAS_SPEED_FIELD_NAME,
            CONTACT_ID_FIELD_NAME
        });
    }
    
    @Override
    protected String[] getFieldTypes() {
        return Util.concatArrays(super.getFieldTypes(), new String[] { 
            "real not null", 
            "real not null", 
            "real not null", 
            "real not null", 
            "real not null", 
            "int not null", 
            "int not null", 
            "int not null"
        });
    }
    
    @Override
    protected final ContentValues getContentValues(ContactLocationData contactLocationData) {
        ContentValues values = super.getContentValues(contactLocationData);
        values.put(LOCATION_TIME_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getLocationTime());
        values.put(ACCURACY_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getAccuracy());
        values.put(LATITUDE_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getLatitude());
        values.put(LONGITUDE_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getLongitude());
        values.put(SPEED_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().getSpeed());
        values.put(HAS_ACCURACY_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().hasAccuracy() ? 1 : 0);
        values.put(HAS_SPEED_FIELD_NAME, contactLocationData.getOwnerLocationData().getLocationData().hasSpeed() ? 1 : 0);
        values.put(CONTACT_ID_FIELD_NAME, contactLocationData.getContactCompoundId().getContactId());
        return values;
    }
    
    @Override
    protected final ContactLocationData createEntityFromCursor(Cursor contactLocationDataCursor) {
        ContactLocationData contactLocationData = new ContactLocationData();
        contactLocationData.setId(contactLocationDataCursor.getInt(0));
        String senderEmail = contactLocationDataCursor.getString(1);
        long timeSent = contactLocationDataCursor.getLong(2);
        long timeReceived = contactLocationDataCursor.getLong(3);
        float accuracy = contactLocationDataCursor.getFloat(4);
        double latitude = contactLocationDataCursor.getDouble(5);
        double longitude = contactLocationDataCursor.getDouble(6);
        float speed = contactLocationDataCursor.getFloat(7);
        boolean hasAccuracy = contactLocationDataCursor.getInt(8) == 1;
        boolean hasSpeed = contactLocationDataCursor.getInt(9) == 1;
        LocationData locationData = new LocationData(accuracy, latitude, longitude, speed, hasAccuracy, hasSpeed);
        SendableLocationData ownerLocationData = new SendableLocationData(timeSent, locationData);
        contactLocationData.setOwnerLocationData(ownerLocationData);
        contactLocationData.setTimeReceived(timeReceived);
        return contactLocationData;
    }
    
    @Override
    protected final ContactLocationData createEntityInstance() {
        return new ContactLocationData();
    }
        
    private ContactLocationData getContactLocationDataFromCursor(Cursor contactLocationDataCursor) {
        if (contactLocationDataCursor != null && contactLocationDataCursor.getCount() > 0) {
            contactLocationDataCursor.moveToFirst();
            return createEntityFromCursor(contactLocationDataCursor);
        }
        return null;
    }
}
