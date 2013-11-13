package com.creek.whereareyou.android.activity.contacts;

import com.creek.accessemail.connector.mail.MailUtil;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactGoogleAccountDetailActivity extends ContactDetailActivity implements OnItemSelectedListener {
    
    @Override
    protected int getLayoutId() {
        return R.layout.contact_google_account_detail;
    }
    
    @Override
    protected String getEmailAddressText(AndroidContact androidContact) {
        return MailUtil.extractUsernameFromEmailAddress(androidContact.getContactData().getContactEmail());
    }
    
    @Override
    protected String buildEmailAddress(String emailAddressText) {
        return emailAddressText + getString(R.string.googlemail_com);
    }
}
