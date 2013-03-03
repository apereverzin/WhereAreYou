package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;

import com.creek.whereareyou.android.ApplManager;
import com.creek.whereareyou.android.contacts.Contact;

/**
 * 
 * @author andreypereverzin
 */
public class ContactsPersistenceManager {
    private static final String CONTACTS_TO_INFORM_FILE_NAME = "/Android/whereareyoucontactstoinform";
    private static final String CONTACTS_TO_TRACE_FILE_NAME = "/Android/whereareyoucontactstotrace";

    public List<Contact> retrieveContactsToInform(Context context) throws IOException {
        return retrieveContacts(context, CONTACTS_TO_INFORM_FILE_NAME);
    }
    
    public void persistContactsToInform(List<Contact> contacts) throws IOException {
        persistContacts(CONTACTS_TO_INFORM_FILE_NAME, contacts);
   }

    public List<Contact> retrieveContactsToTrace(Context context) throws IOException {
        return retrieveContacts(context, CONTACTS_TO_TRACE_FILE_NAME);
    }
    
    public void persistContactsToTrace(List<Contact> contacts) throws IOException {
        persistContacts(CONTACTS_TO_TRACE_FILE_NAME, contacts);
    }
    
    public List<Contact> retrieveContacts(Context context, String fileName) throws IOException {
        String contactIds = getContactIds(fileName);
        return convertCSVStringToContacts(context, contactIds);
    }
    
    private void persistContacts(String fileName, List<Contact> contacts) throws IOException {
        String contactIds = convertContactsToCSVString(contacts);
        persistContactIds(fileName, contactIds);
    }
    
    private String getContactIds(String fileName) throws IOException {
        return ApplManager.getInstance().getFileProvider().getStringFromFile(fileName);
    }
    
    private void persistContactIds(String fileName, String contactIds) throws IOException {
        ApplManager.getInstance().getFileProvider().persistStringToFile(fileName, contactIds);
    }
    
    private String convertContactsToCSVString(List<Contact> contacts) {
        StringBuilder sb = new StringBuilder();
        
        for(Contact contact: contacts) {
            sb.append(contact.getId());
            sb.append(",");
        }
        
        int l = sb.lastIndexOf(",");
        if(l > 0) {
            sb.setLength(l);
        }
        
        return sb.toString();
    }
    
    private List<Contact> convertCSVStringToContacts(Context context, String contactIds) {
        List<Contact> contacts = new ArrayList<Contact>();

        StringTokenizer st = new StringTokenizer(contactIds, ",");
        while(st.hasMoreTokens()) {
            String contactId = st.nextToken();
            Contact contact = ApplManager.getInstance().getContactsProvider().getContactById(context, contactId);
            contacts.add(contact);
        }
        
        return contacts;
    }
}
