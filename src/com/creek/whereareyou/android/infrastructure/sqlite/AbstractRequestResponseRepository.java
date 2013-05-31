package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import com.creek.whereareyoumodel.domain.RequestResponse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author andreypereverzin
 */
public abstract class AbstractRequestResponseRepository<T extends RequestResponse> extends AbstractIdentifiableRepository<T> {
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
    protected final ContentValues getContentValues(T t) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_ID_FIELD_NAME, t.getContactCompoundId().getContactId());
        values.put(EMAIL_FIELD_NAME, t.getContactCompoundId().getContactEmail());
        values.put(MESSAGE_FIELD_NAME, t.getMessage());
        values.put(CODE_FIELD_NAME, t.getCode());
        values.put(TIME_SENT_FIELD_NAME, t.getTimeSent());
        values.put(TIME_RECEIVED_FIELD_NAME, t.getTimeReceived());
        values.put(TIME_CREATED_FIELD_NAME, t.getTimeCreated());
        return values;
    }

    @Override
    protected final T createEntityFromCursor(Cursor cursor) {
        T t = super.createEntityFromCursor(cursor);
        t.setMessage(cursor.getString(3));
        t.setCode(cursor.getInt(4));
        t.setTimeSent(cursor.getLong(5));
        t.setTimeReceived(cursor.getLong(6));
        t.setTimeCreated(cursor.getLong(7));
        return t;
    }
    
    @Override
    protected final String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                EMAIL_FIELD_NAME,
                CONTACT_ID_FIELD_NAME,
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
    protected abstract T createEntityInstance();
}
