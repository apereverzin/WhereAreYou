package com.creek.whereareyou.android.infrastructure.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.Identifiable;

/**
 * 
 * @author andreypereverzin
 */
public abstract class AbstractIdentifiableRepository<T extends Identifiable> extends AbstractSQLiteRepository<T> {
    public AbstractIdentifiableRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }
    
    @Override
    protected T createEntityFromCursor(Cursor cursor) {
        T t = createInstance();
        t.setId(cursor.getInt(0));
        ContactCompoundId contactCompoundId = new ContactCompoundId(cursor.getString(1), cursor.getString(2));
        t.setContactCompoundId(contactCompoundId);
        return t;
    }
    
    protected abstract T createInstance();
}
