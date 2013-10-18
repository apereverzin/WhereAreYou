package com.creek.whereareyou.android.activity.account;

import com.creek.whereareyou.R;

import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_AUTH_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_HOST_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_STARTTLS_ENABLE_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.whereareyou.android.activity.account.CheckMode.SMTP_AND_POP3;
import static com.creek.whereareyou.android.util.ActivityUtil.setActivityTitle;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.view.View.INVISIBLE;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountFinish_5_Activity extends AbstractEmailAccountActivity {
    private static final String TAG = EmailAccountFinish_5_Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        TextView descr = (TextView) findViewById(R.id.mail_account_description);
        descr.setText(buildMailSettingsDescription());
        
        backButton.setVisibility(INVISIBLE);

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "testButton clicked");
                getCheckResult(EmailAccountFinish_5_Activity.this, SMTP_AND_POP3);
            }
        });

        nextButton.setText(R.string.save_and_finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "saveButton clicked");
                try {
                    MailAccountPropertiesProvider.getInstance().persistMailProperties(convertHashMapToProperties(bundledProps));
                    setResult(RESULT_OK);
                } catch (Exception ex) {
                    showException(EmailAccountFinish_5_Activity.this, ex);
                }
                
                finish();
            }
        });

        setActivityTitle(this, R.string.app_name, R.string.mail_settings_activity_name);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.email_account_finish_5;
    }
    
    private String buildMailSettingsDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.mail_username)).append(": " ).append(bundledProps.get(MAIL_USERNAME_PROPERTY));
        
        if (!TRUE.equals(bundledProps.get(PREDEFINED_PROPERTIES))) {
            addProperty(sb, R.string.mail_smtp_host, MAIL_SMTP_HOST_PROPERTY);
            addProperty(sb, R.string.mail_smtp_port, MAIL_SMTP_PORT_PROPERTY);
            addProperty(sb, R.string.mail_smtp_auth, MAIL_SMTP_AUTH_PROPERTY);
            addProperty(sb, R.string.mail_smtp_start_tls_enable, MAIL_SMTP_STARTTLS_ENABLE_PROPERTY);
            addProperty(sb, R.string.mail_smtp_socket_factory_port, MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY);
            addProperty(sb, R.string.mail_smtp_socket_factory_class, MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY);
            addProperty(sb, R.string.mail_pop3_host, MAIL_POP3_HOST_PROPERTY);
            addProperty(sb, R.string.mail_pop3_port, MAIL_POP3_PORT_PROPERTY);
            addProperty(sb, R.string.mail_pop3_socket_factory_port, MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY);
            addProperty(sb, R.string.mail_pop3_socket_factory_class, MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY);
            addProperty(sb, R.string.mail_pop3_socket_factory_fallback, MAIL_POP3_SOCKET_FACTORY_FALLBACK_PROPERTY);
        }
        
        return sb.toString();
    }
    
    private void addProperty(StringBuilder sb, int titleId, String propertyName) {
        String value = bundledProps.get(propertyName);
        
        if (value != null) {
            sb.append("\n").append(getString(titleId)).append(": " ).append(value);
        }
        
    }
}
