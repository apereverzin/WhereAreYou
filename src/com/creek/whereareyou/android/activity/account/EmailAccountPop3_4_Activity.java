package com.creek.whereareyou.android.activity.account;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.activity.account.CheckEmailResultActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountPop3_4_Activity extends AbstractEmailAccountActivity {
    private static final String TAG = EmailAccountPop3_4_Activity.class.getSimpleName();

    private EditText pop3HostText;
    private EditText pop3PortText;
    private EditText pop3SocketFactoryPortText;
    private EditText pop3SocketFactoryClassText;
    private CheckBox pop3SocketFactoryFallbackCheck;
    private Button backButton;
    private Button testButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.email_account_pop3_4);

        extractBundledProperties();

        pop3HostText = (EditText) findViewById(R.id.mail_pop3_host);
        pop3PortText = (EditText) findViewById(R.id.mail_pop3_port);
        pop3SocketFactoryPortText = (EditText) findViewById(R.id.mail_pop3_socket_factory_port);
        pop3SocketFactoryClassText = (EditText) findViewById(R.id.mail_pop3_socket_factory_class);
        pop3SocketFactoryFallbackCheck = (CheckBox) findViewById(R.id.mail_pop3_socket_factory_fallback);
        backButton = (Button) findViewById(R.id.mail_properties_button_back);
        testButton = (Button) findViewById(R.id.mail_properties_button_test);
        nextButton = (Button) findViewById(R.id.mail_properties_button_next);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pop3HostText.setText(bundledProps.get(MAIL_POP3_HOST_PROPERTY));
        pop3PortText.setText(bundledProps.get(MAIL_POP3_PORT_PROPERTY));
        pop3SocketFactoryPortText.setText(bundledProps.get(MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY));
        pop3SocketFactoryClassText.setText(bundledProps.get(MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY));
        pop3SocketFactoryFallbackCheck.setChecked("true".equalsIgnoreCase(bundledProps.get(MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY)));

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----backButton clicked");
                step(EmailAccountPop3_4_Activity.this, EmailAccountSmtp_2_Activity.class);                        
           }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                getResult(EmailAccountPop3_4_Activity.this, CheckEmailResultActivity.class);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----nextButton clicked");
                step(EmailAccountPop3_4_Activity.this, EmailAccountFinish_5_Activity.class);                        
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
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
}
