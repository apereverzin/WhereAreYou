package com.creek.whereareyou.android.contacts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;

/**
 * 
 * @author andreypereverzin
 */
public class ContactsProvider {
    private static final String CONTACTS_TO_INFORM_FILE_PATH = "/Android/contactstoinform.json";
    private static final String CONTACTS_TO_MONITOR_FILE_PATH = "/Android/contactstomonitor.json";

    private static String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
    };

    public Contact getContactById(Context context, String contactId) {
        ContentResolver cr = context.getContentResolver();
        
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, 
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { contactId }, null);

        if (cursor.moveToNext()) {
            return buildContact(cursor);
        }

        return null;
    }
    
    public List<Contact> getAllContacts(Context context) {
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = null;
        
        try {
            cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, null, null, null);

            return getContactsList(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    public List<Contact> getContactsByIds(Context context, List<String> contactIds) {
        ContentResolver cr = context.getContentResolver();
        
        Cursor cursor = null;
        
        try {
            cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, 
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", (String[])contactIds.toArray(), null);
        
            return getContactsList(cursor);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }
    
    public List<String> getGoogleEmailsByContactId(Context context, String contactId) {
        ContentResolver cr = context.getContentResolver();
        
        Cursor cursor = null;
        
        try {
            List<String> emails = new ArrayList<String>();
            
            cursor = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null);

            while (cursor.moveToNext()) {
                String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                // String emailType = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                emails.add(email);
            }
            
            return emails;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    public List<Contact> getContactsToInform() {
        List<Contact> contacts = new ArrayList<Contact>();
        
        return contacts;
    }
    
    public void saveContactsToInform(List<Contact> contacts) {
        
    }
    
    public List<Contact> getContactsToMonitor() {
        List<Contact> contacts = new ArrayList<Contact>();
        
        return contacts;
    }
    
    public void saveContactsToMonitor(List<Contact> contacts) {
        
    }
    
    private List<Contact> getContactsList(Cursor cursor) {
        List<Contact> contacts = new ArrayList<Contact>();

        while (cursor.moveToNext()) {
            contacts.add(buildContact(cursor));
        }
        
        return contacts;
    }
    
    private Contact buildContact(Cursor cursor) {
        String contactId = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        Contact contact = new Contact(contactId);
        contact.setDisplayName(contactName);
        return contact;
    }
}