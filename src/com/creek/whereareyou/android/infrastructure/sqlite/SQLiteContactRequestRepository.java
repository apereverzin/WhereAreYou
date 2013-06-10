package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactRequestRepository extends AbstractRequestResponseRepository<ContactRequest> implements ContactRequestRepository {
    private static final String TAG = SQLiteContactRequestRepository.class.getSimpleName();

    public SQLiteContactRequestRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public final List<ContactRequest> getAllContactRequests() {
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
    public final List<ContactRequest> getContactRequestsByContactId(String contactId) {
        Log.d(TAG, "getContactRequestsByContactId()");
        return retrieveEntitiesByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public final List<ContactRequest> getContactRequestsByEmail(String email) {
        Log.d(TAG, "getContactRequestsByEmail()");
        return retrieveEntitiesByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    public final List<ContactRequest> getUnsentContactRequests() {
        Log.d(TAG, "getUnsentContactRequests()");
        return getUnsent();
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_REQUEST_TABLE;
    }
    
    @Override
    protected final ContactRequest createEntityInstance() {
        return new ContactRequest();
    }
    
    static final String getCreateTableCommand() {
        return String.format(TABLE_CREATE, CONTACT_REQUEST_TABLE);
    }
}
