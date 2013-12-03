package com.creek.whereareyou.android.activity.contacts;

import static com.creek.accessemail.connector.mail.MailUtil.extractUsernameFromEmailAddress;
import static com.creek.accessemail.connector.mail.MailUtil.isEmailAddressValid;
import static com.creek.whereareyou.android.util.Util.buildGooglemailAddress;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;

import android.util.Log;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactGoogleAccountDetailActivity extends ContactDetailActivity implements OnItemSelectedListener {
    private static final String TAG = ContactGoogleAccountDetailActivity.class.getSimpleName();
    
    @Override
    protected int getLayoutId() {
        return R.layout.contact_google_account_detail;
    }
    
    @Override
    protected String getEmailAddressText(AndroidContact androidContact) {
        return extractUsernameFromEmailAddress(androidContact.getContactData().getContactEmail());
    }
    
    @Override
    protected String buildEmailAddress(String emailAddressText) {
        return buildGooglemailAddress(this, emailAddressText);
    }
}
