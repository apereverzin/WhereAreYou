package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creek.whereareyou.android.activity.contacts.ContactsActivity.Mode;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
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
        Map<String, AndroidContact> contacts = new HashMap<String, AndroidContact>();
        for(CheckBoxContact checkBoxContact : contactsList) {
            if(checkBoxContact.isSelected()) {
                contacts.put(checkBoxContact.getContact().getId(), checkBoxContact.getContact());
            }
        }
        
        try {
            if (Mode.ADD_CONTACTS_TO_INFORM.equals(mode)) {
                ContactsPersistenceManager.getInstance().persistContactsToInformWhenAdding(contacts);
            } else if (Mode.ADD_CONTACTS_TO_TRACE.equals(mode)) {
                ContactsPersistenceManager.getInstance().persistContactsToTraceWhenAdding(contacts);
            } else if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode)) {
                ContactsPersistenceManager.getInstance().persistContactsToInform(contacts);
            } else if (Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
                ContactsPersistenceManager.getInstance().persistContactsToTrace(contacts);
            }
        } catch (IOException ex) {
            ActivityUtil.showException(activity, ex);
        }

        activity.setResult(RESULT_OK);
        activity.finish();
    }

}
