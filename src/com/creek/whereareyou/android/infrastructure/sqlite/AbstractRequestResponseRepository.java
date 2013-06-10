package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import com.creek.whereareyoumodel.domain.sendable.GenericRequestResponse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractRequestResponseRepository <T extends GenericRequestResponse> extends AbstractIdentifiableRepository<T> {
    static final String TIME_SENT_FIELD_NAME = "time_sent";
    static final String TIME_RECEIVED_FIELD_NAME = "time_rcvd";
    static final String TIME_CREATED_FIELD_NAME = "time_crtd";
    static final String CODE_FIELD_NAME = "code";
    static final String MESSAGE_FIELD_NAME = "message";

    static final String TABLE_CREATE = 
            "create table %s " 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + CONTACT_ID_FIELD_NAME + " text not null, "
            + EMAIL_FIELD_NAME + " text not null, " 
            + MESSAGE_FIELD_NAME + " text, " 
            + CODE_FIELD_NAME + " integer not null, " 
            + TIME_SENT_FIELD_NAME + " real not null, " 
            + TIME_RECEIVED_FIELD_NAME + " real not null, "
            + TIME_CREATED_FIELD_NAME + " real not null);";

    public AbstractRequestResponseRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    protected final ContentValues getContentValues(T requestResponse) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_ID_FIELD_NAME, requestResponse.getContactCompoundId().getContactId());
        values.put(EMAIL_FIELD_NAME, requestResponse.getContactCompoundId().getContactEmail());
        values.put(MESSAGE_FIELD_NAME, requestResponse.getMessage());
        values.put(CODE_FIELD_NAME, requestResponse.getCode());
        values.put(TIME_SENT_FIELD_NAME, requestResponse.getTimeSent());
        values.put(TIME_RECEIVED_FIELD_NAME, requestResponse.getTimeReceived());
        values.put(TIME_CREATED_FIELD_NAME, requestResponse.getTimeCreated());
        return values;
    }

    @Override
    protected final T createEntityFromCursor(Cursor cursor) {
        T requestResponse = super.createEntityFromCursor(cursor);
        requestResponse.setMessage(cursor.getString(3));
        requestResponse.setCode(cursor.getInt(4));
        requestResponse.setTimeSent(cursor.getLong(5));
        requestResponse.setTimeReceived(cursor.getLong(6));
        requestResponse.setTimeCreated(cursor.getLong(7));
        return requestResponse;
    }
    
    @Override
    protected final String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                CONTACT_ID_FIELD_NAME,
                EMAIL_FIELD_NAME,
                MESSAGE_FIELD_NAME,
                CODE_FIELD_NAME,
                TIME_SENT_FIELD_NAME,
                TIME_RECEIVED_FIELD_NAME,
                TIME_CREATED_FIELD_NAME
        };
    }
    
    protected final List<T> getUnsent() {
        String criteria = createWhereAndCriteria(new String[]{TIME_CREATED_FIELD_NAME + ">0", TIME_SENT_FIELD_NAME + "=0"});
        Cursor cursor = createCursor(criteria, null, null);
        return createEntityListFromCursor(cursor);
    }
}
