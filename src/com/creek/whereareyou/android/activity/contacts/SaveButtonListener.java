package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.app.ListActivity;
import static android.app.Activity.RESULT_OK;
import android.view.View;

/**
 * 
 * @author Andrey Pereverzin
 */
public class SaveButtonListener implements View.OnClickListener {
    private final ListActivity activity;
    private final List<CheckBoxContact> contactsList;
    
    public SaveButtonListener(ListActivity activity, List<CheckBoxContact> contactsList) {
        this.activity = activity;
        this.contactsList = contactsList;
    }

    public void onClick(View view) {
        Map<String, AndroidContact> contacts = new HashMap<String, AndroidContact>();
        for(CheckBoxContact checkBoxContact : contactsList) {
            if(checkBoxContact.isSelected()) {
                contacts.put(checkBoxContact.getContact().getContactId(), checkBoxContact.getContact());
            }
        }
        
        try {
            ContactsPersistenceManager.getInstance().persistContacts(contacts);
        } catch (IOException ex) {
            ActivityUtil.showException(activity, ex);
        }

        activity.setResult(RESULT_OK);
        activity.finish();
    }

}
