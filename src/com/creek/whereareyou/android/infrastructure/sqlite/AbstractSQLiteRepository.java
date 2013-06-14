package com.creek.whereareyou.android.infrastructure.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.Identifiable;
import com.creek.whereareyoumodel.repository.IdentifiableRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractSQLiteRepository<T extends Identifiable> implements IdentifiableRepository<T> {
    private static final String TAG = AbstractSQLiteRepository.class.getSimpleName();
    protected final SQLiteDatabase whereAreYouDb;

    static final String ID_FIELD_NAME = "_id";    
    static final String ANDR_CONT_ID_FIELD_NAME = "andr_cont_id";
    static final String EMAIL_FIELD_NAME = "email";
    
    static final String CONTACT_DATA_TABLE = "contact_data";
    static final String CONTACT_REQUEST_TABLE = "contact_request";
    static final String CONTACT_RESPONSE_TABLE = "contact_response";
    static final String CONTACT_LOCATION_TABLE = "contact_location";
    
    static final int FALSE = 0;
    static final int TRUE = 1;
    public static final int UNDEFINED_INT = -1;
    public static final long UNDEFINED_LONG = -1L;
    
    static final String TABLE_CREATE = "create table %s (%s);";

    private final String[] fieldNames = new String[] {
            ID_FIELD_NAME,
            ANDR_CONT_ID_FIELD_NAME,
            EMAIL_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "integer primary key autoincrement",
            "text",
            "text"
        };
            
    public AbstractSQLiteRepository(SQLiteDatabase whereAreYouDb) {
        this.whereAreYouDb = whereAreYouDb;
    }
    
    @Override
    public T create(T entity) {
        ContentValues values = getContentValues(entity);
        try {
            long id = whereAreYouDb.insert(getTableName(), null, values);
            entity.setId((int) id);
            Log.d(TAG, "Created: " + entity.getId());
        } catch (Throwable ex) {
            ActivityUtil.printStackTrace(TAG, ex);
        }
        return entity;
    }

    @Override
    public boolean update(T entity) {
        Log.d(TAG, "update: " + entity.getId());
        ContentValues values = getContentValues(entity);
        return whereAreYouDb.update(getTableName(), values, createWhereCriteria(ID_FIELD_NAME, Integer.toString(entity.getId())), null) > 0;
    }

    @Override
    public boolean delete(int id) {
        Log.d(TAG, "delete: " + id);
        return whereAreYouDb.delete(getTableName(), createWhereCriteria(ID_FIELD_NAME, Integer.toString(id)), null) > 0;
    }

    protected String getCreateTableCommand() {
        String fields = buildFields();
        return String.format(TABLE_CREATE, getTableName(), fields);
    }

    protected List<T> retrieveEntitiesByCriteria(String fieldName, String fieldValue) {
        Cursor cursor = null;
        try {
            cursor = createCursor(fieldName, fieldValue, null, null);
            return createEntityListFromCursor(cursor);
        } finally {
            SQLiteUtils.closeCursor(cursor);
        }
    }

    protected Cursor createCursor(String fieldName, String fieldValue, String[] selectionArgs, String orderBy) {
        Log.d(TAG, "createCursor()");
        return createCursor(createWhereCriteria(fieldName, fieldValue), selectionArgs, orderBy);
    }
    
    protected Cursor createCursor(String criteria, String[] selectionArgs, String orderBy) {
        Log.d(TAG, "createCursor()");
        return whereAreYouDb.query(getTableName(), getFieldNames(), criteria, selectionArgs, null, null, orderBy);
    }
    
    protected String createWhereCriteria(String fieldName, String fieldValue) {
        return String.format("%s=%s", fieldName, fieldValue);
    }
    
    protected String createWhereAndCriteria(String[] criteria) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < criteria.length; i++) {
            sb.append(criteria[i]);
            if (i < criteria.length - 1) {
                sb.append(" AND ");
            }
        }
        
        return sb.toString();
    }
    
    protected List<T> createEntityListFromCursor(Cursor cursor) {
        List<T> entityList = new ArrayList<T>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                T contactData = createEntityFromCursor(cursor);
                Log.d(TAG, "contactData: " + contactData.getId());
                entityList.add(contactData);
            } while (cursor.moveToNext());
        }

        return entityList;
    }
    
    protected T createEntityFromCursor(Cursor cursor) {
        T t = createEntityInstance();
        t.setId(cursor.getInt(0));
        ContactCompoundId contactCompoundId = new ContactCompoundId(cursor.getString(1), cursor.getString(2));
        t.setContactCompoundId(contactCompoundId);
        return t;
    }
    
    protected int getNumberOfFields() {
        return fieldNames.length;
    }
    
    protected String[] getFieldNames() {
        return fieldNames;
    }
    
    protected String[] getFieldTypes() {
        return fieldTypes;
    }
    
    protected abstract T createEntityInstance();

    protected abstract String getTableName();
    
    protected ContentValues getContentValues(T t) {
        ContentValues values = new ContentValues();
        values.put(ID_FIELD_NAME, t.getId());
        values.put(ANDR_CONT_ID_FIELD_NAME, t.getContactCompoundId().getContactId());
        values.put(EMAIL_FIELD_NAME, t.getContactCompoundId().getContactEmail());
        return values;
    }
    
    private String buildFields() {
        StringBuilder sb = new StringBuilder();
        String[] fieldNames = getFieldNames();
        String[] fieldTypes = getFieldTypes();
        for (int i = 0; i < fieldNames.length; i++) {
            sb.append(fieldNames[i]).append(" ").append(fieldTypes[i]);
            if (i < fieldNames.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
