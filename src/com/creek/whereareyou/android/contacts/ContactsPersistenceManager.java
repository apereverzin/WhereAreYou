package com.creek.whereareyou.android.contacts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactRepository;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteDbManager;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.repository.ContactRepository;

/**
 * 
 * @author andreypereverzin
 */
public class ContactsPersistenceManager {
    private static final ContactsPersistenceManager instance = new ContactsPersistenceManager();

    private final AndroidContactsProvider androidContactsProvider = new AndroidContactsProvider();
    private ContactRepository contactRepository;

    private ContactsPersistenceManager() {
        //
    }

    public static ContactsPersistenceManager getInstance() {
        return instance;
    }

    public void initialise(Context ctx) {
        SQLiteDbManager.getInstance().initialise(ctx);
        contactRepository = new SQLiteContactRepository(SQLiteDbManager.getInstance().getDatabase());
    }

    public void persistContacts(Map<String, AndroidContact> androidContacts) throws IOException {
        for (String contactId: androidContacts.keySet()) {
            ContactData contact = contactRepository.getContactDataByContactId(contactId);
            if (contact == null) {
                AndroidContact androidContact = androidContacts.get(contactId);
                contact = createContactData(androidContact);
                contact.setLocationRequestAllowed(true);
                contactRepository.createContactData(contact);
            } else {
                contact.setLocationRequestAllowed(true);
                contactRepository.updateContactData(contact);
            }
        }
    }

    public ContactData retrieveContactDataByContactId(String contactId) {
        return contactRepository.getContactDataByContactId(contactId);
    }
    
    public List<AndroidContact> retrieveContacts(Context context) throws IOException {
        Map<String, AndroidContact> existingContacts = retrievePersistedContacts();
        return combineContactsLists(context, existingContacts);
    }

    private Map<String, AndroidContact> retrievePersistedContacts() throws IOException {
        List<ContactData> contacts = contactRepository.getAllContactData();
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
            androidContact.setLocationRequestAllowed(persistedContact.isLocationRequestAllowed());
            androidContact.setLocationRequestAgreed(persistedContact.isLocationRequestAgreed());
        }
    }
    
    private ContactData createContactData(AndroidContact androidContact) {
        ContactData contactData = new ContactData();
        
        contactData.setContactId(androidContact.getId());
        contactData.setEmail(androidContact.getEmail());
        contactData.setDisplayName(androidContact.getDisplayName());
        return contactData;
    }
}
