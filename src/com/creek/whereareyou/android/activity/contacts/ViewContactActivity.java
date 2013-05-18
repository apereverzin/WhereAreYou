package com.creek.whereareyou.android.activity.contacts;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyou.android.util.Util;
import com.creek.whereareyoumodel.domain.ContactData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ViewContactActivity extends Activity {
    private TextView displayNameText;
    private EditText emailText;
    private TextView emailTextView;
    private CheckBox locationRequestsAllowedCheckBox;
    private TextView lastSentLocationRequestText;
    private TextView lastReceivedLocationResponseText;
    private TextView lastReceivedLocationRequestText;
    private TextView lastSentLocationResponseText;
    private Button sendLocationRequestButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.contact_view);
        displayNameText = (TextView) findViewById(R.id.contact_display_name);
        emailText = (EditText) findViewById(R.id.contact_email);
        emailTextView = (TextView) findViewById(R.id.contact_email_view);
        locationRequestsAllowedCheckBox = (CheckBox) findViewById(R.id.location_requests_allowed);
        lastSentLocationRequestText = (TextView) findViewById(R.id.last_sent_location_request);
        lastReceivedLocationResponseText = (TextView) findViewById(R.id.last_received_location_response);
        lastReceivedLocationRequestText = (TextView) findViewById(R.id.last_received_location_request);
        lastSentLocationResponseText = (TextView) findViewById(R.id.last_sent_location_response);
        sendLocationRequestButton = (Button) findViewById(R.id.request_location);
        saveButton = (Button) findViewById(R.id.contact_save);

        Bundle extras = getIntent().getExtras();

        final AndroidContact contact = (AndroidContact) extras.get(ContactsActivity.CONTACT_SELECTED);
        ContactData contactData = ContactsPersistenceManager.getInstance().retrieveContactDataByContactId(contact.getId());
        
        Log.d(getClass().getName(), "onCreate() " + contact);

        displayNameText.setText(contact.getDisplayName());
        if (contact.getEmail() == null) {
            emailTextView.setVisibility(View.INVISIBLE);
            emailText.setText(contactData.getEmail());
            saveButton.setVisibility(View.INVISIBLE);
        } else {
            emailText.setVisibility(View.INVISIBLE);
            emailTextView.setText(contact.getEmail());
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
        locationRequestsAllowedCheckBox.setChecked(contact.isLocationRequestAllowed());
        lastSentLocationRequestText.setText(Util.formatDateTime(contactData.getSentLocationRequestTimestamp()));
        lastReceivedLocationResponseText.setText(Util.formatDateTime(contactData.getReceivedLocationRequestTimestamp()));
        lastReceivedLocationRequestText.setText(Util.formatDateTime(contactData.getReceivedLocationRequestTimestamp()));
        lastSentLocationResponseText.setText(Util.formatDateTime(contactData.getSentLocationRequestTimestamp()));

        sendLocationRequestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        StringBuilder title = ActivityUtil.buildActivityTitle(this, R.string.app_name, R.string.view_contact);
        setTitle(title);
    }
}
