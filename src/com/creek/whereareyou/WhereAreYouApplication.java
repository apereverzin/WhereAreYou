package com.creek.whereareyou;

import com.creek.whereareyou.android.activity.contacts.ContactsActivity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class WhereAreYouApplication extends Application {
    private static final String TAG = WhereAreYouApplication.class.getSimpleName();
    private static SQLiteDatabase whereAreYouDatabase;
    
    private static ContactsActivity contactsActivity;
    
    @Override
    public final void onCreate() {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onCreate");
        
        super.onCreate();
      
        whereAreYouDatabase = SQLiteRepositoryManager.getInstance().createDatabaseIfDoesNotExist();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onTerminate");
        
        if (whereAreYouDatabase != null && whereAreYouDatabase.isOpen()) {
            whereAreYouDatabase.close();
        }

        super.onTerminate();
    }

    public static void setDatabase(SQLiteDatabase _whereAreYouDatabase) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-setDatabase: " + _whereAreYouDatabase.isOpen());
        whereAreYouDatabase = _whereAreYouDatabase;
    }
    
    public static SQLiteDatabase getDatabase() {
        return whereAreYouDatabase;
    }
    
    public static void registerContactActivity(ContactsActivity _contactActivity) {
        contactsActivity = _contactActivity;
    }
    
    public static ContactsActivity getContactsActivity() {
        return contactsActivity;
    }
}
