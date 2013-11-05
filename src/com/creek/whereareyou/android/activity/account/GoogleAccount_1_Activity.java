package com.creek.whereareyou.android.activity.account;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.accessemail.connector.mail.PredefinedMailProperties.getPredefinedProperties;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;

import java.util.List;
import java.util.Properties;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;

import com.creek.accessemail.connector.mail.MailUtil;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;

/**
 * 
 * @author Andrey Pereverzin
 */
public class GoogleAccount_1_Activity extends EmailAccountAddress_1_Activity {
    private static final String GMAIL = "gmail";
    private static final String GOOGLE_MAIL = "googlemail";
    
    @Override
    public void onCreate(Bundle icicle) {
        if (getGoogleAccount() == null) {
            try {
                Properties props = getMailProperties();
                if (!isGoogleAccount(props)) {
                    makeText(this, R.string.google_account_not_found, LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                showException(this, ex);
            }
        }
        
        super.onCreate(icicle);
    }

    @Override
    protected String extractEmailAddressText() {
        String username = bundledProps.get(MAIL_USERNAME_PROPERTY);
        return isGoogleAccount(username) ? username : "";
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.email_account_address_1;
    }
    
    @Override
    protected int[] getTitleComponents() {
        return new int[]{R.string.app_name, R.string.google_account_activity_name};
    }
    
    @Override
    protected Intent getNextIntent(String emailAddress) {
        Properties predefinedProps = getPredefinedProperties(emailAddress);

        bundledProps = convertPropertiesToHashMap(predefinedProps);
        bundledProps.put(PREDEFINED_PROPERTIES, TRUE);
        Intent intent = new Intent(getCurrentActivity(), GoogleAccountFinish_5_Activity.class);
        gatherProperties();
        putExtrasIntoIntent(intent, bundledProps);

        return intent;
    }
    
    @Override
    protected boolean isValid(String emailAddress) {
        if (isGoogleAccount(emailAddress)) {
            return true;
        }
        
        makeText(this, R.string.not_google_account, LENGTH_LONG).show();
        return false;
    }

    private boolean isGoogleAccount(Properties mailProperties) {
        String emailAddress = mailProperties.getProperty(MAIL_USERNAME_PROPERTY);
        return isGoogleAccount(emailAddress);
    }

    private boolean isGoogleAccount(String emailAddress) {
        String hostname = MailUtil.extractHostnameFromEmailAddress(emailAddress);
        return hostname.contains(GMAIL) || hostname.contains(GOOGLE_MAIL);
    }
    
    private String getGoogleAccount() {
        List<Account> googleAccounts = GoogleAccountProvider.getInstance().getEmailAccount(this);
        
        if (googleAccounts.size() > 0) {
            return googleAccounts.get(0).name;
        }
        
        return null;
    }
}
