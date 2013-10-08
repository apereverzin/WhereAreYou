package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_AUTH_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_STARTTLS_ENABLE_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY;

import static com.creek.whereareyou.android.activity.account.CheckMode.SMTP;
import static com.creek.whereareyou.android.util.ActivityUtil.setActivityTitle;

import com.creek.whereareyou.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountSmtp_2_Activity extends AbstractEmailAccountActivityWithButtons {
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

        setActivityTitle(this, R.string.app_name, R.string.mail_settings_activity_name, R.string.mail_smtp_settings_activity_name);
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

    @SuppressWarnings("unchecked")
    @Override
    protected EmailAccountSmtp_2_Activity getCurrentActivity() {
        return this;
    }

    @Override
    protected Class<? extends Activity> getPreviousActivityClass() {
        return EmailAccountAddress_1_Activity.class;
    }

    @Override
    protected Class<? extends Activity> getNextActivityClass() {
        return EmailAccountPop3_4_Activity.class;
    }

    @Override
    protected CheckMode getCheckMode() {
        return SMTP;
    }
}
