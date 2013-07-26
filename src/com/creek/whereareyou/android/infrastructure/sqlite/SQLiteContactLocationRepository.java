package com.creek.whereareyou.android.infrastructure.sqlite;

import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.Comparison.EQUALS;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.Comparison.GREATER_THAN;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.repository.LocationRepository;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactLocationRepository extends AbstractSQLiteRepository<LocationData> implements LocationRepository {
    private static final String TAG = SQLiteContactLocationRepository.class.getSimpleName();

    static final String LOCATION_TIME_FIELD_NAME = "location_time";
    static final String ACCURACY_FIELD_NAME = "accuracy";
    static final String LATITUDE_FIELD_NAME = "latitude";
    static final String LONGITUDE_FIELD_NAME = "longitude";
    static final String SPEED_FIELD_NAME = "speed";
    static final String HAS_ACCURACY_FIELD_NAME = "has_accuracy";
    static final String HAS_SPEED_FIELD_NAME = "has_speed";
    
    private final String[] fieldNames = new String[] {
            LOCATION_TIME_FIELD_NAME,
            ACCURACY_FIELD_NAME,
            LATITUDE_FIELD_NAME,
            LONGITUDE_FIELD_NAME,
            SPEED_FIELD_NAME,
            HAS_ACCURACY_FIELD_NAME,
            HAS_SPEED_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "real not null", 
            "real not null", 
            "real not null", 
            "real not null", 
            "real not null", 
            "int not null", 
            "int not null"
        };

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
    public final LocationData getMyActualLocationData(int timeout) {
        Log.d(TAG, "getMyActualLocationData()");
        ComparisonClause myLocation = new ComparisonClause(ANDR_CONT_ID_FIELD_NAME, EQUALS, UNDEFINED_INT);
        long expirationTime = System.currentTimeMillis() - timeout;
        ComparisonClause actualLocation = new ComparisonClause(LOCATION_TIME_FIELD_NAME, GREATER_THAN, expirationTime);
        String criteria = createWhereAndCriteria(new ComparisonClause[]{myLocation, actualLocation});
        Cursor cursor = createCursor(criteria, null, null);
        Log.d(TAG, "--------------getMyActualLocationData: " + cursor.getCount());
        return createEntityFromCursor(cursor);
    }
    
    @Override
    public LocationData getLocationDataById(long id) {
        Log.d(TAG, "getLocationDataById(): " + id);
        return retrieveEntityById(id);
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
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(locationData);
        values.put(LOCATION_TIME_FIELD_NAME, locationData.getLocationTime());
        values.put(ACCURACY_FIELD_NAME, locationData.getAccuracy());
        values.put(LATITUDE_FIELD_NAME, locationData.getLatitude());
        values.put(LONGITUDE_FIELD_NAME, locationData.getLongitude());
        values.put(SPEED_FIELD_NAME, locationData.getSpeed());
        values.put(HAS_ACCURACY_FIELD_NAME, locationData.hasAccuracy() ? INT_TRUE : INT_FALSE);
        values.put(HAS_SPEED_FIELD_NAME, locationData.hasSpeed() ? INT_TRUE : INT_FALSE);
        return values;
    }
    
    @Override
    protected final LocationData createEntity(Cursor cursor) {
        LocationData locationData = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        locationData.setLocationTime(cursor.getLong(numberOfFields++));
        locationData.setAccuracy(cursor.getFloat(numberOfFields++));
        locationData.setLatitude(cursor.getDouble(numberOfFields++));
        locationData.setLongitude(cursor.getDouble(numberOfFields++));
        locationData.setSpeed(cursor.getFloat(numberOfFields++));
        locationData.setHasAccuracy(cursor.getInt(numberOfFields++) == INT_TRUE);
        locationData.setHasSpeed(cursor.getInt(numberOfFields) == INT_TRUE);
        Log.d(TAG, "createEntity(): " + locationData);
        return locationData;
    }
    
    @Override
    protected final LocationData createEntityInstance() {
        return new LocationData();
    }
}
