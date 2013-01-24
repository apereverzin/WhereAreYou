package com.creek.whereareyou.android.activity.account;

import java.io.IOException;
import java.util.Properties;

import com.creek.accessemail.connector.mail.ConnectorException;
import com.creek.accessemail.connector.mail.MailConnector;
import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.ApplManager;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import static com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider.MAIL_PASSWORD_PROPERTY;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;
import com.creek.whereareyou.android.util.CryptoException;

import android.accounts.Account;
import android.app.Activity;
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
public class EmailAccountEditActivity extends Activity {
    private static final String TAG = EmailAccountEditActivity.class.getSimpleName();

    private EditText passwordText;
    private Button testButton;
    private Button saveButton;
    
    static final String MAIL_PROPERTIES = "MAIL_PROPERTIES";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.mail_properties);
        passwordText = (EditText) findViewById(R.id.mail_password);
        testButton = (Button) findViewById(R.id.mail_properties_button_test);
        saveButton = (Button) findViewById(R.id.mail_properties_button_save);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        final Account googleAccount = ApplManager.getInstance().getAccountProvider().getGoogleAccount(this);
        
        try {
            Properties props = ApplManager.getInstance().getMailAccountPropertiesProvider().getMailProperties();
            if (props != null) {
                passwordText.setText(props.getProperty(MAIL_PASSWORD_PROPERTY));
            }
        } catch (IOException ex) {
            showException(this, ex);
        } catch (CryptoException ex) {
            showException(this, ex);
        }

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                Properties fullProps = PredefinedMailProperties.getPredefinedProperties(googleAccount.name);
                if (fullProps != null) {
                    fullProps.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable(MAIL_PROPERTIES, fullProps);
                    Intent intent = new Intent(EmailAccountEditActivity.this, CheckEmailResultActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // TODO no full props dialog
                    setResult(RESULT_OK);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Properties fullProps = PredefinedMailProperties.getPredefinedProperties(googleAccount.name);
                    
                    Properties props = new Properties();
                    props.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                    
                    if (fullProps != null) {
                        fullProps.setProperty(MAIL_PASSWORD_PROPERTY, passwordText.getText().toString());
                        MailConnector connector = new MailConnector(fullProps);
                        connector.checkSMTPConnection();
                        
                        ApplManager.getInstance().getMailAccountPropertiesProvider().persistProperties(props);
                    }

                    setResult(RESULT_OK);
                    finish();
                } catch (ConnectorException ex) {
                    showException(EmailAccountEditActivity.this, ex);
                } catch (IOException ex) {
                    showException(EmailAccountEditActivity.this, ex);
                } catch (CryptoException ex) {
                    showException(EmailAccountEditActivity.this, ex);
                }
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            finish();
        }
    }
}