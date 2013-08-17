package com.creek.whereareyou.android.activity.account;

import java.util.Map;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;

import com.creek.accessemail.connector.mail.ConnectorException;
import com.creek.accessemail.connector.mail.MailConnector;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.util.ActivityUtil;
import static com.creek.whereareyou.android.activity.account.AbstractEmailAccountActivity.MAIL_PROPERTIES;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class CheckEmailResultActivity extends Activity {
    private static final String TAG = CheckEmailResultActivity.class.getSimpleName();

    private boolean res;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle icicle) {
        Log.i(TAG, "onCreate() called");
        super.onCreate(icicle);

        Bundle extras = getIntent().getExtras();
        @SuppressWarnings("unchecked")
        Map<String, String> fullProps = (Map<String, String>) extras.get(MAIL_PROPERTIES);
        Properties props = new Properties();
        props.putAll(fullProps);
        
        final MailConnector connector = new MailConnector(props);

        setContentView(R.layout.check_email_result);
        final TextView checkEmailResultText1 = (TextView) findViewById(R.id.check_email_result_1);
        final TextView checkEmailResultText2 = (TextView) findViewById(R.id.check_email_result_2);

        final ProgressDialog progressBar = ProgressDialog.show(CheckEmailResultActivity.this, "",
                        getString(R.string.check_email_result_wait), true);

        new Thread(new Runnable() {
            public void run() {
                try {
                    connector.checkSMTPConnection();
                    connector.checkPOP3Connection();
                    res = true;
                } catch (ConnectorException ex) {
                    ActivityUtil.logException(TAG, ex);
                    if (ex.getCause().getCause() instanceof AuthenticationFailedException) {
                        errorMessage = getString(R.string.check_email_result_authentication_failed);
                    } else {
                        errorMessage = getString(R.string.check_email_result_connection_failed);
                    }
                    res = false;
                } finally {
                    progressBar.dismiss();
                }
            }
        }).start();
        checkEmailResultText1.setText(R.string.check_email_result_pending);

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.check_email_result_title));
        setTitle(title);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
        if (hasFocus) {
            final TextView checkEmailResultText1 = (TextView) findViewById(R.id.check_email_result_1);
            final TextView checkEmailResultText2 = (TextView) findViewById(R.id.check_email_result_2);
            checkEmailResultText1.setText(buildMessage());
        }
    }

    private String buildMessage() {
        if (res) {
            return getString(R.string.check_email_result_success);
        }
        return getString(R.string.check_email_result_failure) + ": " + errorMessage;
    }
}
