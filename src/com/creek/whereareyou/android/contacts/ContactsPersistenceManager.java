package com.creek.whereareyou.android.contacts;

import java.io.IOException;
import java.util.ArrayList;
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

    public List<AndroidContact> retrieveContactsToInform() throws IOException {
        // TODO Add getContactsToInform() method
        List<ContactData> contacts = contactRepository.getAllContactData();
        List<AndroidContact> androidContacts = new ArrayList<AndroidContact>();
        for (int i = 0; i < contacts.size(); i++) {
            ContactData contact = contacts.get(i);
            if (contact.isLocationRequestAllowed()) {
                AndroidContact androidContact = new AndroidContact(contact);
                androidContacts.add(androidContact);
            }
        }
        return androidContacts;
    }

    public void persistContactsToInformWhenAdding(Map<String, AndroidContact> androidContacts) throws IOException {
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

    public void persistContactsToInform(Map<String, AndroidContact> androidContacts) throws IOException {
        List<ContactData> contacts = contactRepository.getAllContactData();
        for (ContactData contact: contacts) {
            if (!androidContacts.containsKey(contact.getContactId())) {
                contact.setLocationRequestAllowed(false);
                contactRepository.updateContactData(contact);
            }
        }
    }

    public List<AndroidContact> retrieveContactsToTrace() throws IOException {
        // TODO Add getContactsToTrace() method
        List<ContactData> contacts = contactRepository.getAllContactData();
        List<AndroidContact> androidContacts = new ArrayList<AndroidContact>();
        for (int i = 0; i < contacts.size(); i++) {
            ContactData contact = contacts.get(i);
            if (contact.isLocationRequestAgreed()) {
                AndroidContact androidContact = new AndroidContact(contact);
                androidContacts.add(androidContact);
            }
        }
        return androidContacts;
    }

    public void persistContactsToTraceWhenAdding(Map<String, AndroidContact> androidContacts) throws IOException {
        for (String contactId: androidContacts.keySet()) {
            ContactData contact = contactRepository.getContactDataByContactId(contactId);
            if (contact == null) {
                AndroidContact androidContact = androidContacts.get(contactId);
                contact = createContactData(androidContact);
                contact.setLocationRequestAgreed(true);
                contactRepository.createContactData(contact);
            } else {
                contact.setLocationRequestAgreed(true);
                contactRepository.updateContactData(contact);
            }
        }
    }

    public void persistContactsToTrace(Map<String, AndroidContact> androidContacts) throws IOException {
        List<ContactData> contacts = contactRepository.getAllContactData();
        for (ContactData contact: contacts) {
            if (!androidContacts.containsKey(contact.getContactId())) {
                contact.setLocationRequestAgreed(false);
                contactRepository.updateContactData(contact);
            }
        }
    }

    public List<AndroidContact> retrieveContactsToAddToTrace(Context context) throws IOException {
        List<AndroidContact> existingContacts = retrieveContactsToTrace();
        return subtractContactsList(context, existingContacts);
    }

    public List<AndroidContact> retrieveContactsToAddToInform(Context context) throws IOException {
        List<AndroidContact> existingContacts = retrieveContactsToInform();
        return subtractContactsList(context, existingContacts);
    }

    private List<AndroidContact> subtractContactsList(Context context, List<AndroidContact> existingContacts) {
        List<AndroidContact> allContacts = androidContactsProvider.getAllContacts(context);

        allContacts.removeAll(existingContacts);

        return allContacts;
    }
    
    private ContactData createContactData(AndroidContact androidContact) {
        ContactData contactData = new ContactData();
        
        contactData.setContactId(androidContact.getId());
        contactData.setEmail(androidContact.getEmail());
        contactData.setDisplayName(androidContact.getDisplayName());
        return contactData;
    }
}
