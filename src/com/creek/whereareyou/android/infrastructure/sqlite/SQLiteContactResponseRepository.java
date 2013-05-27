package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;

/**
 * 
 * @author andreypereverzin
 */
public final class SQLiteContactResponseRepository extends AbstractSQLiteRepository<ContactResponse> implements ContactResponseRepository {
    private static final String TAG = SQLiteContactResponseRepository.class.getSimpleName();

    static final String CONTACT_ID_FIELD_NAME = "contact_id";
    static final String EMAIL_FIELD_NAME = "email";
    static final String MESSAGE_FIELD_NAME = "message";
    static final String RESPONSE_CODE_FIELD_NAME = "resp_code";
    static final String TIME_SENT_FIELD_NAME = "time_sent";
    static final String TIME_RECEIVED_FIELD_NAME = "time_rcvd";

    static final String CONTACT_RESPONSE_TABLE_CREATE = 
            "create table " + CONTACT_RESPONSE_TABLE 
            + " (" + ID_FIELD_NAME + " integer primary key autoincrement, " 
            + CONTACT_ID_FIELD_NAME + " text not null, "
            + EMAIL_FIELD_NAME + " text, " 
            + MESSAGE_FIELD_NAME + " text, " 
            + RESPONSE_CODE_FIELD_NAME + " integer not null, " 
            + TIME_SENT_FIELD_NAME + " real not null, " 
            + TIME_RECEIVED_FIELD_NAME + " real not null);";

    public SQLiteContactResponseRepository(SQLiteDatabase whereAreYouDb) {
        super(whereAreYouDb);
    }

    @Override
    public List<ContactResponse> getAllContactResponses() {
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
    public List<ContactResponse> getContactResponsesByContactId(String contactId) {
        Log.d(TAG, "getContactRequestsByContactId()");
        return retrieveContactResponsesByCriteria(CONTACT_ID_FIELD_NAME, contactId);
    }

    @Override
    public List<ContactResponse> getContactResponsesByEmail(String email) {
        Log.d(TAG, "getContactResponsesByEmail()");
        return retrieveContactResponsesByCriteria(EMAIL_FIELD_NAME, email);
    }

    @Override
    protected ContentValues getContentValues(ContactResponse contactResponse) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_ID_FIELD_NAME, contactResponse.getContactCompoundId().getContactId());
        values.put(EMAIL_FIELD_NAME, contactResponse.getContactCompoundId().getContactEmail());
        values.put(MESSAGE_FIELD_NAME, contactResponse.getMessage());
        values.put(RESPONSE_CODE_FIELD_NAME, contactResponse.getResponseCode());
        values.put(TIME_SENT_FIELD_NAME, contactResponse.getTimeSent());
        values.put(TIME_RECEIVED_FIELD_NAME, contactResponse.getTimeReceived());
        return values;
    }

    @Override
    protected ContactResponse createEntityFromCursor(Cursor contactResponseCursor) {
        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setId(contactResponseCursor.getInt(0));
        ContactCompoundId contactCompoundId = new ContactCompoundId(contactResponseCursor.getString(1), contactResponseCursor.getString(2));
        contactResponse.setContactCompoundId(contactCompoundId);
        contactResponse.setMessage(contactResponseCursor.getString(3));
        contactResponse.setResponseCode(contactResponseCursor.getInt(4));
        contactResponse.setTimeSent(contactResponseCursor.getLong(5));
        contactResponse.setTimeReceived(contactResponseCursor.getLong(6));
        return contactResponse;
    }
    
    @Override
    protected String getTableName() {
        return CONTACT_RESPONSE_TABLE;
    }
    
    @Override
    protected String[] getFieldNames() {
        return new String[] {ID_FIELD_NAME,
                EMAIL_FIELD_NAME,
                CONTACT_ID_FIELD_NAME,
                MESSAGE_FIELD_NAME,
                RESPONSE_CODE_FIELD_NAME,
                TIME_SENT_FIELD_NAME,
                TIME_RECEIVED_FIELD_NAME
        };
    }

    private List<ContactResponse> retrieveContactResponsesByCriteria(String fieldName, String fieldValue) {
        Cursor contactResponseCursor = null;
        try {
            contactResponseCursor = createCursor(fieldName, fieldValue, null, null);
            return createEntityListFromCursor(contactResponseCursor);
        } finally {
            SQLiteUtils.closeCursor(contactResponseCursor);
        }
    }
}
