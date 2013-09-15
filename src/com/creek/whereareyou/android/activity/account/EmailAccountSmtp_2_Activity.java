package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_AUTH_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_STARTTLS_ENABLE_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.whereareyou.android.activity.account.CheckMode.SMTP;

import com.creek.whereareyou.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountSmtp_2_Activity extends AbstractEmailAccountActivity {
    private static final String TAG = EmailAccountSmtp_2_Activity.class.getSimpleName();

    private EditText smtpHostText;
    private EditText smtpPortText;
    private CheckBox smtpAuthCheck;
    private CheckBox smtpStartTlsEnableCheck;
    private EditText smtpSocketFactoryPortText;
    private EditText smtpSocketFactoryClassText;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        smtpHostText = (EditText) findViewById(R.id.mail_smtp_host);
        smtpPortText = (EditText) findViewById(R.id.mail_smtp_port);
        smtpAuthCheck = (CheckBox) findViewById(R.id.mail_smtp_auth);
        smtpSocketFactoryClassText = (EditText) findViewById(R.id.mail_smtp_socket_factory_class);
        smtpSocketFactoryPortText = (EditText) findViewById(R.id.mail_smtp_socket_factory_port);
        smtpStartTlsEnableCheck = (CheckBox) findViewById(R.id.mail_smtp_start_tls_enable);

        smtpHostText.setText(bundledProps.get(MAIL_SMTP_HOST_PROPERTY));
        smtpPortText.setText(bundledProps.get(MAIL_SMTP_PORT_PROPERTY));
        smtpAuthCheck.setChecked(TRUE.equalsIgnoreCase(bundledProps.get(MAIL_SMTP_AUTH_PROPERTY)));
        smtpStartTlsEnableCheck.setChecked(TRUE.equalsIgnoreCase(bundledProps.get(MAIL_SMTP_STARTTLS_ENABLE_PROPERTY)));
        smtpSocketFactoryClassText.setText(bundledProps.get(MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY));
        smtpSocketFactoryPortText.setText(bundledProps.get(MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY));

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----backButton clicked");
                // TODO go back
                step(EmailAccountSmtp_2_Activity.this, EmailAccountAddress_1_Activity.class);                        
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                getCheckResult(EmailAccountSmtp_2_Activity.this, SMTP);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----nextButton clicked");
                step(EmailAccountSmtp_2_Activity.this, EmailAccountPop3_4_Activity.class);
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
    }

    @Override
    protected void gatherProperties() {
        gatherTextFieldValue(bundledProps, MAIL_SMTP_HOST_PROPERTY, smtpHostText);
        gatherTextFieldValue(bundledProps, MAIL_SMTP_PORT_PROPERTY, smtpPortText);
        gatherBooleanValue(bundledProps, MAIL_SMTP_AUTH_PROPERTY, smtpAuthCheck);
        gatherBooleanValue(bundledProps, MAIL_SMTP_STARTTLS_ENABLE_PROPERTY, smtpStartTlsEnableCheck);
        gatherTextFieldValue(bundledProps, MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY, smtpSocketFactoryClassText);
        gatherTextFieldValue(bundledProps, MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY, smtpSocketFactoryPortText);
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.email_account_smpt_2;
    }
}
