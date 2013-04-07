package com.creek.whereareyou.android.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

/**
 * 
 * @author andreypereverzin
 */
public class ContactsProvider {
    private static final String TAG = ContactsProvider.class.getSimpleName();

    private static String[] CONTACT_PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
    };

    public Contact getContactById(Context context, String contactId) {
        List<Contact> contacts =  retrieveContactsByIds(context, Collections.singleton(contactId));
        return contacts.size() > 0 ? contacts.get(0) : null;
    }
    
    public List<Contact> getAllContacts(Context context) {
        return retrieveContactsByIds(context, null);
    }
    
    public List<Contact> getContactsByIds(Context context, Set<String> contactIds) {
        return retrieveContactsByIds(context, contactIds);
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
    
    public List<String> getContactEmails(Context context, String contactId) {
        List<String> emails = new ArrayList<String>();
        Cursor emailsCursor = context.getContentResolver().query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null);
        while (emailsCursor.moveToNext()) {
            String emailIdOfContact = emailsCursor.getString(emailsCursor.getColumnIndex(Email.DATA));
            int emailType = emailsCursor.getInt(emailsCursor.getColumnIndex(Phone.TYPE));
            Log.d(TAG, "-----emailIdOfContact: " + emailIdOfContact);
            Log.d(TAG, "-----emailType: " + emailType);
        }
        emailsCursor.close();
        return emails;
    }
    
    private List<Contact> retrieveContactsByIds(Context context, Set<String> contactIds) {
        ContentResolver cr = context.getContentResolver();
        
        Cursor cursor = null;
        
        try {
            cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, CONTACT_PROJECTION, 
                    contactIds == null ? null : ContactsContract.Contacts._ID + buildINClause(contactIds), 
                    null, ContactsContract.Contacts.DISPLAY_NAME);
        
            return getContactsList(cursor);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }
    
    private String buildINClause(Set<String> contactIds) {
        Iterator<String> iter = contactIds.iterator();
        StringBuilder sb = new StringBuilder(" IN (");
        if (iter.hasNext()) {
            sb.append(iter.next());
            while (iter.hasNext()) {
                sb.append(",").append(iter.next());
            }
        }
        return sb.append(")").toString();
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
