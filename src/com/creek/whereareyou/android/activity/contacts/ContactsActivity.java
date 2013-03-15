package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.ApplManager;
import com.creek.whereareyou.android.contacts.Contact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.util.ActivityUtil;

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
import android.widget.SimpleAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * List of contacts.
 * 
 * @author Andrey Pereverzin
 */
public class ContactsActivity extends ListActivity {
    private static final String TAG = ContactsActivity.class.getSimpleName();

    private static final int ADD_CONTACTS_MENU_ITEM = Menu.FIRST;

    private static final int VIEW_CONTACT_MENU_ITEM = Menu.FIRST;
    private static final int EXCLUDE_CONTACT_MENU_ITEM = Menu.FIRST + 1;

    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_CHECK = "contact_check";

    public static final String CONTACT_SELECTED = "CONTACT_SELECTED";
    public static final String CONTACT_ACTIVITY_MODE = "CONTACT_ACTIVITY_MODE";

    private List<Contact> contactsDataList;
    private final ContactsPersistenceManager contactsPersistentManager = new ContactsPersistenceManager();

    private Mode mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate()");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        mode = (Mode) bundle.get(CONTACT_ACTIVITY_MODE);

        setActivityTitle();
        contactsDataList = getContactsList();

        setContentView(R.layout.contacts_list);

