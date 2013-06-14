package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.repository.LocationRepository;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactLocationRepository extends AbstractSQLiteRepository<LocationData> implements LocationRepository {

    static final String LOCATION_TIME_FIELD_NAME = "location_time";
    static final String ACCURACY_FIELD_NAME = "accuracy";
    static final String LATITUDE_FIELD_NAME = "latitude";
    static final String LONGITUDE_FIELD_NAME = "longitude";
    static final String SPEED_FIELD_NAME = "speed";
    static final String HAS_ACCURACY_FIELD_NAME = "has_accuracy";
    static final String HAS_SPEED_FIELD_NAME = "has_speed";
    static final String CONTACT_ID_FIELD_NAME = "contact_id";

    private final String[] fieldNames = new String[] {
            LOCATION_TIME_FIELD_NAME,
            ACCURACY_FIELD_NAME,
            LATITUDE_FIELD_NAME,
            LONGITUDE_FIELD_NAME,
            SPEED_FIELD_NAME,
            HAS_ACCURACY_FIELD_NAME,
            HAS_SPEED_FIELD_NAME,
            CONTACT_ID_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "real not null", 
            "real not null", 
            "real not null", 
            "real not null", 
            "real not null", 
            "integer not null", 
            "integer not null", 
            "integer not null"
        };

    public SQLiteContactLocationRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public final List<LocationData> getContactLocationDataByContactId(int email) {
        return null;
    }

    @Override
    public final List<LocationData> getContactLocationDataByEmail(String email) {
        return null;
    }

    @Override
    public final List<LocationData> getContactLocationDataByIdOfContact(String contactId) {
        return null;
    }

    @Override
    public final LocationData getLatestContactLocationDataByContactId(int contactId) {
        return null;
    }

    @Override
    public final LocationData getLatestContactLocationDataByEmail(String contactId) {
        return null;
    }

    @Override
    public final LocationData getLatestContactLocationDataByIdOfContact(String contactId) {
        return null;
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_LOCATION_TABLE;
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
    protected final ContentValues getContentValues(LocationData locationData) {
        ContentValues values = super.getContentValues(locationData);
        values.put(LOCATION_TIME_FIELD_NAME, locationData.getLocationTime());
        values.put(ACCURACY_FIELD_NAME, locationData.getAccuracy());
        values.put(LATITUDE_FIELD_NAME, locationData.getLatitude());
        values.put(LONGITUDE_FIELD_NAME, locationData.getLongitude());
        values.put(SPEED_FIELD_NAME, locationData.getSpeed());
        values.put(HAS_ACCURACY_FIELD_NAME, locationData.hasAccuracy() ? TRUE : FALSE);
        values.put(HAS_SPEED_FIELD_NAME, locationData.hasSpeed() ? TRUE : FALSE);
        values.put(CONTACT_ID_FIELD_NAME, locationData.getContactCompoundId().getContactId());
        return values;
    }
    
    @Override
    protected final LocationData createEntityFromCursor(Cursor cursor) {
        LocationData locationData = new LocationData();
        int numberOfFields = super.getNumberOfFields();
        locationData.setLocationTime(cursor.getLong(numberOfFields + 1));
        locationData.setAccuracy(cursor.getFloat(numberOfFields + 2));
        locationData.setLatitude(cursor.getDouble(numberOfFields + 3));
        locationData.setLongitude(cursor.getDouble(numberOfFields + 4));
        locationData.setSpeed(cursor.getFloat(numberOfFields + 5));
        locationData.setHasAccuracy(cursor.getInt(numberOfFields + 6) == TRUE);
        locationData.setHasSpeed(cursor.getInt(numberOfFields + 7) == TRUE);
        return locationData;
    }
    
    @Override
    protected final LocationData createEntityInstance() {
        return new LocationData();
    }
        
    private LocationData getContactLocationDataFromCursor(Cursor contactLocationDataCursor) {
        if (contactLocationDataCursor != null && contactLocationDataCursor.getCount() > 0) {
            contactLocationDataCursor.moveToFirst();
            return createEntityFromCursor(contactLocationDataCursor);
        }
        return null;
    }
}
