package com.creek.whereareyou.manager;

import java.io.IOException;

import com.creek.accessemail.connector.mail.MailConnector;
import com.creek.whereareyou.android.FileProvider;
import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.contacts.ContactsProvider;
import com.creek.whereareyou.android.locationprovider.LocationProvider;
import com.creek.whereareyou.android.util.CryptoException;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class ApplManager {
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

    public GoogleAccountProvider getGoogleAccountProvider() {
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
    
    public MailConnector getMailConnector() throws ManagerException {
        try {
            return new MailConnector(mailAccountPropertiesProvider.getMailProperties());
        } catch(IOException ex) {
            throw new ManagerException(ex);
        } catch(CryptoException ex) {
            throw new ManagerException(ex);
        }
    }
}
