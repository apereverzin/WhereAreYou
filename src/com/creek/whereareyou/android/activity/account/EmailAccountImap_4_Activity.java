package com.creek.whereareyou.android.activity.account;

import java.io.IOException;
import java.util.Properties;

import com.creek.accessemail.connector.mail.ConnectorException;
import com.creek.accessemail.connector.mail.MailConnector;
import static com.creek.accessemail.connector.mail.PredefinedMailProperties.getPredefinedProperties;

import com.creek.whereareyou.R;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;

import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.CryptoException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountImap_4_Activity extends Activity {
    private static final String TAG = EmailAccountImap_4_Activity.class.getSimpleName();

    private EditText emailAddressText;
    private EditText passwordText;
    private Button testButton;
    private Button advancedButton;
    private Button saveButton;
    Dialog d;

    static final String MAIL_PROPERTIES = "MAIL_PROPERTIES";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.mail_properties);
        emailAddressText = (EditText) findViewById(R.id.mail_username);
        passwordText = (EditText) findViewById(R.id.mail_password);
        testButton = (Button) findViewById(R.id.mail_properties_button_test);
        advancedButton = (Button) findViewById(R.id.mail_properties_button_advanced);
        saveButton = (Button) findViewById(R.id.mail_properties_button_save);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //final Account googleAccount = GoogleAccountProvider.getInstance().getEmailAccount(this);

        try {
            Properties props = MailAccountPropertiesProvider.getInstance().getMailProperties();
            if (props != null) {
                emailAddressText.setText(props.getProperty(MAIL_USERNAME_PROPERTY));
                passwordText.setText(props.getProperty(MAIL_PASSWORD_PROPERTY));
            }
        } catch (IOException ex) {
            showException(this, ex);
        } catch (CryptoException ex) {
            showException(this, ex);
        }

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "testButton clicked");
                String emailAddress = emailAddressText.getText().toString().toLowerCase();
                Properties fullProps = getPredefinedProperties(emailAddress);
                if (fullProps != null) {
                    fullProps.setProperty(MAIL_USERNAME_PROPERTY, emailAddress);
                    fullProps.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable(MAIL_PROPERTIES, fullProps);
                    Intent intent = new Intent(EmailAccountImap_4_Activity.this, CheckEmailResultActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // TODO no full props dialog
                    setResult(RESULT_OK);
                }
            }
        });

        advancedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----advancedButton clicked");
                String emailAddress = emailAddressText.getText().toString().toLowerCase();
                Properties fullProps = getPredefinedProperties(emailAddress);
                if (fullProps == null) {
                    fullProps = new Properties();
                }
                fullProps.setProperty(MAIL_USERNAME_PROPERTY, emailAddress);
                fullProps.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                final Bundle bundle = new Bundle();
                bundle.putSerializable(MAIL_PROPERTIES, fullProps);
                Intent intent = new Intent(EmailAccountImap_4_Activity.this, EmailAccountEditAdvancedActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String emailAddress = emailAddressText.getText().toString().toLowerCase();
                    //Properties fullProps = getPredefinedProperties(googleAccount.name);
                    Properties fullProps = getPredefinedProperties(emailAddress);

                    Properties props = new Properties();
                    props.setProperty(MAIL_USERNAME_PROPERTY, emailAddress);
                    props.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());

                    if (fullProps != null) {
                        //fullProps.setProperty(MAIL_USERNAME_PROPERTY, extractUserNameFromEmailAddress(googleAccount.name));
                        fullProps.setProperty(MAIL_USERNAME_PROPERTY, emailAddress);
                        fullProps.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                        MailConnector connector = new MailConnector(fullProps);
                        // Durable operation
                        connector.checkSMTPConnection();
                        connector.checkPOP3Connection();

                        MailAccountPropertiesProvider.getInstance().persistMailProperties(props);
                    }

                    setResult(RESULT_OK);
                    finish();
                } catch (ConnectorException ex) {
                    showException(EmailAccountImap_4_Activity.this, ex);
                } catch (IOException ex) {
                    showException(EmailAccountImap_4_Activity.this, ex);
                } catch (CryptoException ex) {
                    showException(EmailAccountImap_4_Activity.this, ex);
                }
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

//    private String extractUserNameFromEmailAddress(String emailAddress) throws AddressException {
//        StringTokenizer st = new StringTokenizer(emailAddress, "@");
//       return st.nextToken();
//    }
}
