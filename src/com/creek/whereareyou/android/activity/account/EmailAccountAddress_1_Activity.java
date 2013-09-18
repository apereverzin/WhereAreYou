package com.creek.whereareyou.android.activity.account;

import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import static com.creek.accessemail.connector.mail.PredefinedMailProperties.getPredefinedProperties;

import com.creek.whereareyou.R;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;

import static com.creek.whereareyou.android.activity.account.CheckMode.SMTP_AND_POP3;

import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountAddress_1_Activity extends AbstractEmailAccountActivity {
    private static final String TAG = EmailAccountAddress_1_Activity.class.getSimpleName();

    private EditText emailAddressText;
    private EditText passwordText;
    
    @Override
    protected void onCreate(Bundle icicle) {
        Log.d(TAG, "-------onCreate()");
        super.onCreate(icicle);
        emailAddressText = (EditText) findViewById(R.id.mail_username);
        passwordText = (EditText) findViewById(R.id.mail_password);
        backButton.setVisibility(View.INVISIBLE);

        //final Account googleAccount = GoogleAccountProvider.getInstance().getEmailAccount(this);

        if (bundledProps == null) {
            buildBundledProperties(getIntent().getExtras());
        }
        
        Log.d(TAG, "-------bundledProps: " + bundledProps);
        emailAddressText.setText(bundledProps.get(MAIL_USERNAME_PROPERTY));
        passwordText.setText(bundledProps.get(MAIL_PASSWORD_PROPERTY));

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String emailAddress = emailAddressText.getText().toString().toLowerCase(Locale.getDefault());
                
                HashMap<String, String> props;
                Properties predefinedProps = getPredefinedProperties(emailAddress);
                if (predefinedProps == null) {
                    props = bundledProps;
                } else {
                    props = convertPropertiesToHashMap(predefinedProps);
                }
                props.put(MAIL_USERNAME_PROPERTY, emailAddress);
                props.put(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                Intent intent = new Intent(EmailAccountAddress_1_Activity.this, CheckEmailResultActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putSerializable(MAIL_PROPERTIES, props);
                bundle.putSerializable(CHECK_MODE, SMTP_AND_POP3);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String emailAddress = emailAddressText.getText().toString().toLowerCase(Locale.getDefault());

                    //Properties fullProps = getPredefinedProperties(googleAccount.name);
                    Properties predefinedProps = getPredefinedProperties(emailAddress);

                    Intent intent;
                    if (predefinedProps != null) {
                        // TODO check if predefined properties are already redefined
                        bundledProps = convertPropertiesToHashMap(predefinedProps);
                        bundledProps.put(PREDEFINED_PROPERTIES, TRUE);
                        intent = new Intent(EmailAccountAddress_1_Activity.this, EmailAccountFinish_5_Activity.class);
                        gatherProperties();
                        putExtrasIntoIntent(intent, bundledProps);
                        startActivity(intent);
                        setResult(RESULT_OK);
                    } else {
                        bundledProps.put(PREDEFINED_PROPERTIES, FALSE);
                        intent = new Intent(EmailAccountAddress_1_Activity.this, EmailAccountSmtp_2_Activity.class);                        
                        putBundledPropertiesIntoIntentAndStartActivity(intent);
                    }
                } catch (Exception ex) {
                    showException(EmailAccountAddress_1_Activity.this, ex);
                }
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
    }

    @Override
    protected void gatherProperties() {
        gatherTextFieldValue(bundledProps, MAIL_USERNAME_PROPERTY, emailAddressText);
        gatherTextFieldValue(bundledProps, MAIL_PASSWORD_PROPERTY, passwordText);
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.email_account_address_1;
    }

    @SuppressWarnings("unchecked")
    private void buildBundledProperties(Bundle extras) {
        try {
            if (extras != null) {
                bundledProps = (HashMap<String, String>) extras.get(MAIL_PROPERTIES);
            }

            if (bundledProps == null) {
                Properties mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
                bundledProps = convertPropertiesToHashMap(mailProps);
            }
        } catch (Exception ex) {
            showException(this, ex);
        }
    }
}
