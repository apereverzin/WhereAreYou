package com.creek.whereareyou.android.activity.account;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.creek.accessemail.connector.mail.ConnectorException;
import com.creek.accessemail.connector.mail.MailConnector;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_AUTH_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_STARTTLS_ENABLE_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_IMAP_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_IMAP_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.activity.account.CheckEmailResultActivity;
import com.creek.whereareyou.android.activity.account.EmailAccountEditActivity;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyou.android.util.CryptoException;

import android.app.Activity;
import android.content.Intent;
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
public class EmailAccountEditAdvancedActivity extends Activity {
    private static final String TAG = EmailAccountEditAdvancedActivity.class.getSimpleName();

    private EditText emailAddressText;
    private EditText passwordText;
    private EditText smtpHostText;
    private EditText smtpPortText;
    private CheckBox smtpAuthCheck;
    private CheckBox startTlsEnableCheck;
    private EditText smtpSocketFactoryPortText;
    private EditText smtpSocketFactoryClassText;
    private EditText pop3HostText;
    private EditText pop3PortText;
    private EditText pop3SocketFactoryPortText;
    private EditText pop3SocketFactoryClassText;
    private CheckBox pop3SocketFactoryFallbackCheck;
    private Button testButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.mail_properties_advanced);

        Bundle extras = getIntent().getExtras();
        @SuppressWarnings("unchecked")
        Map<String, String> fullMailProps = (Map<String, String>) extras.get(EmailAccountEditActivity.MAIL_PROPERTIES);
        Properties props = new Properties();
        props.putAll(fullMailProps);

        emailAddressText = (EditText) findViewById(R.id.mail_username);
        passwordText = (EditText) findViewById(R.id.mail_password);
        smtpHostText = (EditText) findViewById(R.id.mail_smtp_host);
        smtpPortText = (EditText) findViewById(R.id.mail_smtp_port);
        smtpAuthCheck = (CheckBox) findViewById(R.id.mail_smtp_auth);
        smtpSocketFactoryClassText = (EditText) findViewById(R.id.mail_smtp_socket_factory_class);
        smtpSocketFactoryPortText = (EditText) findViewById(R.id.mail_smtp_socket_factory_port);
        startTlsEnableCheck = (CheckBox) findViewById(R.id.mail_smtp_start_tls_enable);
        pop3HostText = (EditText) findViewById(R.id.mail_pop3_host);
        pop3PortText = (EditText) findViewById(R.id.mail_pop3_port);
        pop3SocketFactoryPortText = (EditText) findViewById(R.id.mail_pop3_socket_factory_port);
        pop3SocketFactoryClassText = (EditText) findViewById(R.id.mail_pop3_socket_factory_class);
        pop3SocketFactoryFallbackCheck = (CheckBox) findViewById(R.id.mail_pop3_socket_factory_fallback);
        testButton = (Button) findViewById(R.id.mail_properties_button_test);
        saveButton = (Button) findViewById(R.id.mail_properties_button_save);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        emailAddressText.setText(props.getProperty(MAIL_USERNAME_PROPERTY));
        passwordText.setText(props.getProperty(MAIL_PASSWORD_PROPERTY));
        smtpHostText.setText(props.getProperty(MAIL_SMTP_HOST_PROPERTY));
        smtpPortText.setText(props.getProperty(MAIL_SMTP_PORT_PROPERTY));
        smtpAuthCheck.setChecked("true".equalsIgnoreCase(props.getProperty(MAIL_SMTP_AUTH_PROPERTY)));
        startTlsEnableCheck.setChecked("true".equalsIgnoreCase(props.getProperty(MAIL_SMTP_STARTTLS_ENABLE_PROPERTY)));
        smtpSocketFactoryClassText.setText(props.getProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY));
        smtpSocketFactoryPortText.setText(props.getProperty(MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY));
        pop3HostText.setText(props.getProperty(MAIL_POP3_HOST_PROPERTY));
        pop3PortText.setText(props.getProperty(MAIL_POP3_PORT_PROPERTY));
        pop3SocketFactoryPortText.setText(props.getProperty(MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY));
        pop3SocketFactoryClassText.setText(props.getProperty(MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY));
        pop3SocketFactoryFallbackCheck.setChecked("true".equalsIgnoreCase(props.getProperty(MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY)));

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                Properties fullProps = gatherProperties();
                final Bundle bundle = new Bundle();
                bundle.putSerializable(EmailAccountEditActivity.MAIL_PROPERTIES, fullProps);
                Intent intent = new Intent(EmailAccountEditAdvancedActivity.this, CheckEmailResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----saveButton clicked");
                try {
                    Properties fullProps = gatherProperties();
                    MailConnector connector = new MailConnector(fullProps);
                    connector.checkSMTPConnection();
                    connector.checkPOP3Connection();
                    MailAccountPropertiesProvider.getInstance().persistMailProperties(fullProps);
                    setResult(RESULT_OK);
                    finish();
                } catch (IOException ex) {
                    ActivityUtil.showException(EmailAccountEditAdvancedActivity.this, ex);
                } catch (CryptoException ex) {
                    ActivityUtil.showException(EmailAccountEditAdvancedActivity.this, ex);
                } catch (ConnectorException ex) {
                    ActivityUtil.showException(EmailAccountEditAdvancedActivity.this, ex);
                }
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
    }

    private Properties gatherProperties() {
        Properties fullProps = new Properties();
        if (emailAddressText.getText() != null) {
            String emailAddress = emailAddressText.getText().toString().toLowerCase();
            fullProps.setProperty(MAIL_USERNAME_PROPERTY, emailAddress);
        }
        gatherTextFieldValue(fullProps, MAIL_PASSWORD_PROPERTY, passwordText);
        gatherTextFieldValue(fullProps, MAIL_SMTP_HOST_PROPERTY, smtpHostText);
        gatherTextFieldValue(fullProps, MAIL_SMTP_PORT_PROPERTY, smtpPortText);
        if (smtpAuthCheck.isChecked()) {
            fullProps.setProperty(MAIL_SMTP_AUTH_PROPERTY, "true");
        }
        if (smtpAuthCheck.isChecked()) {
            fullProps.setProperty(MAIL_SMTP_STARTTLS_ENABLE_PROPERTY, "true");
        }
        gatherTextFieldValue(fullProps, MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY, smtpSocketFactoryClassText);
        gatherTextFieldValue(fullProps, MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY, smtpSocketFactoryPortText);
        gatherTextFieldValue(fullProps, MAIL_IMAP_HOST_PROPERTY, pop3HostText);
        gatherTextFieldValue(fullProps, MAIL_IMAP_PORT_PROPERTY, pop3PortText);
        return fullProps;
    }

    private void gatherTextFieldValue(Properties props, String propName, EditText field) {
        if (field.getText() != null) {
            props.setProperty(propName, field.getText().toString());
        }
    }
}
