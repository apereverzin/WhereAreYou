package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.sendable.GenericRequestResponse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractRequestResponseRepository <T extends GenericRequestResponse> extends AbstractSQLiteRepository<T> {
    static final String CODE_FIELD_NAME = "code";
    static final String MESSAGE_FIELD_NAME = "message";
    static final String TIME_CREATED_FIELD_NAME = "time_crtd";
    static final String TIME_SENT_FIELD_NAME = "time_sent";
    static final String TIME_RECEIVED_FIELD_NAME = "time_rcvd";


    public AbstractRequestResponseRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }
    
    @Override
    protected ContentValues getContentValues(T t) {
        ContentValues values = super.getContentValues(t);
        values.put(CODE_FIELD_NAME, t.getCode());
        values.put(MESSAGE_FIELD_NAME, t.getMessage());
        values.put(TIME_CREATED_FIELD_NAME, t.getTimeCreated());
        values.put(TIME_SENT_FIELD_NAME, t.getTimeSent());
        values.put(TIME_RECEIVED_FIELD_NAME, t.getTimeReceived());
        return values;
    }

    @Override
    protected T createEntityFromCursor(Cursor cursor) {
        T requestResponse = super.createEntityFromCursor(cursor);
        requestResponse.setCode(cursor.getInt(3));
        requestResponse.setMessage(cursor.getString(4));
        requestResponse.setTimeCreated(cursor.getLong(5));
        requestResponse.setTimeSent(cursor.getLong(6));
        requestResponse.setTimeReceived(cursor.getLong(7));
        return requestResponse;
    }
    
    @Override
    protected String[] getFieldNames() {
        return Util.concatArrays(super.getFieldNames(), new String[] {
            CODE_FIELD_NAME,
            MESSAGE_FIELD_NAME,
            TIME_CREATED_FIELD_NAME,
            TIME_SENT_FIELD_NAME,
            TIME_RECEIVED_FIELD_NAME
        });
    }
    
    @Override
    protected String[] getFieldTypes() {
        return Util.concatArrays(super.getFieldTypes(), new String[] { 
            "integer not null", 
            "text", 
            "real not null", 
            "real not null", 
            "real not null"
        });
    }
    
    protected final List<T> getUnsent() {
        String criteria = createWhereAndCriteria(new String[]{TIME_CREATED_FIELD_NAME + ">0", TIME_SENT_FIELD_NAME + "=0"});
        Cursor cursor = createCursor(criteria, null, null);
        return createEntityListFromCursor(cursor);
    }
}
