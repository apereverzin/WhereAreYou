package com.creek.whereareyou.android.activity.contacts;

import com.creek.whereareyou.android.contacts.AndroidContact;

public class CheckBoxContact {
    private final AndroidContact contact;
    private boolean selected = false;
    
    public CheckBoxContact(AndroidContact contact) {
        this.contact = contact;
    }

    public AndroidContact getContact() {
        return contact;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
