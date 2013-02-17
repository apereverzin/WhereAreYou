package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.android.activity.contacts.ContactsActivity.CONTACT_ACTIVITY_MODE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.ApplManager;
import com.creek.whereareyou.android.activity.map.MainMapActivity;
import com.creek.whereareyou.android.contacts.Contact;
import com.google.android.maps.Projection;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * List of contacts.
 * 
 * @author Andrey Pereverzin
 */
public class ContactsActivity extends ListActivity {
    private static final String TAG = ContactsActivity.class.getSimpleName();

    private static final int ADD_CONTACT_MENU_ITEM = Menu.FIRST;

    private static final int VIEW_CONTACT_MENU_ITEM = Menu.FIRST;
    private static final int EXCLUDE_CONTACT_MENU_ITEM = Menu.FIRST + 1;

    private static final String CONTACT_NAME = "contact_name";

    public static final String CONTACT_SELECTED = "CONTACT_SELECTED";
    public static final String CONTACT_ACTIVITY_MODE = "CONTACT_ACTIVITY_MODE";

    private static String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };

    private List<Map<String, String>> contactsList;
    private List<Contact> contactsDataList;
    private SimpleAdapter contactsListAdapter;

    private Mode mode;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate()");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        mode = (Mode) bundle.get(CONTACT_ACTIVITY_MODE);

        setActivityTitle();

        setContentView(R.layout.contacts_list);
        contactsList = new LinkedList<Map<String, String>>();

        contactsDataList = getContactsList();
        for (Contact contact : contactsDataList) {
            Map<String, String> contactMap = createMapForList(contact);
            contactsList.add(contactMap);
        }
        
        contactsListAdapter = new SimpleAdapter(getApplicationContext(), contactsList, R.layout.contact_row, new String[] { CONTACT_NAME }, new int[] { R.id.contact_name });
        setListAdapter(contactsListAdapter);
        registerForContextMenu(getListView());
        Log.d(getClass().getName(), "onCreate() finished");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(getClass().getName(), "onPrepareOptionsMenu()");
        boolean result = super.onPrepareOptionsMenu(menu);
        menu.clear();

        menu.add(0, ADD_CONTACT_MENU_ITEM, 0, R.string.menu_add_contact);

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case ADD_CONTACT_MENU_ITEM:
            Log.d(getClass().getName(), "ADD_CONTACT_MENU_ITEM");
            // contactsDataList =
            // RepositoryManager.getInstance().getTripRepository().getTrips();
            //
            // recreateTripsList(contactsDataList);
            // StringBuilder title = new
            // StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.all_trips));
            // setTitle(title);
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

    private Map<String, String> createMapForList(Contact contact) {
        Map<String, String> tripMap = new HashMap<String, String>();
        tripMap.put(CONTACT_NAME, contact.toString());
        return tripMap;
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
        if (Mode.DISPLAY_CONTACTS_TO_INFORM == mode) {
            return ApplManager.getInstance().getContactsProvider().getContactsToInform();
        } else if (Mode.DISPLAY_CONTACTS_TO_TRACE == mode) {
            return ApplManager.getInstance().getContactsProvider().getContactsToTrace();
        } else if (Mode.ADD_CONTACT_TO_INFORM == mode) {
            return ApplManager.getInstance().getContactsProvider().getContactsToAddToInform(this);
        } else /*if (Mode.ADD_CONTACT_TO_TRACE == mode) */ {
            return ApplManager.getInstance().getContactsProvider().getContactsToAddToTrace(this);
        }
    }

    private void setActivityTitle() {
        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ");

        if (Mode.DISPLAY_CONTACTS_TO_INFORM == mode) {
            title.append(getString(R.string.contacts_to_inform));
        } else if (Mode.DISPLAY_CONTACTS_TO_TRACE == mode) {
            title.append(getString(R.string.contacts_to_trace));
        } else if (Mode.ADD_CONTACT_TO_INFORM == mode) {
            title.append(getString(R.string.contacts_to_inform)).append(": ").append(R.string.add);
        } else if (Mode.ADD_CONTACT_TO_TRACE == mode) {
            title.append(getString(R.string.contacts_to_trace)).append(": ").append(R.string.add);
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
        DISPLAY_CONTACTS_TO_INFORM, DISPLAY_CONTACTS_TO_TRACE, ADD_CONTACT_TO_INFORM, ADD_CONTACT_TO_TRACE;
    }
}
