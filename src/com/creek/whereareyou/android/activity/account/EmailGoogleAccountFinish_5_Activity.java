package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;

import com.creek.whereareyou.R;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailGoogleAccountFinish_5_Activity extends EmailAccountFinish_5_Activity {
    @Override
    protected int getLayoutId() {
        return R.layout.email_google_account_finish_5;
    }
    
    @Override
    protected int[] getTitleComponents() {
        return new int[] { R.string.finish_activity_name };
    }
    
    @Override
    protected String buildMailSettingsDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.google_account)).append(": " ).append(bundledProps.get(MAIL_USERNAME_PROPERTY));
        
        return sb.toString();
    }
    
    @Override
    protected void goBack() {
        step(EmailGoogleAccountFinish_5_Activity.this, EmailGoogleAccountAddress_1_Activity.class); 
        finish();
    }
}
