package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import static com.creek.whereareyou.android.infrastructure.sqlite.Comparison.EQUALS;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.CREATION_TIME_KNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.CREATION_TIME_UNKNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.RECEIVED_TIME_KNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.RECEIVED_TIME_UNKNOWN;
import static com.creek.whereareyou.android.infrastructure.sqlite.ComparisonClause.NOT_PROCESSED;
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
    
    static final ComparisonClause LOCATION_REQUEST = new ComparisonClause(REQUEST_CODE_FIELD_NAME, EQUALS, LOCATION.getCode());

    @Override
    protected ContentValues getContentValues(ContactRequest contactRequest) {
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(contactRequest);
        values.put(REQUEST_CODE_FIELD_NAME, contactRequest.getRequestCode().getCode());
        return values;
    }

    @Override
    protected ContactRequest createEntity(Cursor cursor) {
        ContactRequest contactRequest = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        int code = cursor.getInt(numberOfFields);
        contactRequest.setRequestCode(RequestCode.getRequestCode(code));
//        int responseId = cursor.getInt(numberOfFields + 1);
//        if (responseId != UNDEFINED_INT) {
//            // TODO setResponse(...)
//        }
        Log.d(TAG, "createEntity(): " + contactRequest);
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
    public final List<ContactRequest> getIncomingUnrespondedLocationRequests() {
        Log.d(TAG, "getUnrespondedLocationRequests()");
        String criteria = createWhereAndCriteria(
                new ComparisonClause[]{CREATION_TIME_UNKNOWN, RECEIVED_TIME_KNOWN, LOCATION_REQUEST, NOT_PROCESSED});
        Cursor cursor = createCursor(criteria, null, null);
        Log.d(TAG, "--------------+++++++++++getIncomingUnrespondedLocationRequests: " + cursor.getCount());
        return createEntityListFromCursor(cursor);
    }
    
    @Override
    public final List<ContactRequest> getOutgoingUnrespondedLocationRequests() {
        Log.d(TAG, "getUnrespondedLocationRequests()");
        String criteria = createWhereAndCriteria(
                new ComparisonClause[]{CREATION_TIME_KNOWN, RECEIVED_TIME_UNKNOWN, LOCATION_REQUEST, NOT_PROCESSED});
        Cursor cursor = createCursor(criteria, null, null);
        Log.d(TAG, "--------------+++++++++++getOutgoingUnrespondedLocationRequests: " + cursor.getCount());
        return createEntityListFromCursor(cursor);
    }

    @Override
    public void updateProcessedOutgoingContactRequests(String contactEmail) {
        Log.d(TAG, "updateProcessedOutgoingContactRequests: " + contactEmail);
        Log.d(TAG, "--------------updateProcessedOutgoingContactRequests: " + contactEmail);
        ComparisonClause emailComparisonClause = new ComparisonClause(EMAIL_FIELD_NAME, EQUALS, contactEmail);
        String criteria = createWhereAndCriteria(
                new ComparisonClause[]{emailComparisonClause, CREATION_TIME_KNOWN, RECEIVED_TIME_UNKNOWN, LOCATION_REQUEST, NOT_PROCESSED});
        ContentValues valuesToSet = getValuesToSet();
        whereAreYouDb.update(getTableName(), valuesToSet, criteria, null);
    }

    @Override
    protected final ContactRequest createEntityInstance() {
        return new ContactRequest();
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_REQUEST_TABLE;
    }

    private ContentValues getValuesToSet() {
        ContentValues valuesToSet = new ContentValues();
        valuesToSet.put(PROCESSED_FIELD_NAME, INT_TRUE);
        return valuesToSet;
    }
}
