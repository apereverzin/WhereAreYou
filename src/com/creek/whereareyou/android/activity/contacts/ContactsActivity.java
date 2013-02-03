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
 * List of all trips.
 * 
 * @author Andrey Pereverzin
 * 
 */
public class ContactsActivity extends ListActivity {
    private static final int RECENT_TRIPS_MENU_ITEM = Menu.FIRST;
    private static final int ALL_TRIPS_MENU_ITEM = Menu.FIRST + 1;
    private static final int NEW_TRIP_MENU_ITEM = Menu.FIRST + 2;
    private static final int ROUTES_MENU_ITEM = Menu.FIRST + 3;
    private static final int STATISTICS_MENU_ITEM = Menu.FIRST + 4;

    private static final int TRIP_DETAILS_MENU_ITEM = Menu.FIRST;
    private static final int VIEW_TRIP_MENU_ITEM = Menu.FIRST + 1;
    private static final int VIEW_ROUTE_MENU_ITEM = Menu.FIRST + 2;
    private static final int INCLUDE_INTO_NEW_ROUTE_MENU_ITEM = Menu.FIRST + 3;
    private static final int INCLUDE_INTO_EXISTING_ROUTE_MENU_ITEM = Menu.FIRST + 4;
    private static final int DELETE_TRIP_MENU_ITEM = Menu.FIRST + 5;

    private static final String CONTACT_NAME = "contact_name";

    public static final String TRIP = "TRIP";

    private List<Map<String, String>> contactsList;
    private List<Contact> contactsDataList;
    private SimpleAdapter contactsListAdapter;

