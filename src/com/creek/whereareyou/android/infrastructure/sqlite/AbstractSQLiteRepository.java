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
    protected SQLiteDatabase whereAreYouDb;

    static final String ID_FIELD_NAME = "_id";
    static final String ANDR_CONT_ID_FIELD_NAME = "andr_cont_id";
    static final String EMAIL_FIELD_NAME = "email";

    static final String CONTACT_DATA_TABLE = "contact_data";
    static final String CONTACT_REQUEST_TABLE = "contact_request";
    static final String CONTACT_RESPONSE_TABLE = "contact_response";
    static final String CONTACT_LOCATION_TABLE = "contact_location";

    static final int INT_FALSE = 0;
    static final int INT_TRUE = 1;
    private static final String AND = " AND ";
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

    public void setDatabase(SQLiteDatabase _whereAreYouDb) {
        this.whereAreYouDb = _whereAreYouDb;
    }

    @Override
    public T create(T entity) {
        try {
            ContentValues values = getContentValues(entity);
            Log.d(TAG, "--------------create: " + entity);
            long id = whereAreYouDb.insert(getTableName(), null, values);
            entity.setId(id);
            Log.d(TAG, "Created: " + entity.getId());
            Log.d(TAG, "--------------Created: " + entity.getId());
        } catch (Throwable ex) {
            ActivityUtil.printStackTrace(TAG, ex);
        }
        return entity;
    }

    @Override
    public boolean update(T entity) {
        Log.d(TAG, "update: " + entity.getId());
        ContentValues values = getContentValues(entity);
        return whereAreYouDb.update(getTableName(), values, createWhereCriteria(ID_FIELD_NAME, entity.getId()), null) > 0;
    }

    @Override
    public boolean delete(int id) {
        Log.d(TAG, "delete: " + id);
        return whereAreYouDb.delete(getTableName(), createWhereCriteria(ID_FIELD_NAME, id), null) > 0;
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
        Log.d(TAG, "createCursor(): " + criteria);
        Log.d(TAG, "------------createCursor(): " + criteria);
        Log.d(TAG, "------------createCursor(): " + getTableName());
        Log.d(TAG, "------------createCursor() selectionArgs: " + selectionArgs);
        Log.d(TAG, "------------createCursor() orderBy: " + orderBy);
        try {
            Log.d(TAG, "------------createCursor: 1");
            Log.d(TAG, "------------isOpen: " + whereAreYouDb.isOpen());
            Cursor c = whereAreYouDb.query(getTableName(), getFieldNames(), criteria, selectionArgs, null, null, orderBy);
            Log.d(TAG, "------------createCursor count: " + c.getCount());
            return c;
        } catch (Throwable t) {
            t.printStackTrace();
            Log.d(TAG, "------------createCursor: 2");
            return null;
        }
    }

    protected String createWhereAndCriteria(ComparisonClause[] criteria) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < criteria.length; i++) {
            criteria[i].appendToStringBuilder(sb);
            if (i < criteria.length - 1) {
                sb.append(AND);
            }
        }

        return sb.toString();
    }

    protected List<T> createEntityListFromCursor(Cursor cursor) {
        Log.d(TAG, "createEntityListFromCursor()");
        Log.d(TAG, "--------------createEntityListFromCursor()");
        List<T> entityList = new ArrayList<T>();

        while (cursor.moveToNext()) {
            T data = createEntity(cursor);
            Log.d(TAG, "data: " + data.getId());
            entityList.add(data);
        }

        return entityList;
    }

    protected T createEntityFromCursor(Cursor cursor) {
        Log.d(TAG, "createEntityFromCursor()");
        Log.d(TAG, "--------------createEntityFromCursor()");
        cursor.moveToFirst();

        if (cursor.isFirst()) {
            return createEntity(cursor);
        }

        return null;
    }

    protected T createEntity(Cursor cursor) {
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
        Log.d(TAG, "getContentValues()");
        ContentValues values = new ContentValues();
        //values.put(ID_FIELD_NAME, t.getId());
        values.put(ANDR_CONT_ID_FIELD_NAME, t.getContactCompoundId().getContactId());
        values.put(EMAIL_FIELD_NAME, t.getContactCompoundId().getContactEmail());
        return values;
    }

    private String createWhereCriteria(String fieldName, String fieldValue) {
        return String.format("%s='%s'", fieldName, fieldValue);
    }

    private String createWhereCriteria(String fieldName, long fieldValue) {
        return String.format("%s=%s", fieldName, fieldValue);
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
