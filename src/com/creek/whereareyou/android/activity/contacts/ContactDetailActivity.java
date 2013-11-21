package com.creek.whereareyou.android.activity.contacts;

import java.util.List;
import java.util.Locale;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactDataDTO;
import com.creek.whereareyou.android.infrastructure.sqlite.DBFileManager;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.RequestAllowance;

import static com.creek.whereareyou.android.activity.contacts.ContactsActivity.CONTACT_SELECTED;
import static com.creek.whereareyou.android.util.ActivityUtil.setActivityTitle;
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
    private EditText emailAddressText;
    private Spinner locationRequestsAllowanceSpinner;
    private Button saveButton;
    private IndexedContactData indexedContact;
    private ContactDataDTO contactDataDto;
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(getLayoutId());
        displayNameText = (TextView) findViewById(R.id.contact_display_name);
        emailAddressText = (EditText) findViewById(R.id.contact_email);
        locationRequestsAllowanceSpinner = (Spinner) findViewById(R.id.location_requests_allowance);
        saveButton = (Button) findViewById(R.id.contact_save);

        Bundle extras = getIntent().getExtras();

        indexedContact = (IndexedContactData) extras.get(CONTACT_SELECTED);
        AndroidContact androidContact = indexedContact.getContactData().getAndroidContact();
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
        emailAddressText.setText(getEmailAddressText(androidContact));
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String emailAddress = emailAddressText.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=emailAddress: " + emailAddress);
                String email = buildEmailAddress(emailAddress);
                Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=email: " + email);
                contactDataDto.setContactEmail(email);
                ContactData contactData = contactDataDto.toContactData();
                
                if (contactData.getId() == -1L) {
                    contactData = (ContactData) SQLiteRepositoryManager.getInstance().getContactDataRepository().create(contactData);
                } else {
                    SQLiteRepositoryManager.getInstance().getContactDataRepository().update(contactData);
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

        setActivityTitle(this, R.string.edit_contact);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        contactDataDto.setRequestAllowanceCode(RequestAllowance.values()[pos].getCode());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    
    protected int getLayoutId() {
        return R.layout.contact_detail;
    }
    
    protected String getEmailAddressText(AndroidContact androidContact) {
        return androidContact.getContactData().getContactEmail();
    }
    
    protected String buildEmailAddress(String emailAddressText) {
        return emailAddressText;
    }

    private void finishActivity() {
        Log.d(TAG, "finishActivity()");
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