        if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode) || Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
            final List<Map<String, Object>> contactsList = new LinkedList<Map<String, Object>>();

            for (Contact contact : contactsDataList) {
                Map<String, Object> contactMap = createMapForList(contact);
                contactsList.add(contactMap);
            }

            SimpleAdapter contactsListAdapter = 
                    new SimpleAdapter(getApplicationContext(), contactsList, 
                            R.layout.contact_row, new String[] { CONTACT_NAME }, new int[] { R.id.contact_name });
            setListAdapter(contactsListAdapter);
        } else {
            final List<CheckBoxContact> contactsList = new ArrayList<CheckBoxContact>();
            
            for (Contact contact : contactsDataList) {
                CheckBoxContact checkBoxContact = new CheckBoxContact(contact);
                contactsList.add(checkBoxContact);
            }
            
            ContactListCheckBoxAdapter contactsListAdapter = new ContactListCheckBoxAdapter(this, contactsList);
            
            ListView lv = (ListView) findViewById(android.R.id.list);
            final Button saveButton = new Button(this);
            saveButton.setText(getString(R.string.save));
            lv.addFooterView(saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    List<Contact> contacts = new ArrayList<Contact>();
                    for(CheckBoxContact checkBoxContact : contactsList) {
                        if(checkBoxContact.isSelected()) {
                            contacts.add(checkBoxContact.getContact());
                        }
                    }
                    
                    try {
                        if (Mode.ADD_CONTACTS_TO_INFORM.equals(mode)) {
                            contactsPersistentManager.persistContactsToInformWhenAdding(contacts);
                        } else if (Mode.ADD_CONTACTS_TO_TRACE.equals(mode)) {
                            contactsPersistentManager.persistContactsToTraceWhenAdding(contacts);
                        } else if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode)) {
                            contactsPersistentManager.persistContactsToInform(contacts);
                        } else if (Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
                            contactsPersistentManager.persistContactsToTrace(contacts);
                        }
                    } catch (IOException ex) {
                        ActivityUtil.showException(ContactsActivity.this, ex);
                    }

                    setResult(RESULT_OK);
                    finish();
                }
            });

            setListAdapter(contactsListAdapter);
        }
        registerForContextMenu(getListView());
        Log.d(getClass().getName(), "onCreate() finished");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(getClass().getName(), "onPrepareOptionsMenu()");
        boolean result = super.onPrepareOptionsMenu(menu);
        menu.clear();

        if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode) || Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
            menu.add(0, ADD_CONTACTS_MENU_ITEM, 0, R.string.menu_add_contacts);
        }

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case ADD_CONTACTS_MENU_ITEM:
            Log.d(getClass().getName(), "ADD_CONTACTS_MENU_ITEM");
            if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode)) {
                startContactsActivity(Mode.ADD_CONTACTS_TO_INFORM);
            } else if (Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
                startContactsActivity(Mode.ADD_CONTACTS_TO_TRACE);
            }

            //recreateTripsList(contactsDataList);
            setActivityTitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // @Override
    // public void onResume() {
    // Log.d(getClass().getName(), "onResume()");
    // Trip lastCreatedTrip =
    // RepositoryManager.getInstance().getTripRepository().getLastCreatedTrip();
    // if (lastCreatedTrip != null) {
    // contactsDataList.add(0, lastCreatedTrip);
    // Map<String, String> tripMap = createTripMapForList(lastCreatedTrip);
    // contactsList.add(0, tripMap);
    // ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
    // }
    // Trip lastUpdatedTrip =
    // RepositoryManager.getInstance().getTripRepository().getLastUpdatedTrip();
    // if (lastUpdatedTrip != null) {
    // int ind = contactsDataList.indexOf(lastUpdatedTrip);
    // contactsDataList.remove(ind);
    // contactsDataList.add(ind, lastUpdatedTrip);
    // Map<String, String> tripMap = createTripMapForList(lastUpdatedTrip);
    // contactsList.remove(ind);
    // contactsList.add(ind, tripMap);
    // ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
    // }
    // super.onResume();
    // }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(getClass().getName(), "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, VIEW_CONTACT_MENU_ITEM, 0, R.string.menu_view_contact);
        menu.add(0, EXCLUDE_CONTACT_MENU_ITEM, 0, R.string.menu_exclude_contact);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final Contact contactSelected = contactsDataList.get((int) info.id);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT_SELECTED, contactSelected);
        switch (item.getItemId()) {
        case VIEW_CONTACT_MENU_ITEM:
            Log.d(getClass().getName(), "VIEW_CONTACT_MENU_ITEM");
            Intent intent = new Intent(ContactsActivity.this, EditContactActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        case EXCLUDE_CONTACT_MENU_ITEM:
            Log.d(getClass().getName(), "EXCLUDE_CONTACT_MENU_ITEM");
            // new AlertDialog.Builder(this)
            // .setIcon(android.R.drawable.ic_dialog_alert)
            // .setTitle(R.string.confirm).setMessage(R.string.delete_trip_confirmation).setPositiveButton(R.string.yes,
            // new DialogInterface.OnClickListener() {
            // public void onClick(DialogInterface dialog, int which) {
            // RepositoryManager.getInstance().getTripRepository().deleteTrip(contactSelected);
            // int ind = contactsDataList.indexOf(contactSelected);
            // contactsDataList.remove(contactSelected);
            // contactsList.remove(ind);
            // ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
            // }
            //
            // }).setNegativeButton(R.string.no, null).show();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private Map<String, Object> createMapForList(Contact contact) {
        Map<String, Object> contactMap = new HashMap<String, Object>();
        contactMap.put(CONTACT_NAME, contact.getDisplayName());
        contactMap.put(CONTACT_CHECK, Boolean.FALSE);
        return contactMap;
    }

    // private void recreateTripsList(List<Trip> contactsDataList) {
    // contactsList.clear();
    // for (Trip trip : contactsDataList) {
    // Map<String, String> tripMap = createTripMapForList(trip);
    // contactsList.add(tripMap);
    // }
    // ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
    // }

    private List<Contact> getContactsList() {
        try {
            if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode)) {
                return contactsPersistentManager.retrieveContactsToInform(this);
            } else if (Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
                return contactsPersistentManager.retrieveContactsToTrace(this);
            } else if (Mode.ADD_CONTACTS_TO_INFORM.equals(mode)) {
                return contactsPersistentManager.retrieveContactsToAddToInform(this);
            } else /* if (Mode.ADD_CONTACTS_TO_TRACE.equals(mode)) */{
                return contactsPersistentManager.retrieveContactsToAddToTrace(this);
            }
        } catch (IOException ex) {
            ActivityUtil.showException(ContactsActivity.this, ex);
            return new ArrayList<Contact>();
        }

    }

    private void setActivityTitle() {
        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ");

        if (Mode.DISPLAY_CONTACTS_TO_INFORM.equals(mode)) {
            title.append(getString(R.string.contacts_to_inform));
        } else if (Mode.DISPLAY_CONTACTS_TO_TRACE.equals(mode)) {
            title.append(getString(R.string.contacts_to_trace));
        } else if (Mode.ADD_CONTACTS_TO_INFORM.equals(mode)) {
            title.append(getString(R.string.contacts_to_inform)).append(": ").append(getString(R.string.add));
        } else if (Mode.ADD_CONTACTS_TO_TRACE.equals(mode)) {
            title.append(getString(R.string.contacts_to_trace)).append(": ").append(getString(R.string.add));
        }

        setTitle(title);
    }

    private void startContactsActivity(Mode mode) {
        Intent intent = new Intent(ContactsActivity.this, ContactsActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT_ACTIVITY_MODE, mode);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static enum Mode {
        DISPLAY_CONTACTS_TO_INFORM, DISPLAY_CONTACTS_TO_TRACE, ADD_CONTACTS_TO_INFORM, ADD_CONTACTS_TO_TRACE;
    }

}
