package com.creek.whereareyou.android.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;

import com.creek.whereareyou.android.ApplManager;

/**
 * 
 * @author andreypereverzin
 */
public class ContactsPersistenceManager {
    private static final String CONTACTS_TO_INFORM_FILE_NAME = "/Android/whereareyoucontactstoinform.txt";
    private static final String CONTACTS_TO_TRACE_FILE_NAME = "/Android/whereareyoucontactstotrace.txt";
    private static final String CONTACTS_SEPARATOR = "|";
    private static final String CONTACT_DETAILS_SEPARATOR = ",";
    private static final String DISABLED = "0";
    private static final String ENABLED = "1";

    public List<Contact> retrieveContactsToInform(Context context) throws IOException {
        return retrieveContacts(context, CONTACTS_TO_INFORM_FILE_NAME);
    }
    
    public void persistContactsToInformWhenAdding(List<Contact> contacts) throws IOException {
        String existingContactsString = getContactIds(CONTACTS_TO_INFORM_FILE_NAME);
        persistContacts(CONTACTS_TO_INFORM_FILE_NAME, new StringBuilder(existingContactsString), contacts);
    }

    public void persistContactsToInform(List<Contact> contacts) throws IOException {
        persistContacts(CONTACTS_TO_INFORM_FILE_NAME, new StringBuilder(""), contacts);
    }

    public List<Contact> retrieveContactsToTrace(Context context) throws IOException {
        return retrieveContacts(context, CONTACTS_TO_TRACE_FILE_NAME);
    }
    
    public void persistContactsToTraceWhenAdding(List<Contact> contacts) throws IOException {
        String existingContactsString = getContactIds(CONTACTS_TO_TRACE_FILE_NAME);
        persistContacts(CONTACTS_TO_TRACE_FILE_NAME, new StringBuilder(existingContactsString), contacts);
    }
    
    public void persistContactsToTrace(List<Contact> contacts) throws IOException {
        persistContacts(CONTACTS_TO_TRACE_FILE_NAME, new StringBuilder(""), contacts);
    }
    
    public List<Contact> retrieveContacts(Context context, String fileName) throws IOException {
        String contactIds = getContactIds(fileName);
        return convertSeparatedStringToContacts(context, contactIds);
    }
    
    public List<Contact> retrieveContactsToAddToTrace(Context context) throws IOException {
        List<Contact> existingContacts = retrieveContactsToTrace(context);
        return subtractContactsList(context, existingContacts);
    }
    
    public List<Contact> retrieveContactsToAddToInform(Context context) throws IOException {
        List<Contact> existingContacts = retrieveContactsToInform(context);
        return subtractContactsList(context, existingContacts);
    }
    
    private void persistContacts(String fileName, StringBuilder separatedExistingContactIds, List<Contact> contacts) throws IOException {
        StringBuilder separatedContactIds = convertContactsToSeparatedString(contacts);
        
        if(separatedExistingContactIds.length() > 0) {
            separatedExistingContactIds.append(CONTACTS_SEPARATOR);
        }
        separatedExistingContactIds.append(separatedContactIds);
        
        ApplManager.getInstance().getFileProvider().persistStringToFile(fileName, separatedExistingContactIds.toString());
    }
    
    private String getContactIds(String fileName) throws IOException {
        return ApplManager.getInstance().getFileProvider().getStringFromFile(fileName);
    }
    
    private StringBuilder convertContactsToSeparatedString(List<Contact> contacts) {
        StringBuilder sb = new StringBuilder();
        
        for (Contact contact: contacts) {
            sb.append(convertContactDetailsToSeparatedString(contact));
            sb.append(CONTACTS_SEPARATOR);
        }
        
        int l = sb.lastIndexOf(CONTACTS_SEPARATOR);
        if (l > 0) {
            sb.setLength(l);
        }
        
        return sb;
    }
    
    private String convertContactDetailsToSeparatedString(Contact contact) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(contact.getId());
        sb.append(CONTACT_DETAILS_SEPARATOR);
        sb.append(contact.isEnabled() ? ENABLED : DISABLED);

        return sb.toString();
    }
    
    private List<Contact> convertSeparatedStringToContacts(Context context, String contactsString) {
        List<Contact> contacts = new ArrayList<Contact>();
        Contact convertedContact;

        StringTokenizer st = new StringTokenizer(contactsString, CONTACTS_SEPARATOR);
        while (st.hasMoreTokens()) {
            convertedContact = convertSeparatedStringToContactDetails(st.nextToken());
            Contact contact = ApplManager.getInstance().getContactsProvider().getContactById(context, convertedContact.getId());
            if(contact != null) {
                contact.setEnabled(convertedContact.isEnabled());
                contacts.add(contact);
            }
        }
        
        return contacts;
    }
    
    private Contact convertSeparatedStringToContactDetails(String contactDetailsString) {
        StringTokenizer st = new StringTokenizer(contactDetailsString, CONTACT_DETAILS_SEPARATOR);
        
        Contact contact = new Contact(st.nextToken());
        if(st.hasMoreTokens()) {
            contact.setEnabled(ENABLED.equals(st.nextToken()));
        }
        
        return contact;
    }
    
    private void addContactsList(List<Contact> existingContacts, List<Contact> contactsToAdd) {
        existingContacts.addAll(contactsToAdd);
    }
    
    private List<Contact> subtractContactsList(Context context, List<Contact> existingContacts) {
        List<Contact> allContacts = ApplManager.getInstance().getContactsProvider().getAllContacts(context);
        
        allContacts.removeAll(existingContacts);
        
        return allContacts;
    }
}
