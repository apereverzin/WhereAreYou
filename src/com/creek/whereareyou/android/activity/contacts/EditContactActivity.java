package com.creek.whereareyou.android.activity.contacts;

import java.io.FilePermission;
import java.util.List;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.FileProvider;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.RequestAllowance;
import static com.creek.whereareyoumodel.domain.RequestAllowance.NEVER;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EditContactActivity extends Activity implements OnItemSelectedListener {
    private static final String TAG = EditContactActivity.class.getSimpleName();

    private TextView displayNameText;
    private EditText emailText;
    private Spinner locationRequestsAllowanceSpinner;
    private Button saveButton;
    private ContactData contactData;
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.contact_edit);
        displayNameText = (TextView) findViewById(R.id.contact_display_name);
        emailText = (EditText) findViewById(R.id.contact_email);
        locationRequestsAllowanceSpinner = (Spinner) findViewById(R.id.location_requests_allowance);
        saveButton = (Button) findViewById(R.id.contact_save);

        Bundle extras = getIntent().getExtras();

        final AndroidContact contact = (AndroidContact) extras.get(ContactsActivity.CONTACT_SELECTED);
        
        contactData = ContactsPersistenceManager.getInstance().retrieveContactDataByContactId(contact.getId());
        contactData = SQLiteRepositoryManager.getInstance().getContactDataRepository().getContactDataByContactId(contact.getId());
        Log.d(TAG, "-----------contactData1 " + contactData);
        if (contactData == null) {
            contactData = SQLiteRepositoryManager.getInstance().getContactDataRepository().getContactDataByEmail(contact.getEmail());
            Log.d(TAG, "-----------contactData2 " + contactData);
            if (contactData == null) {
                contactData = new ContactData();
                ContactCompoundId contactCompoundId = new ContactCompoundId(contact.getId(), contact.getEmail());
                contactData.setContactCompoundId(contactCompoundId);
                contactData.setRequestAllowance(NEVER);
                Log.d(TAG, "-----------contactData3 " + contactData);
            }
        }
        
        Log.d(TAG, "onCreate() " + contact);
        Log.d(TAG, "-----------contact " + contact);
        Log.d(TAG, "-----------contactData " + contactData);

        displayNameText.setText(contact.getDisplayName());
        emailText.setText(contact.getEmail());
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----------save() " + contactData);
                SQLiteRepositoryManager.getInstance().getContactDataRepository().update(contactData);
                List<ContactData> contactDataList = SQLiteRepositoryManager.getInstance().getContactDataRepository().getAllContactData();
                //FileProvider
                setResult(RESULT_OK);
                finish();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.requests_allowances_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationRequestsAllowanceSpinner.setAdapter(adapter);
        locationRequestsAllowanceSpinner.setSelection(contactData.getRequestAllowance().getCode());
        locationRequestsAllowanceSpinner.setOnItemSelectedListener(this);

        StringBuilder title = ActivityUtil.buildActivityTitle(this, R.string.app_name, R.string.edit_contact);
        setTitle(title);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d(TAG, "-----------pos " + pos);
        contactData.setRequestAllowance(RequestAllowance.values()[pos]);
        Log.d(TAG, "-----------onItemSelected " + contactData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
