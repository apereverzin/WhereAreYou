package com.creek.whereareyou.android.activity.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.creek.whereareyou.R;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.activity.account.EmailAccountAddress_1_Activity;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.contacts.RequestResponseFactory;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * List of contacts.
 * 
 * @author Andrey Pereverzin
 */
public final class ContactsActivity extends ListActivity {
    private static final String TAG = ContactsActivity.class.getSimpleName();
    
    private static final String CONTACT_NAME = "contact_name";

    // Options menu
    private static final int EMAIL_ACCOUNT_MENU_ITEM = Menu.FIRST;

    // Context menu
    private static final int EDIT_CONTACT_DETAILS_MENU_ITEM = Menu.FIRST;
    private static final int VIEW_CONTACT_LAST_LOCATION_MENU_ITEM = Menu.FIRST + 1;
    private static final int REQUEST_CONTACT_LOCATION_MENU_ITEM = Menu.FIRST + 2;

    static final String CONTACT_SELECTED = "CONTACT_SELECTED";
    
    private List<AndroidContact> androidContactList;
    private AndroidContact selectedAndroidContact;
    
    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        ActivityUtil.setActivityTitle(this, R.string.app_name, R.string.contacts, R.string.allowed_location_requests);

        androidContactList = getAndroidContacts();

        setContentView(R.layout.contacts_list);

        lv = (ListView) findViewById(android.R.id.list);

        populateContactList();
        
        final List<Map<String, Object>> contactsList = createContactsList(androidContactList);
        SimpleAdapter contactsListAdapter = new SimpleAdapter(this, contactsList, R.layout.contact_row, new String[] { CONTACT_NAME }, new int[] {R.id.contact_name });
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
        } catch (Exception ex) {
            ActivityUtil.showException(ContactsActivity.this, ex);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EMAIL_ACCOUNT_MENU_ITEM:
            // return startNewActivity(EmailAccountAddress_1_Activity.class);
            return startNewActivity(EmailAccountAddress_1_Activity.class);
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onCreateContextMenu() called");
        super.onCreateContextMenu(menu, v, menuInfo);
        final AndroidContact contactSelected = androidContactList.get((int) ((AdapterContextMenuInfo) menuInfo).id);
        menu.add(0, EDIT_CONTACT_DETAILS_MENU_ITEM, 0, R.string.menu_edit_contact_details);
        menu.add(0, VIEW_CONTACT_LAST_LOCATION_MENU_ITEM, 0, R.string.menu_view_last_contact_location);
        menu.add(0, REQUEST_CONTACT_LOCATION_MENU_ITEM, 0, R.string.menu_request_contact_location).setEnabled(isContactEmailDefined(contactSelected));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final AndroidContact androidContact = androidContactList.get((int) info.id);
        selectedAndroidContact = androidContact;
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT_SELECTED, androidContact);
        switch (item.getItemId()) {
        case EDIT_CONTACT_DETAILS_MENU_ITEM:
            try {
                Log.d(TAG, "EDIT_CONTACT_DETAILS_MENU_ITEM: " + androidContact.getContactId());
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
                ContactRequest contactRequest = RequestResponseFactory.getInstance().createContactLocationRequest(androidContact);

                SQLiteRepositoryManager.getInstance().openDatabase();
                ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();

                contactRequestRepository.create(contactRequest);
                return true;
            } finally {
                SQLiteRepositoryManager.getInstance().closeDatabase();
            }
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void populateContactList() {
        Cursor cursor = getContacts();
        String[] fields = new String[] { ContactsContract.Data.DISPLAY_NAME };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_row, cursor, fields, new int[] { R.id.contact_name });
        lv.setAdapter(adapter);
    }

    private boolean isContactEmailDefined(AndroidContact androidContact) {
        return androidContact.getContactData().getContactEmail() != null && !"".equals(androidContact.getContactData().getContactEmail());
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
            ActivityUtil.showException(ContactsActivity.this, ex);
        }
        return true;
    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
        //String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return managedQuery(uri, projection, null, selectionArgs, sortOrder);
    }

    private List<AndroidContact> getAndroidContacts() {
        try {
            return ContactsPersistenceManager.getInstance().retrieveContacts(this);
        } catch (IOException ex) {
            ActivityUtil.showException(ContactsActivity.this, ex);
            return new ArrayList<AndroidContact>();
        }
    }

    private List<Map<String, Object>> createContactsList(List<AndroidContact> contactsDataList) {
        final List<Map<String, Object>> contactsList = new LinkedList<Map<String, Object>>();

        for (AndroidContact contact : contactsDataList) {
            Map<String, Object> contactMap = createMapForList(contact);
            contactsList.add(contactMap);
        }
        
        return contactsList;
    }

    private Map<String, Object> createMapForList(AndroidContact contact) {
        Map<String, Object> contactMap = new HashMap<String, Object>();
        contactMap.put(CONTACT_NAME, contact.getDisplayName());
        return contactMap;
    }
}
