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
    }
    
    public List<AndroidContact> retrieveContacts(Context ctx) throws IOException {
        Log.d(TAG, "retrieveContacts()");
        
        Map<String, ContactData> existingContacts = retrievePersistedContacts();
        return combineContactsLists(ctx, existingContacts);
    }

    private Map<String, ContactData> retrievePersistedContacts() throws IOException {
        Log.d(TAG, "retrievePersistedContacts()");
        
            ContactDataRepository contactDataRepository = SQLiteRepositoryManager.getInstance().getContactDataRepository();

            List<ContactData> contactDataList = contactDataRepository.getAllContactData();
            Map<String, ContactData> contactDataMap = new HashMap<String, ContactData>();
            for (int i = 0; i < contactDataList.size(); i++) {
                ContactData contactData = contactDataList.get(i);
                contactDataMap.put(contactData.getContactCompoundId().getContactId(), contactData);
            }
            return contactDataMap;
    }

    private List<AndroidContact> combineContactsLists(Context context, Map<String, ContactData> persistedContacts) {
        Log.d(TAG, "combineContactsLists()");
        List<AndroidContact> allContacts = androidContactsProvider.getAllContacts(context);

        for (int i = 0; i < allContacts.size(); i++) {
            AndroidContact androidContact = allContacts.get(i);
            ContactData contactData = persistedContacts.get(androidContact.getContactId());
            androidContact.setContactData(new ContactDataDTO(androidContact.getContactId(), contactData));
       }

        return allContacts;
    }

    private ContactData createContactData(AndroidContact androidContact) {
        ContactData contactData = new ContactData();
        
        ContactCompoundId contactCompoundId = new ContactCompoundId(androidContact.getContactId(), androidContact.getContactEmail());
        contactData.setContactCompoundId(contactCompoundId);
        return contactData;
    }
}
