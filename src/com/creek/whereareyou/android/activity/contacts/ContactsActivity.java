package com.creek.whereareyou.android.activity.contacts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.ApplManager;
import com.creek.whereareyou.android.contacts.Contact;
import com.google.android.maps.Projection;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
    private static final int ADD_CONTACT_MENU_ITEM = Menu.FIRST;

    private static final int VIEW_CONTACT_MENU_ITEM = Menu.FIRST;
    private static final int EDIT_CONTACT_MENU_ITEM = Menu.FIRST + 1;
    private static final int EXCLUDE_CONTACT_MENU_ITEM = Menu.FIRST + 2;

    private static final String CONTACT_NAME = "contact_name";

    public static final String TRIP = "TRIP";

    private List<Map<String, String>> contactsList;
    private List<Contact> contactsDataList;
    private SimpleAdapter contactsListAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate()");
        super.onCreate(savedInstanceState);
        
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

        setContentView(R.layout.contacts_list);
        contactsList = new LinkedList<Map<String, String>>();
        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ");

        setTitle(title);

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
//            contactsDataList = RepositoryManager.getInstance().getTripRepository().getTrips();
//
//            recreateTripsList(contactsDataList);
//            StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.all_trips));
//            setTitle(title);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onResume() {
//        Log.d(getClass().getName(), "onResume()");
//        Trip lastCreatedTrip = RepositoryManager.getInstance().getTripRepository().getLastCreatedTrip();
//        if (lastCreatedTrip != null) {
//            contactsDataList.add(0, lastCreatedTrip);
//            Map<String, String> tripMap = createTripMapForList(lastCreatedTrip);
//            contactsList.add(0, tripMap);
//            ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
//        }
//        Trip lastUpdatedTrip = RepositoryManager.getInstance().getTripRepository().getLastUpdatedTrip();
//        if (lastUpdatedTrip != null) {
//            int ind = contactsDataList.indexOf(lastUpdatedTrip);
//            contactsDataList.remove(ind);
//            contactsDataList.add(ind, lastUpdatedTrip);
//            Map<String, String> tripMap = createTripMapForList(lastUpdatedTrip);
//            contactsList.remove(ind);
//            contactsList.add(ind, tripMap);
//            ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
//        }
//        super.onResume();
//    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(getClass().getName(), "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, VIEW_CONTACT_MENU_ITEM, 0, R.string.menu_view_contact);
        menu.add(0, EDIT_CONTACT_MENU_ITEM, 0, R.string.menu_edit_contact);
        menu.add(0, EXCLUDE_CONTACT_MENU_ITEM, 0, R.string.menu_exclude_contact);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final Contact contactSelected = contactsDataList.get((int) info.id);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(TRIP, contactSelected);
        switch (item.getItemId()) {
        case VIEW_CONTACT_MENU_ITEM:
            Log.d(getClass().getName(), "VIEW_CONTACT_MENU_ITEM");
            Intent tripDetailsIntent = new Intent(ContactsActivity.this, EditContactActivity.class);
            tripDetailsIntent.putExtras(bundle);
            startActivity(tripDetailsIntent);
            return true;
        case EDIT_CONTACT_MENU_ITEM:
            Log.d(getClass().getName(), "EDIT_CONTACT_MENU_ITEM");
            Intent mapsIntent = new Intent(ContactsActivity.this, ViewContactActivity.class);
            mapsIntent.putExtras(bundle);
            startActivity(mapsIntent);
            return true;
        case EXCLUDE_CONTACT_MENU_ITEM:
            Log.d(getClass().getName(), "EXCLUDE_CONTACT_MENU_ITEM");
//            new AlertDialog.Builder(this)
//             .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle(R.string.confirm).setMessage(R.string.delete_trip_confirmation).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            RepositoryManager.getInstance().getTripRepository().deleteTrip(contactSelected);
//                            int ind = contactsDataList.indexOf(contactSelected);
//                            contactsDataList.remove(contactSelected);
//                            contactsList.remove(ind);
//                            ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
//                        }
//
//                    }).setNegativeButton(R.string.no, null).show();
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

//    private void recreateTripsList(List<Trip> contactsDataList) {
//        contactsList.clear();
//        for (Trip trip : contactsDataList) {
//            Map<String, String> tripMap = createTripMapForList(trip);
//            contactsList.add(tripMap);
//        }
//        ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
//    }
}
