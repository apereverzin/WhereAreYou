package com.creek.whereareyou.android.contacts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactsPersistenceManager {
    private static final ContactsPersistenceManager instance = new ContactsPersistenceManager();

    private final AndroidContactsProvider androidContactsProvider = new AndroidContactsProvider();

    private ContactsPersistenceManager() {
        //
    }

    public static ContactsPersistenceManager getInstance() {
        return instance;
    }

    public void initialise(Context ctx) {
        SQLiteRepositoryManager.getInstance().initialise(ctx);
    }

    public void close() {
        SQLiteRepositoryManager.getInstance().closeDatabase();
    }

    public void persistContacts(Map<String, AndroidContact> androidContacts) throws IOException {
        for (String contactId: androidContacts.keySet()) {
            ContactData contact = SQLiteRepositoryManager.getInstance().getContactDataRepository().getContactDataByContactId(contactId);
            if (contact == null) {
                AndroidContact androidContact = androidContacts.get(contactId);
                contact = createContactData(androidContact);
                SQLiteRepositoryManager.getInstance().getContactDataRepository().create(contact);
            } else {
                SQLiteRepositoryManager.getInstance().getContactDataRepository().update(contact);
            }
        }
    }

    public ContactData retrieveContactDataByContactId(String contactId) {
        return SQLiteRepositoryManager.getInstance().getContactDataRepository().getContactDataByContactId(contactId);
    }
    
    public List<AndroidContact> retrieveContacts(Context context) throws IOException {
        Map<String, AndroidContact> existingContacts = retrievePersistedContacts();
        return combineContactsLists(context, existingContacts);
    }

    private Map<String, AndroidContact> retrievePersistedContacts() throws IOException {
        List<ContactData> contacts = SQLiteRepositoryManager.getInstance().getContactDataRepository().getAllContactData();
        Map<String, AndroidContact> androidContacts = new HashMap<String, AndroidContact>();
        for (int i = 0; i < contacts.size(); i++) {
            ContactData contact = contacts.get(i);
            AndroidContact androidContact = new AndroidContact(contact);
            androidContacts.put(androidContact.getId(), androidContact);
        }
        return androidContacts;
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
