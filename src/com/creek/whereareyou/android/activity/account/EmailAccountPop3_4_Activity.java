package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY;

import static com.creek.whereareyou.android.activity.account.CheckMode.POP3;

import com.creek.whereareyou.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountPop3_4_Activity extends AbstractEmailAccountActivityWithButtons {
    private EditText pop3HostText;
    private EditText pop3PortText;
    private EditText pop3SocketFactoryPortText;
    private EditText pop3SocketFactoryClassText;
    private CheckBox pop3SocketFactoryFallbackCheck;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        pop3HostText = (EditText) findViewById(R.id.mail_pop3_host);
        pop3PortText = (EditText) findViewById(R.id.mail_pop3_port);
        pop3SocketFactoryPortText = (EditText) findViewById(R.id.mail_pop3_socket_factory_port);
        pop3SocketFactoryClassText = (EditText) findViewById(R.id.mail_pop3_socket_factory_class);
        pop3SocketFactoryFallbackCheck = (CheckBox) findViewById(R.id.mail_pop3_socket_factory_fallback);

        pop3HostText.setText(bundledProps.get(MAIL_POP3_HOST_PROPERTY));
        pop3PortText.setText(bundledProps.get(MAIL_POP3_PORT_PROPERTY));
        pop3SocketFactoryPortText.setText(bundledProps.get(MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY));
        pop3SocketFactoryClassText.setText(bundledProps.get(MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY));
        pop3SocketFactoryFallbackCheck.setChecked("true".equalsIgnoreCase(bundledProps.get(MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY)));
    }

    @Override
    protected void gatherProperties() {
        gatherTextFieldValue(bundledProps, MAIL_POP3_HOST_PROPERTY, pop3HostText);
        gatherTextFieldValue(bundledProps, MAIL_POP3_PORT_PROPERTY, pop3PortText);
        gatherTextFieldValue(bundledProps, MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY, pop3SocketFactoryPortText);
        gatherTextFieldValue(bundledProps, MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY, pop3SocketFactoryClassText);
        gatherBooleanValue(bundledProps, MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY, pop3SocketFactoryFallbackCheck);
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.email_account_pop3_4;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected EmailAccountPop3_4_Activity getCurrentActivity() {
        return this;
    }

    @Override
    protected Class<? extends Activity> getPreviousActivityClass() {
        return EmailAccountSmtp_2_Activity.class;
    }

    @Override
    protected Class<? extends Activity> getNextActivityClass() {
        return EmailAccountFinish_5_Activity.class;
    }

    @Override
    protected CheckMode getCheckMode() {
        return POP3;
    }
    
    @Override
    protected int[] getTitleComponents() {
        return new int[]{R.string.app_name, R.string.mail_settings_activity_name, R.string.mail_pop3_settings_activity_name};
    }
}
