package com.creek.whereareyou.android.activity.contacts;

import java.util.List;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactDataDTO;
import com.creek.whereareyou.android.infrastructure.sqlite.DBFileManager;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.RequestAllowance;

import static com.creek.whereareyou.android.activity.contacts.ContactsActivity.CONTACT_SELECTED;
import static com.creek.whereareyou.android.util.ActivityUtil.buildActivityTitle;
import static com.creek.whereareyoumodel.domain.RequestAllowance.NEVER;

import android.app.Activity;
import android.content.Intent;
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
public class ContactDetailActivity extends Activity implements OnItemSelectedListener {
    private static final String TAG = ContactDetailActivity.class.getSimpleName();

    private TextView displayNameText;
    private EditText emailText;
    private Spinner locationRequestsAllowanceSpinner;
    private Button saveButton;
    private IndexedContactData indexedContact;
    private ContactDataDTO contactDataDto;
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.contact_details);
        displayNameText = (TextView) findViewById(R.id.contact_display_name);
        emailText = (EditText) findViewById(R.id.contact_email);
        locationRequestsAllowanceSpinner = (Spinner) findViewById(R.id.location_requests_allowance);
        saveButton = (Button) findViewById(R.id.contact_save);

        Bundle extras = getIntent().getExtras();

        indexedContact = (IndexedContactData) extras.get(CONTACT_SELECTED);
        AndroidContact androidContact = indexedContact.getContactData().getAndroidContact();
        Log.d(TAG, "-----------androidContact: " + androidContact);
        contactDataDto = androidContact.getContactData();
        if (contactDataDto == null) {
            contactDataDto = new ContactDataDTO(androidContact.getContactId());
            contactDataDto.setId(-1L);
            contactDataDto.setContactEmail(androidContact.getContactData().getContactEmail());
            contactDataDto.setRequestAllowanceCode(NEVER.getCode());
            contactDataDto.setAllowanceDate(0L);
        }
        
        Log.d(TAG, "onCreate() " + androidContact);

        displayNameText.setText(androidContact.getDisplayName());
        emailText.setText(androidContact.getContactData().getContactEmail());
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = emailText.getText().toString();
                Log.d(TAG, "-----------email: " + email);
                contactDataDto.setContactEmail(email);
                ContactData contactData = contactDataDto.toContactData();
                
                Log.d(TAG, "-----------contactData: " + contactData);
                if (contactData.getId() == -1L) {
                    contactData = (ContactData) SQLiteRepositoryManager.getInstance().getContactDataRepository().create(contactData);
                    Log.d(TAG, "-----------create() " + contactData);
                } else {
                    SQLiteRepositoryManager.getInstance().getContactDataRepository().update(contactData);
                    Log.d(TAG, "-----------update() " + contactData);
                }
                
                List<ContactData> contactDataList = SQLiteRepositoryManager.getInstance().getContactDataRepository().getAllContactData();
                DBFileManager dbFileManager = new DBFileManager();
                dbFileManager.reserveContactData(contactDataList);

                finishActivity();
            }
        });
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.requests_allowances_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationRequestsAllowanceSpinner.setAdapter(adapter);
        locationRequestsAllowanceSpinner.setSelection(contactDataDto.getRequestAllowanceCode());
        locationRequestsAllowanceSpinner.setOnItemSelectedListener(this);

        StringBuilder title = buildActivityTitle(this, R.string.app_name, R.string.edit_contact);
        setTitle(title);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d(TAG, "-----------pos " + pos);
        contactDataDto.setRequestAllowanceCode(RequestAllowance.values()[pos].getCode());
        Log.d(TAG, "-----------onItemSelected " + contactDataDto);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    
    private void finishActivity() {
        Log.d(TAG, "finishActivity()");
        Log.d(TAG, "-----------finishActivity()");
        Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        indexedContact.getContactData().getAndroidContact().getContactData().setContactEmail(contactDataDto.getContactEmail());
        indexedContact.getContactData().setRequestAllowance(RequestAllowance.getRequestAllowance(contactDataDto.getRequestAllowanceCode()));
        bundle.putSerializable(CONTACT_SELECTED, indexedContact);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
