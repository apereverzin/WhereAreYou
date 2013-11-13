package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.PredefinedMailProperties.getPredefinedProperties;

import java.util.Properties;

import android.content.Intent;

import com.creek.accessemail.connector.mail.MailUtil;
import com.creek.whereareyou.R;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailGoogleAccountAddress_1_Activity extends EmailAccountAddress_1_Activity {
    
    @Override
    protected int getLayoutId() {
        return R.layout.email_google_account_address_1;
    }
    
    @Override
    protected Intent getNextIntent(String emailAddress) {
        Properties predefinedProps = getPredefinedProperties(emailAddress);

        bundledProps = convertPropertiesToHashMap(predefinedProps);
        bundledProps.put(PREDEFINED_PROPERTIES, TRUE);
        Intent intent = new Intent(getCurrentActivity(), EmailGoogleAccountFinish_5_Activity.class);
        gatherProperties();
        putExtrasIntoIntent(intent, bundledProps);

        return intent;
    }

    @Override
    protected String getEmailAddressText(String emailAddress) {
        return MailUtil.extractUsernameFromEmailAddress(emailAddress);
    }
    
    @Override
    protected String buildEmailAddress(String emailAddressText) {
        return emailAddressText + getString(R.string.googlemail_com);
    }
    
    @Override
    protected boolean isValid(String emailAddress) {
        return true;
    }
}
