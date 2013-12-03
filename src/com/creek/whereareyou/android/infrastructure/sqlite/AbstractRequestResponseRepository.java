package com.creek.whereareyou.android.infrastructure.sqlite;

import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.CREATION_TIME_KNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.SENT_TIME_UNKNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.SENT_TIME_KNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteUtils.closeCursor;
import static com.creek.whereareyou.android.util.Util.concatArrays;

import java.util.List;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.sendable.GenericRequestResponse;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractRequestResponseRepository <T extends GenericRequestResponse> extends AbstractSQLiteRepository<T> {
    private static final String TAG = AbstractRequestResponseRepository.class.getSimpleName();

    static final String MESSAGE_FIELD_NAME = "message";
    static final String TIME_CREATED_FIELD_NAME = "time_crtd";
    static final String TIME_SENT_FIELD_NAME = "time_sent";
    static final String TIME_RECEIVED_FIELD_NAME = "time_rcvd";
    static final String PROCESSED_FIELD_NAME = "processed";

    private final String[] fieldNames = new String[] {
            MESSAGE_FIELD_NAME,
            TIME_CREATED_FIELD_NAME,
            TIME_SENT_FIELD_NAME,
            TIME_RECEIVED_FIELD_NAME,
            PROCESSED_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "text", 
            "real not null", 
            "real not null", 
            "real not null",
            "int not null"
        };
    
    @Override
    protected ContentValues getContentValues(T t) {
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(t);
        values.put(MESSAGE_FIELD_NAME, t.getMessage());
        values.put(TIME_CREATED_FIELD_NAME, t.getTimeCreated());
        values.put(TIME_SENT_FIELD_NAME, t.getTimeSent());
        values.put(TIME_RECEIVED_FIELD_NAME, t.getTimeReceived());
        values.put(PROCESSED_FIELD_NAME, t.isProcessed() ? INT_TRUE : INT_FALSE);
        return values;
    }

    @Override
    protected T createEntity(Cursor cursor) {
        Log.d(TAG, "createEntity()");
        T requestResponse = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        requestResponse.setMessage(cursor.getString(numberOfFields++));
        requestResponse.setTimeCreated(cursor.getLong(numberOfFields++));
        requestResponse.setTimeSent(cursor.getLong(numberOfFields++));
        requestResponse.setTimeReceived(cursor.getLong(numberOfFields++));
        requestResponse.setProcessed(cursor.getInt(numberOfFields) == INT_TRUE);
        return requestResponse;
    }
    
    @Override
    protected int getNumberOfFields() {
        return super.getNumberOfFields() + fieldNames.length;
    }
    
    @Override
    protected String[] getFieldNames() {
        return  concatArrays(super.getFieldNames(), fieldNames);
    }
    
    @Override
    protected String[] getFieldTypes() {
        return concatArrays(super.getFieldTypes(), fieldTypes);
    }
    
    protected final List<T> getUnsent() {
        String criteria = createWhereAndCriteria(new ComparisonClause[]{CREATION_TIME_KNOWN, SENT_TIME_UNKNOWN});
        return createEntityList(criteria);
    }
    
    protected final List<T> getSent() {
        String criteria = createWhereAndCriteria(new ComparisonClause[]{CREATION_TIME_KNOWN, SENT_TIME_KNOWN});
        return createEntityList(criteria);
    }

    private List<T> createEntityList(String criteria) {
        Cursor cursor = null;
        
        try {
            cursor = createCursor(criteria, null, null);
            return createEntityListFromCursor(cursor);
        } finally {
            closeCursor(cursor);
        }
    }
}
