package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.domain.RequestResponse;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;

/**
 * 
 * @author andreypereverzin
 */
public final class SQLiteContactResponseRepository extends AbstractRequestResponseRepository implements ContactResponseRepository {
    private static final String TAG = SQLiteContactResponseRepository.class.getSimpleName();

    public SQLiteContactResponseRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public final List<RequestResponse> getAllContactResponses() {
        Log.d(TAG, "getAllContactResponses()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            return createEntityListFromCursor(contactDataCursor);
        } finally {
            SQLiteUtils.closeCursor(contactDataCursor);
        }
    }

    @Override
    public final List<RequestResponse> getContactResponsesByContactId(String contactId) {
        Log.d(TAG, "getContactRequestsByContactId()");
        return retrieveEntitiesByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public final List<RequestResponse> getContactResponsesByEmail(String email) {
        Log.d(TAG, "getContactResponsesByEmail()");
        return retrieveEntitiesByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    public final List<RequestResponse> getUnsentContactResponses() {
        Log.d(TAG, "getUnsentContactResponses()");
        return getUnsent();
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_RESPONSE_TABLE;
    }
    
    static final String getCreateTableCommand() {
        return String.format(TABLE_CREATE, CONTACT_RESPONSE_TABLE);
    }
}
