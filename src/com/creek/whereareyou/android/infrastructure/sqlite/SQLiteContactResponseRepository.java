package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.domain.sendable.ResponseCode;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteContactResponseRepository extends AbstractRequestResponseRepository<ContactResponseEntity> implements ContactResponseRepository<ContactResponseEntity> {
    private static final String TAG = SQLiteContactResponseRepository.class.getSimpleName();

    static final String RESPONSE_CODE_FIELD_NAME = "response_code";
    static final String TYPE_FIELD_NAME = "type";
    static final String REQUEST_ID_FIELD_NAME = "request_id";
    static final String LOCATION_ID_FIELD_NAME = "location_id";
    
    public static final int NORMAL_RESPONSE_TYPE = 0;
    public static final int LOCATION_RESPONSE_TYPE = 1;

    private final String[] fieldNames = new String[] {
            RESPONSE_CODE_FIELD_NAME,
            TYPE_FIELD_NAME,
            REQUEST_ID_FIELD_NAME,
            LOCATION_ID_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "int not null", 
            "int not null", 
            "int not null", 
            "int not null"
        };
       
    @Override
    protected int getNumberOfFields() {
        return super.getNumberOfFields() + fieldNames.length;
    }
    
    @Override
    protected String[] getFieldNames() {
        return  Util.concatArrays(super.getFieldNames(), fieldNames);
    }
    
    @Override
    protected String[] getFieldTypes() {
        return Util.concatArrays(super.getFieldTypes(), fieldTypes);
    }

    @Override
    public final List<ContactResponse> getAllContactResponses() {
        Log.d(TAG, "getAllContactResponses()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            List<ContactResponseEntity> entities = createEntityListFromCursor(contactDataCursor);
            return convertContactResponses(entities);
        } finally {
            SQLiteUtils.closeCursor(contactDataCursor);
        }
    }

    @Override
    public final List<ContactResponse> getContactResponsesByContactId(String contactId) {
        Log.d(TAG, "getContactRequestsByContactId()");
        List<ContactResponseEntity> entities = retrieveEntitiesByCriteria(ANDR_CONT_ID_FIELD_NAME, contactId);
        return convertContactResponses(entities);
    }

    @Override
    public final List<ContactResponse> getContactResponsesByEmail(String email) {
        Log.d(TAG, "getContactResponsesByEmail()");
        List<ContactResponseEntity> entities = retrieveEntitiesByCriteria(EMAIL_FIELD_NAME, email);
        return convertContactResponses(entities);
    }

    @Override
    public final List<ContactResponse> getUnsentContactResponses() {
        Log.d(TAG, "getUnsentContactResponses()");
        List<ContactResponseEntity> entities = getUnsent();
        return convertContactResponses(entities);
    }

    @Override
    public List<String> getEmailAddressesForResponsesEverReceived() {
        List<String> l = new ArrayList<String>();
        
        return l;
    }

    @Override
    public List<String> getEmailAddressesForResponsesEverSent() {
        List<String> l = new ArrayList<String>();
        
        return l;
    }
    
    @Override
    protected final ContactResponseEntity createEntityInstance() {
        return new ContactResponseEntity();
    }
    
    @Override
    protected final ContentValues getContentValues(ContactResponseEntity contactResponse) {
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(contactResponse);
        values.put(RESPONSE_CODE_FIELD_NAME, contactResponse.getResponseCode().getCode());
        values.put(TYPE_FIELD_NAME, contactResponse.getType());
        values.put(REQUEST_ID_FIELD_NAME, contactResponse.getRequestId());
        values.put(LOCATION_ID_FIELD_NAME, contactResponse.getLocationId());
        return values;
    }
    
    @Override
    protected final ContactResponseEntity createEntity(Cursor cursor) {
        ContactResponseEntity contactResponse = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        int code = cursor.getInt(numberOfFields++);
        contactResponse.setResponseCode(ResponseCode.getResponseCode(code));
        contactResponse.setType(cursor.getInt(numberOfFields++));
        contactResponse.setRequestId(cursor.getInt(numberOfFields++));
        contactResponse.setLocationDataId(cursor.getInt(numberOfFields));
        Log.d(TAG, "createEntity(): " + contactResponse);
        return contactResponse;
    }
    
    @Override
    protected final String getTableName() {
        return CONTACT_RESPONSE_TABLE;
    }
    
    private List<ContactResponse> convertContactResponses(List<ContactResponseEntity> entities) {
        List<ContactResponse> responses = new ArrayList<ContactResponse>();
        for (int i = 0; i < entities.size(); i++) {
            responses.add(entities.get(i));
        }
        return responses;
    }
}
