package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.RequestCode;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactRequestRepository extends AbstractRequestResponseRepository<ContactRequest> implements ContactRequestRepository {
    private static final String TAG = SQLiteContactRequestRepository.class.getSimpleName();

    static final String REQUEST_CODE_FIELD_NAME = "request_code";


    private final String[] fieldNames = new String[] {
            REQUEST_CODE_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "integer not null"
        };

    public SQLiteContactRequestRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }
    
    @Override
    protected ContentValues getContentValues(ContactRequest contactRequest) {
        ContentValues values = super.getContentValues(contactRequest);
        values.put(REQUEST_CODE_FIELD_NAME, contactRequest.getRequestCode().getCode());
        return values;
    }

    @Override
    protected ContactRequest createEntityFromCursor(Cursor cursor) {
        ContactRequest contactRequest = super.createEntityFromCursor(cursor);
        int numberOfFields = super.getNumberOfFields();
        int code = cursor.getInt(numberOfFields + 1);
        contactRequest.setRequestCode(RequestCode.getRequestCode(code));
        return contactRequest;
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
        return retrieveEntitiesByCriteria(ANDR_CONT_ID_FIELD_NAME, contactId);
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
    public final List<ContactRequest> getAllUnrespondedContactRequests() {
        Log.d(TAG, "getAllUnrespondedContactRequests()");
        return null;
    }
    
    @Override
    public final List<ContactRequest> getUnrespondedLocationRequests() {
        Log.d(TAG, "getUnrespondedLocationRequests()");
        String criteria = createWhereAndCriteria(new String[]{TIME_CREATED_FIELD_NAME + "=0", TIME_RECEIVED_FIELD_NAME + ">0", REQUEST_CODE_FIELD_NAME + "=" + RequestCode.LOCATION.getCode(), RESPONSE_ID_FIELD_NAME + "=" + 0});
        Cursor cursor = createCursor(criteria, null, null);
        return createEntityListFromCursor(cursor);
    }

    @Override
    protected final ContactRequest createEntityInstance() {
        return new ContactRequest();
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_REQUEST_TABLE;
    }
}
