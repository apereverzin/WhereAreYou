package com.creek.whereareyou.android.contacts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.repository.ContactDataRepository;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactsPersistenceManager {
    private static final String TAG = ContactsPersistenceManager.class.getSimpleName();

    private static final ContactsPersistenceManager instance = new ContactsPersistenceManager();

    private final AndroidContactsProvider androidContactsProvider = new AndroidContactsProvider();

    private ContactsPersistenceManager() {
        //
    }

    public static ContactsPersistenceManager getInstance() {
        return instance;
    }

    public void persistContacts(Map<String, AndroidContact> androidContacts) throws IOException {
        Log.d(TAG, "persistContacts()");
        
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactDataRepository contactDataRepository = SQLiteRepositoryManager.getInstance().getContactDataRepository();

            for (String contactId : androidContacts.keySet()) {
                ContactData contact = contactDataRepository.getContactDataByContactId(contactId);
                if (contact == null) {
                    AndroidContact androidContact = androidContacts.get(contactId);
                    contact = createContactData(androidContact);
                    contactDataRepository.create(contact);
                } else {
                    contactDataRepository.update(contact);
                }
            }
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }

    public ContactData retrieveContactDataByContactId(String contactId) {
        Log.d(TAG, "retrieveContactDataByContactId(): " + contactId);
        
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactDataRepository contactDataRepository = SQLiteRepositoryManager.getInstance().getContactDataRepository();

            return contactDataRepository.getContactDataByContactId(contactId);
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }
    
    public List<AndroidContact> retrieveContacts(Context ctx) throws IOException {
        Log.d(TAG, "retrieveContacts()");
        
        Map<String, AndroidContact> existingContacts = retrievePersistedContacts();
        return combineContactsLists(ctx, existingContacts);
    }

    private Map<String, AndroidContact> retrievePersistedContacts() throws IOException {
        Log.d(TAG, "retrievePersistedContacts()");
        
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactDataRepository contactDataRepository = SQLiteRepositoryManager.getInstance().getContactDataRepository();

            List<ContactData> contacts = contactDataRepository.getAllContactData();
            Map<String, AndroidContact> androidContacts = new HashMap<String, AndroidContact>();
            for (int i = 0; i < contacts.size(); i++) {
                ContactData contact = contacts.get(i);
                AndroidContact androidContact = new AndroidContact(contact);
                androidContacts.put(androidContact.getId(), androidContact);
            }
            return androidContacts;
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }

    private List<AndroidContact> combineContactsLists(Context context, Map<String, AndroidContact> persistedContacts) {
        List<AndroidContact> allContacts = androidContactsProvider.getAllContacts(context);

        for (int i = 0; i < allContacts.size(); i++) {
            AndroidContact androidContact = allContacts.get(i);
            AndroidContact persistedContact = persistedContacts.get(androidContact.getId());
            combineContacts(androidContact, persistedContact);
        }

        return allContacts;
    }

    private void combineContacts(AndroidContact androidContact, AndroidContact persistedContact) {
        if (persistedContact != null) {
            androidContact.setEmail(persistedContact.getEmail());
            androidContact.setRequestAllowed(persistedContact.isRequestAllowed());
        }
        
        // TODO hack
        androidContact.setEmail("andrey.pereverzin@gmail.com");
    }
    
    private ContactData createContactData(AndroidContact androidContact) {
        ContactData contactData = new ContactData();
        
        ContactCompoundId contactCompoundId = new ContactCompoundId(androidContact.getId(), androidContact.getEmail());
        contactData.setContactCompoundId(contactCompoundId);
        contactData.setDisplayName(androidContact.getDisplayName());
        return contactData;
    }
}
