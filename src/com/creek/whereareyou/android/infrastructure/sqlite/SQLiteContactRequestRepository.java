package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.Comparison.EQUALS;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.CREATION_TIME_UNKNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.RECEIVED_TIME_KNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.PENDING;
import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.RequestCode;
import static com.creek.whereareyoumodel.domain.sendable.RequestCode.LOCATION;

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
            "int not null"
        };

    public SQLiteContactRequestRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }
    
    @Override
    protected ContentValues getContentValues(ContactRequest contactRequest) {
        Log.d(TAG, "getContentValues()");
        Log.d(TAG, "--------------getContentValues()");
        ContentValues values = super.getContentValues(contactRequest);
        values.put(REQUEST_CODE_FIELD_NAME, contactRequest.getRequestCode().getCode());
        return values;
    }

    @Override
    protected ContactRequest createEntity(Cursor cursor) {
        Log.d(TAG, "createEntity()");
        Log.d(TAG, "--------------createEntity()");
        ContactRequest contactRequest = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        Log.d(TAG, "--------------numberOfFields: " + numberOfFields);
        int code = cursor.getInt(numberOfFields);
        contactRequest.setRequestCode(RequestCode.getRequestCode(code));
//        int responseId = cursor.getInt(numberOfFields + 1);
//        if (responseId != UNDEFINED_INT) {
//            // TODO setResponse(...)
//        }
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
        ComparisonClause locationRequest = new ComparisonClause(REQUEST_CODE_FIELD_NAME, EQUALS, LOCATION.getCode());
        String criteria = createWhereAndCriteria(
                new ComparisonClause[]{CREATION_TIME_UNKNOWN, RECEIVED_TIME_KNOWN, locationRequest, PENDING});
        Log.d(TAG, "--------------getUnrespondedLocationRequests");
        Cursor cursor = createCursor(criteria, null, null);
        Log.d(TAG, "--------------getUnrespondedLocationRequests: " + cursor.getCount());
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
