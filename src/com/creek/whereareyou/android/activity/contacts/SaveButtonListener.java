package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.ApplManager;
import com.creek.whereareyou.android.activity.contacts.ContactsActivity.Mode;
import com.creek.whereareyou.android.contacts.Contact;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.app.ListActivity;
import static android.app.Activity.RESULT_OK;
import android.view.View;

/**
 * 
 * @author andreypereverzin
 */
public class SaveButtonListener implements View.OnClickListener {
    private final ListActivity activity;
    private final List<CheckBoxContact> contactsList;
    private final Mode mode;
    
    public SaveButtonListener(ListActivity activity, List<CheckBoxContact> contactsList, Mode mode) {
        this.activity = activity;
        this.contactsList = contactsList;
        this.mode = mode;
    }

    public void onClick(View view) {
        List<Contact> contacts = new ArrayList<Contact>();
        for(CheckBoxContact checkBoxContact : contactsList) {
            if(checkBoxContact.isSelected()) {
                contacts.add(checkBoxContact.getContact());
            }
        }
        
        try {
            if (Mode.ADD_CONTACTS_TO_INFORM.equals(mode)) {
                ApplManager.getInstance().getContactsPersistentManager().persistContactsToInformWhenAdding(contacts);
            } else if (Mode.ADD_CONTACTS_TO_TRACE.equals(mode)) {
                ApplManager.getInstance().getContactsPersistentManager().persistContactsToTraceWhenAdding(contacts);
            } else if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode)) {
                ApplManager.getInstance().getContactsPersistentManager().persistContactsToInform(contacts);
            } else if (Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
                ApplManager.getInstance().getContactsPersistentManager().persistContactsToTrace(contacts);
            }
        } catch (IOException ex) {
            ActivityUtil.showException(activity, ex);
        }

        activity.setResult(RESULT_OK);
        activity.finish();
    }

}
