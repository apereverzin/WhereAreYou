package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.domain.RequestResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;

/**
 * 
 * @author andreypereverzin
 */
public final class SQLiteContactRequestRepository extends AbstractRequestResponseRepository implements ContactRequestRepository {
    private static final String TAG = SQLiteContactRequestRepository.class.getSimpleName();

    public SQLiteContactRequestRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public final List<RequestResponse> getAllContactRequests() {
        Log.d(TAG, "getAllContactRequests()");

        Cursor contactRequestCursor = null;
        try {
            contactRequestCursor = createCursor(null, null, null);
            return createEntityListFromCursor(contactRequestCursor);
        } finally {
            SQLiteUtils.closeCursor(contactRequestCursor);
        }
    }

    @Override
    public final List<RequestResponse> getContactRequestsByContactId(String contactId) {
        Log.d(TAG, "getContactRequestsByContactId()");
        return retrieveEntitiesByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public final List<RequestResponse> getContactRequestsByEmail(String email) {
        Log.d(TAG, "getContactRequestsByEmail()");
        return retrieveEntitiesByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    public final List<RequestResponse> getUnsentContactRequests() {
        Log.d(TAG, "getUnsentContactRequests()");
        return getUnsent();
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_REQUEST_TABLE;
    }
    
    static final String getCreateTableCommand() {
        return String.format(TABLE_CREATE, CONTACT_REQUEST_TABLE);
    }
}
