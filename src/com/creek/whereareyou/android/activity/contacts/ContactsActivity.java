package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.contacts.RequestResponseFactory;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;

/**
 * List of contacts.
 * 
 * @author Andrey Pereverzin
 */
public class ContactsActivity extends ListActivity {
    private static final String TAG = ContactsActivity.class.getSimpleName();

    private static final int EDIT_CONTACT_DETAILS_MENU_ITEM = Menu.FIRST;
    private static final int VIEW_CONTACT_LAST_LOCATION_MENU_ITEM = Menu.FIRST + 1;
    private static final int REQUEST_CONTACT_LOCATION_MENU_ITEM = Menu.FIRST + 2;

    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_CHECK = "contact_check";

    static final String CONTACT_SELECTED = "CONTACT_SELECTED";
    
    private List<AndroidContact> androidContactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        
        ActivityUtil.setActivityTitle(this, R.string.app_name, R.string.contacts, R.string.allowed_location_requests);

        androidContactList = getAndroidContacts();

        setContentView(R.layout.contacts_list);

        // final List<Map<String, Object>> contactsList =
        // createContactsList(androidContactList);
        // SimpleAdapter contactsListAdapter =
        // new SimpleAdapter(getApplicationContext(), contactsList,
        // R.layout.contact_row, new String[] { CONTACT_NAME }, new int[] {
        // R.id.contact_name });
        // setListAdapter(contactsListAdapter);

        /*----------*/
        final List<CheckBoxContact> contactsList = new ArrayList<CheckBoxContact>();
        final List<CheckBoxContact> uncheckedContactsList = new ArrayList<CheckBoxContact>();

        for (AndroidContact androidContact : androidContactList) {
            CheckBoxContact checkBoxContact = new CheckBoxContact(androidContact);
            if (checkBoxContact.isSelected()) {
                contactsList.add(checkBoxContact);
            } else {
                uncheckedContactsList.add(checkBoxContact);
            }
        }
        contactsList.addAll(uncheckedContactsList);

        ContactListCheckBoxAdapter contactsListAdapter = new ContactListCheckBoxAdapter(this, contactsList);

        ListView lv = (ListView) findViewById(android.R.id.list);
        final Button saveButton = new Button(this);
        saveButton.setText(getString(R.string.save));
        lv.addFooterView(saveButton);

        saveButton.setOnClickListener(new SaveButtonListener(this, contactsList));

        setListAdapter(contactsListAdapter);
        /*----------*/

        registerForContextMenu(getListView());

        Log.d(TAG, "onCreate() finished");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        Log.d(TAG, "-----------------------onResume()");
        ((ContactListCheckBoxAdapter) getListAdapter()).notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        final AndroidContact contactSelected = androidContactList.get((int)((AdapterContextMenuInfo)menuInfo).id);
        menu.add(0, EDIT_CONTACT_DETAILS_MENU_ITEM, 0, R.string.menu_edit_contact_details);
        menu.add(0, VIEW_CONTACT_LAST_LOCATION_MENU_ITEM, 0, R.string.menu_view_last_contact_location);
        menu.add(0, REQUEST_CONTACT_LOCATION_MENU_ITEM, 0, R.string.menu_request_contact_location).setEnabled(isContactEmailDefined(contactSelected));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final AndroidContact androidContact = androidContactList.get((int) info.id);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT_SELECTED, androidContact);
        switch (item.getItemId()) {
        case EDIT_CONTACT_DETAILS_MENU_ITEM:
            try {
                Log.d(TAG, "EDIT_CONTACT_DETAILS_MENU_ITEM: " + androidContact.getContactId());
                Intent intent = new Intent(ContactsActivity.this, EditContactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                startActivityForResult(intent, requestCode);
                return true;
            } finally {
                SQLiteRepositoryManager.getInstance().closeDatabase();
            }
        case VIEW_CONTACT_LAST_LOCATION_MENU_ITEM:
            try {
                SQLiteRepositoryManager.getInstance().openDatabase();
                Log.d(TAG, "VIEW_CONTACT_LAST_LOCATION_MENU_ITEM");
                return true;
            } finally {
                SQLiteRepositoryManager.getInstance().closeDatabase();
            }
        case REQUEST_CONTACT_LOCATION_MENU_ITEM:
            Log.d(TAG, "REQUEST_CONTACT_LOCATION_MENU_ITEM");
            try {
                ContactRequest contactRequest = RequestResponseFactory.getInstance().createContactLocationRequest(androidContact);

                SQLiteRepositoryManager.getInstance().openDatabase();
                ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
                
                Log.d(TAG, "--------------created contact location request: " + contactRequest);
                contactRequestRepository.create(contactRequest);
                Log.d(TAG, "--------------persisted contact location request: " + contactRequest);
                return true;
            } finally {
                SQLiteRepositoryManager.getInstance().closeDatabase();
            }
        default:
            return super.onContextItemSelected(item);
        }
    }

    private Map<String, Object> createMapForList(AndroidContact contact) {
        Log.d(TAG, "createMapForList: " + contact.getDisplayName());
        Map<String, Object> contactMap = new HashMap<String, Object>();
        contactMap.put(CONTACT_NAME, contact.getDisplayName());
        contactMap.put(CONTACT_CHECK, Boolean.FALSE);
        return contactMap;
    }

    private List<Map<String, Object>> createContactsList(List<AndroidContact> contactsDataList) {
        final List<Map<String, Object>> contactsList = new LinkedList<Map<String, Object>>();

        for (AndroidContact contact : contactsDataList) {
            Map<String, Object> contactMap = createMapForList(contact);
            contactsList.add(contactMap);
        }
        
        return contactsList;
    }

    private List<AndroidContact> getAndroidContacts() {
        try {
            return ContactsPersistenceManager.getInstance().retrieveContacts(this);
        } catch (IOException ex) {
            ActivityUtil.showException(ContactsActivity.this, ex);
            return new ArrayList<AndroidContact>();
        }
    }
    
    private boolean isContactEmailDefined(AndroidContact androidContact) {
        return androidContact.getContactData().getContactEmail() != null && !"".equals(androidContact.getContactData().getContactEmail());
    }
}
