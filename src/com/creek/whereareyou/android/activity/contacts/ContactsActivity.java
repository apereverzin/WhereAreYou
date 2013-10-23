package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.creek.whereareyou.R;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.whereareyou.android.activity.map.MainMapActivity.RECEIVED_LOCATIONS;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;
import static com.creek.whereareyou.android.util.Util.isStringNotEmpty;

import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.activity.account.EmailAccountAddress_1_Activity;
import com.creek.whereareyou.android.activity.map.MainMapActivity;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.contacts.RequestResponseFactory;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.valueobject.SendableLocationData;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/**
 * List of contacts.
 * 
 * @author Andrey Pereverzin
 */
public final class ContactsActivity extends ListActivity {
    private static final String TAG = ContactsActivity.class.getSimpleName();
    
    // Options menu
    private static final int EMAIL_ACCOUNT_MENU_ITEM = Menu.FIRST;
    private static final int MAP_MENU_ITEM = Menu.FIRST + 1;

    // Context menu
    private static final int EDIT_CONTACT_DETAILS_MENU_ITEM = Menu.FIRST;
    private static final int VIEW_CONTACT_LAST_LOCATION_MENU_ITEM = Menu.FIRST + 1;
    private static final int REQUEST_CONTACT_LOCATION_MENU_ITEM = Menu.FIRST + 2;

    static final String CONTACT_SELECTED = "CONTACT_SELECTED";
    
    private List<AndroidContact> androidContactList;
    
    private ListView lv;
    
    private ContactsArrayAdapter contactsListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-onCreate()");
        super.onCreate(savedInstanceState);

        ActivityUtil.setActivityTitle(this, R.string.app_name, R.string.contacts);

        androidContactList = getAndroidContacts();

        setContentView(R.layout.contacts_list);

        lv = (ListView) findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                
            }
        });
        
        List<ContactData> contactDataList = new ArrayList<ContactData>();
        for (int i = 0; i < androidContactList.size(); i++) {
            contactDataList.add(new ContactData(androidContactList.get(i)));
        }
        contactsListAdapter = new ContactsArrayAdapter(this, contactDataList);
        setListAdapter(contactsListAdapter);
        
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu()");
        try {
            if (areEmailPropertiesDefined()) {
                menu.add(0, EMAIL_ACCOUNT_MENU_ITEM, 0, R.string.edit_email_account);
            } else {
                menu.add(0, EMAIL_ACCOUNT_MENU_ITEM, 0, R.string.enter_email_account);
            }
            menu.add(0, MAP_MENU_ITEM, 0, R.string.map);
        } catch (Exception ex) {
            showException(ContactsActivity.this, ex);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EMAIL_ACCOUNT_MENU_ITEM:
            return startNewActivity(EmailAccountAddress_1_Activity.class);
        case MAP_MENU_ITEM:
            Intent intent = new Intent(this, MainMapActivity.class);
            List<OwnerLocationDataMessage> locations = new ArrayList<OwnerLocationDataMessage>();
            LocationData locationData = new LocationData();
            locationData.setAccuracy(935.0F);
            locationData.setSpeed(0.0F);
            locationData.setLatitude(51.4456628);
            locationData.setLongitude(-0.1839771);
            locationData.setHasAccuracy(true);
            locationData.setHasSpeed(false);
            ContactCompoundId contactCompoundId = new ContactCompoundId("100", "andrey.pereverzin@gmail.com");
            locationData.setContactCompoundId(contactCompoundId);
            SendableLocationData sendableLocationData = new SendableLocationData(locationData);
            OwnerLocationDataMessage location = new OwnerLocationDataMessage(sendableLocationData, "andrey.pereverzin@gmail.com");
            locations.add(location);
            intent.putExtra(RECEIVED_LOCATIONS, new ArrayList<OwnerLocationDataMessage>(locations));
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-=-onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        final AndroidContact contactSelected = androidContactList.get((int) ((AdapterContextMenuInfo) menuInfo).id);
        menu.add(0, EDIT_CONTACT_DETAILS_MENU_ITEM, 0, R.string.menu_edit_contact_details);
        menu.add(0, VIEW_CONTACT_LAST_LOCATION_MENU_ITEM, 0, R.string.menu_view_last_contact_location);
        menu.add(0, REQUEST_CONTACT_LOCATION_MENU_ITEM, 0, R.string.menu_request_contact_location).setEnabled(isStringNotEmpty(contactSelected.getContactData().getContactEmail()));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final AndroidContact selectedAndroidContact = androidContactList.get((int) info.id);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT_SELECTED, selectedAndroidContact);
        switch (item.getItemId()) {
        case EDIT_CONTACT_DETAILS_MENU_ITEM:
            try {
                Log.d(TAG, "EDIT_CONTACT_DETAILS_MENU_ITEM: " + selectedAndroidContact.getContactId());
                Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
                ContactRequest contactRequest = RequestResponseFactory.getInstance().createContactLocationRequest(selectedAndroidContact);

                SQLiteRepositoryManager.getInstance().openDatabase();
                ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();

                contactRequestRepository.create(contactRequest);
                
                contactsListAdapter.updateData(selectedAndroidContact);
                contactsListAdapter.notifyDataSetChanged();
                return true;
            } finally {
                SQLiteRepositoryManager.getInstance().closeDatabase();
            }
        default:
            return super.onContextItemSelected(item);
        }
    }

    private boolean areEmailPropertiesDefined() throws CryptoException, IOException {
        Properties props = MailAccountPropertiesProvider.getInstance().getMailProperties();
        String username = props.getProperty(MAIL_USERNAME_PROPERTY);
        return username != null && username.length() > 0;
    }

    private boolean startNewActivity(Class<? extends Activity> clazz) {
        try {
            Intent intent = new Intent(this, clazz);

            startActivity(intent);
        } catch (Exception ex) {
            showException(ContactsActivity.this, ex);
        }
        return true;
    }

    private List<AndroidContact> getAndroidContacts() {
        try {
            return ContactsPersistenceManager.getInstance().retrieveContacts(this);
        } catch (IOException ex) {
            showException(ContactsActivity.this, ex);
            return new ArrayList<AndroidContact>();
        }
    }
}
