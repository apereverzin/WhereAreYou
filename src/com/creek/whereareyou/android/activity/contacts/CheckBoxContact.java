package com.creek.whereareyou.android.activity.contacts;

import com.creek.whereareyou.android.contacts.Contact;

public class CheckBoxContact {
    private final Contact contact;
    private boolean selected = false;
    
    public CheckBoxContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
