package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;

import com.creek.whereareyou.R;

/**
 * 
 * @author Andrey Pereverzin
 */
public class GoogleAccountFinish_5_Activity extends EmailAccountFinish_5_Activity {
    @Override
    protected int getLayoutId() {
        return R.layout.google_account_finish_5;
    }
    
    protected Class<? extends AbstractEmailAccountActivity> getPreviousActivityClass() {
        return GoogleAccount_1_Activity.class;
    }
    
    @Override
    protected int[] getTitleComponents() {
        return new int[]{R.string.app_name, R.string.google_account_activity_name, R.string.finish_activity_name};
    }
    
    @Override
    protected String buildMailSettingsDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.google_account)).append(": " ).append(bundledProps.get(MAIL_USERNAME_PROPERTY));
        
        return sb.toString();
    }
    
    protected void goBack() {
        step(GoogleAccountFinish_5_Activity.this, GoogleAccount_1_Activity.class); 
        finish();
    }
}
