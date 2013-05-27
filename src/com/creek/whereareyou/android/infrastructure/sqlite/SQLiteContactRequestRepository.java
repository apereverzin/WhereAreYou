package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactRequest;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;

/**
 * 
 * @author andreypereverzin
 */
public final class SQLiteContactRequestRepository extends AbstractSQLiteRepository<ContactRequest> implements ContactRequestRepository {
    private static final String TAG = SQLiteContactRequestRepository.class.getSimpleName();

    static final String CONTACT_ID_FIELD_NAME = "contact_id";
    static final String EMAIL_FIELD_NAME = "email";
    static final String MESSAGE_FIELD_NAME = "message";
    static final String REQUEST_CODE_FIELD_NAME = "req_code";
    static final String TIME_SENT_FIELD_NAME = "time_sent";
    static final String TIME_RECEIVED_FIELD_NAME = "time_rcvd";

    static final String CONTACT_REQUEST_TABLE_CREATE = 
            "create table " + CONTACT_REQUEST_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + CONTACT_ID_FIELD_NAME + " text not null, "
            + EMAIL_FIELD_NAME + " text, " 
            + MESSAGE_FIELD_NAME + " text, " 
            + REQUEST_CODE_FIELD_NAME + " integer not null, " 
            + TIME_SENT_FIELD_NAME + " real not null, " 
            + TIME_RECEIVED_FIELD_NAME + " real not null);";

    public SQLiteContactRequestRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public List<ContactRequest> getAllContactRequests() {
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
    public List<ContactRequest> getContactRequestsByContactId(String contactId) {
        Log.d(TAG, "getContactRequestsByContactId()");
        return retrieveContactRequestsByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public List<ContactRequest> getContactRequestsByEmail(String email) {
        Log.d(TAG, "getContactRequestsByEmail()");
        return retrieveContactRequestsByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    protected ContentValues getContentValues(ContactRequest contactRequest) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_ID_FIELD_NAME, contactRequest.getContactCompoundId().getContactId());
        values.put(EMAIL_FIELD_NAME, contactRequest.getContactCompoundId().getContactEmail());
        values.put(MESSAGE_FIELD_NAME, contactRequest.getMessage());
        values.put(REQUEST_CODE_FIELD_NAME, contactRequest.getRequestCode());
        values.put(TIME_SENT_FIELD_NAME, contactRequest.getTimeSent());
        values.put(TIME_RECEIVED_FIELD_NAME, contactRequest.getTimeReceived());
        return values;
    }
    
    @Override
    protected ContactRequest createEntityFromCursor(Cursor contactRequestCursor) {
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setId(contactRequestCursor.getInt(0));
        ContactCompoundId contactCompoundId = new ContactCompoundId(contactRequestCursor.getString(1), contactRequestCursor.getString(2));
        contactRequest.setContactCompoundId(contactCompoundId);
        contactRequest.setMessage(contactRequestCursor.getString(3));
        contactRequest.setRequestCode(contactRequestCursor.getInt(4));
        contactRequest.setTimeSent(contactRequestCursor.getLong(5));
        contactRequest.setTimeReceived(contactRequestCursor.getLong(6));
        return contactRequest;
    }
    
    @Override
    protected String getTableName() {
        return CONTACT_REQUEST_TABLE;
    }
    
    @Override
    protected String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                CONTACT_ID_FIELD_NAME,
                EMAIL_FIELD_NAME,
                MESSAGE_FIELD_NAME,
                REQUEST_CODE_FIELD_NAME,
                TIME_SENT_FIELD_NAME,
                TIME_RECEIVED_FIELD_NAME
        };
    }

    private List<ContactRequest> retrieveContactRequestsByCriteria(String fieldName, String fieldValue) {
        Cursor contactRequestCursor = null;
        try {
            contactRequestCursor = createCursor(fieldName, fieldValue, null, null);
            return createEntityListFromCursor(contactRequestCursor);
        } finally {
            SQLiteUtils.closeCursor(contactRequestCursor);
        }
    }
}
