package com.creek.whereareyou;

import com.creek.whereareyou.android.FileProvider;
import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.contacts.ContactsProvider;
import com.creek.whereareyou.android.locationprovider.LocationProvider;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ApplManager {
    private static ApplManager instance = new ApplManager();
    private final LocationProvider locationProvider = new LocationProvider();
    private final GoogleAccountProvider accountProvider = new GoogleAccountProvider();
    private final MailAccountPropertiesProvider mailAccountPropertiesProvider = new MailAccountPropertiesProvider();
    private final FileProvider fileProvider = new FileProvider();
    private final ContactsProvider contactsProvider = new ContactsProvider();
    private final ContactsPersistenceManager contactsPersistentManager = new ContactsPersistenceManager();

    private ApplManager() {
        //
    }

    public static ApplManager getInstance() {
        return instance;
    }

    public LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public GoogleAccountProvider getAccountProvider() {
        return accountProvider;
    }

    public MailAccountPropertiesProvider getMailAccountPropertiesProvider() {
        return mailAccountPropertiesProvider;
    }

    public FileProvider getFileProvider() {
        return fileProvider;
    }

    public ContactsProvider getContactsProvider() {
        return contactsProvider;
    }

    public ContactsPersistenceManager getContactsPersistentManager() {
        return contactsPersistentManager;
    }
}