    private boolean showAll = false;
    private static final int TRIPS_LIMIT = 10;

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

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        Log.d(getClass().getName(), "onPrepareOptionsMenu()");
//        boolean result = super.onPrepareOptionsMenu(menu);
//        menu.clear();
//        if (showAll) {
//            menu.add(0, RECENT_TRIPS_MENU_ITEM, 0, R.string.menu_recent_trips);
//            showAll = false;
//        } else {
//            menu.add(0, ALL_TRIPS_MENU_ITEM, 0, R.string.menu_all_trips);
//            showAll = true;
//        }
//        menu.add(0, NEW_TRIP_MENU_ITEM, 0, R.string.menu_new_trip);
//        menu.add(0, ROUTES_MENU_ITEM, 0, R.string.menu_routes);
//        menu.add(0, STATISTICS_MENU_ITEM, 0, R.string.menu_statistics);
//        return result;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//        case ALL_TRIPS_MENU_ITEM:
//            Log.d(getClass().getName(), "ALL_TRIPS_MENU_ITEM");
//            contactsDataList = RepositoryManager.getInstance().getTripRepository().getTrips();
//
//            recreateTripsList(contactsDataList);
//            StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.all_trips));
//            setTitle(title);
//            return true;
//        case RECENT_TRIPS_MENU_ITEM:
//            Log.d(getClass().getName(), "RECENT_TRIPS_MENU_ITEM");
//            contactsDataList = RepositoryManager.getInstance().getTripRepository().getTrips(TRIPS_LIMIT);
//
//            recreateTripsList(contactsDataList);
//            title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.recent_trips));
//            setTitle(title);
//            return true;
//        case NEW_TRIP_MENU_ITEM:
//            Log.d(getClass().getName(), "NEW_TRIP_MENU_ITEM");
//            Intent mapsIntent = new Intent(ContactsActivity.this, NewTripActivity.class);
//            startActivity(mapsIntent);
//            return true;
//        case ROUTES_MENU_ITEM:
//            Log.d(getClass().getName(), "ROUTES_MENU_ITEM");
//            Intent routesIntent = new Intent(ContactsActivity.this, RoutesListActivity.class);
//            startActivity(routesIntent);
//            return true;
//        case STATISTICS_MENU_ITEM:
//            Log.d(getClass().getName(), "STATISTICS_MENU_ITEM");
//            Intent statisticsIntent = new Intent(ContactsActivity.this, StatisticsRequestActivity.class);
//            startActivity(statisticsIntent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        Log.d(getClass().getName(), "onCreateContextMenu()");
//        super.onCreateContextMenu(menu, v, menuInfo);
//        final Trip tripSelected = contactsDataList.get((int) ((AdapterContextMenuInfo) menuInfo).id);
//        menu.add(0, TRIP_DETAILS_MENU_ITEM, 0, R.string.menu_trips_list_details);
//        menu.add(0, VIEW_TRIP_MENU_ITEM, 0, R.string.menu_trips_list_view);
//        if (tripSelected.getRoute() != null) {
//            menu.add(0, VIEW_ROUTE_MENU_ITEM, 0, R.string.menu_trips_route);
//        }
//        menu.add(0, INCLUDE_INTO_NEW_ROUTE_MENU_ITEM, 0, R.string.menu_trips_new_route);
//        menu.add(0, INCLUDE_INTO_EXISTING_ROUTE_MENU_ITEM, 0, R.string.menu_trips_include_into_route);
//        menu.add(0, DELETE_TRIP_MENU_ITEM, 0, R.string.menu_trips_list_delete);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//        final Trip tripSelected = contactsDataList.get((int) info.id);
//        final Bundle bundle = new Bundle();
//        bundle.putSerializable(TRIP, tripSelected);
//        switch (item.getItemId()) {
//        case TRIP_DETAILS_MENU_ITEM:
//            Log.d(getClass().getName(), "TRIP_DETAILS_MENU_ITEM");
//            Intent tripDetailsIntent = new Intent(ContactsActivity.this, TripDetailsActivity.class);
//            tripDetailsIntent.putExtras(bundle);
//            startActivity(tripDetailsIntent);
//            return true;
//        case VIEW_TRIP_MENU_ITEM:
//            Log.d(getClass().getName(), "VIEW_TRIP_MENU_ITEM");
//            Intent mapsIntent = new Intent(ContactsActivity.this, ViewTripActivity.class);
//            mapsIntent.putExtras(bundle);
//            startActivity(mapsIntent);
//            return true;
//        case VIEW_ROUTE_MENU_ITEM:
//            Log.d(getClass().getName(), "VIEW_ROUTE_MENU_ITEM");
//            return true;
//        case INCLUDE_INTO_NEW_ROUTE_MENU_ITEM:
//            Log.d(getClass().getName(), "INCLUDE_INTO_NEW_ROUTE_MENU_ITEM");
//            Intent editRouteIntent = new Intent(ContactsActivity.this, RouteEditActivity.class);
//            editRouteIntent.putExtras(bundle);
//            startActivity(editRouteIntent);
//            return true;
//        case INCLUDE_INTO_EXISTING_ROUTE_MENU_ITEM:
//            Log.d(getClass().getName(), "INCLUDE_INTO_EXISTING_ROUTE_MENU_ITEM");
//            Intent routesListIntent = new Intent(ContactsActivity.this, RoutesListActivity.class);
//            routesListIntent.putExtras(bundle);
//            startActivity(routesListIntent);
//            return true;
//        case DELETE_TRIP_MENU_ITEM:
//            Log.d(getClass().getName(), "DELETE_TRIP_MENU_ITEM");
//            new AlertDialog.Builder(this)
//             .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle(R.string.confirm).setMessage(R.string.delete_trip_confirmation).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            RepositoryManager.getInstance().getTripRepository().deleteTrip(tripSelected);
//                            int ind = contactsDataList.indexOf(tripSelected);
//                            contactsDataList.remove(tripSelected);
//                            contactsList.remove(ind);
//                            ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
//                        }
//
//                    }).setNegativeButton(R.string.no, null).show();
//            return true;
//        default:
//            return super.onContextItemSelected(item);
//        }
//    }

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
