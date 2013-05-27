package com.creek.whereareyou.android.contacts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactDataRepository;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteDbManager;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.repository.ContactDataRepository;

/**
 * 
 * @author andreypereverzin
 */
public class ContactsPersistenceManager {
    private static final ContactsPersistenceManager instance = new ContactsPersistenceManager();

    private final AndroidContactsProvider androidContactsProvider = new AndroidContactsProvider();
    private ContactDataRepository contactDataRepository;

    private ContactsPersistenceManager() {
        //
    }

    public static ContactsPersistenceManager getInstance() {
        return instance;
    }

    public void initialise(Context ctx) {
        SQLiteDbManager.getInstance().initialise(ctx);
        contactDataRepository = new SQLiteContactDataRepository(SQLiteDbManager.getInstance().getDatabase());
    }

    public void persistContacts(Map<String, AndroidContact> androidContacts) throws IOException {
        for (String contactId: androidContacts.keySet()) {
            ContactData contact = contactDataRepository.getContactDataByContactId(contactId);
            if (contact == null) {
                AndroidContact androidContact = androidContacts.get(contactId);
                contact = createContactData(androidContact);
                contactDataRepository.create(contact);
            } else {
                contactDataRepository.update(contact);
            }
        }
    }

    public ContactData retrieveContactDataByContactId(String contactId) {
        return contactDataRepository.getContactDataByContactId(contactId);
    }
    
    public List<AndroidContact> retrieveContacts(Context context) throws IOException {
        Map<String, AndroidContact> existingContacts = retrievePersistedContacts();
        return combineContactsLists(context, existingContacts);
    }

    private Map<String, AndroidContact> retrievePersistedContacts() throws IOException {
        List<ContactData> contacts = contactDataRepository.getAllContactData();
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
    }
    
    private ContactData createContactData(AndroidContact androidContact) {
        ContactData contactData = new ContactData();
        
        ContactCompoundId contactCompoundId = new ContactCompoundId(androidContact.getId(), androidContact.getEmail());
        contactData.setContactCompoundId(contactCompoundId);
        contactData.setDisplayName(androidContact.getDisplayName());
        return contactData;
    }
}
