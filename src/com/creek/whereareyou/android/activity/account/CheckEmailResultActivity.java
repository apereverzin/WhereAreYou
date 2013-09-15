package com.creek.whereareyou.android.activity.account;

import java.util.Map;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;

import com.creek.accessemail.connector.mail.ConnectorException;
import com.creek.accessemail.connector.mail.MailConnector;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class CheckEmailResultActivity extends AbstractEmailAccountActivity {
    private static final String TAG = CheckEmailResultActivity.class.getSimpleName();

    private StringBuilder resultMessage = new StringBuilder();

    @Override
    protected void onCreate(Bundle icicle) {
        Log.i(TAG, "onCreate() called");
        super.onCreate(icicle);

        testButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        @SuppressWarnings("unchecked")
        Map<String, String> fullProps = (Map<String, String>) extras.get(MAIL_PROPERTIES);
        Properties props = new Properties();
        props.putAll(fullProps);
        final CheckMode checkMode = (CheckMode) extras.get(CHECK_MODE);
        
        final MailConnector connector = new MailConnector(props);

        setContentView(R.layout.check_email_result);
        final TextView checkEmailResultText1 = (TextView) findViewById(R.id.check_email_result_1);

        final ProgressDialog progressBar = ProgressDialog.show(CheckEmailResultActivity.this, "", "", true);
        progressBar.setMessage(getString(R.string.check_email_settings));

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (checkMode.hasSmtp()) {
                        runSmtpConnectionTest(connector, progressBar, R.string.check_email_result_smtp);
                    }
                    
                    if (checkMode.hasPop3()) {
                        runPop3ConnectionTest(connector, progressBar, R.string.check_email_result_pop3);
                    }
                    
                    if (checkMode.hasImap()) {
                        runImapConnectionTest(connector, progressBar, R.string.check_email_result_imap);
                    }
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
            checkEmailResultText1.setText(resultMessage);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.check_email_result;
    }

    private void runSmtpConnectionTest(MailConnector connector, ProgressDialog progressBar, int resultHeaderMessageId) {
        resultMessage.append(getString(resultHeaderMessageId)).append(": ");
        try {
            connector.checkSMTPConnection();
            resultMessage.append(getString(R.string.check_email_result_success)).append("\n");
        } catch (ConnectorException ex) {
            ActivityUtil.logException(TAG, ex);
            if (ex.getCause().getCause() instanceof AuthenticationFailedException) {
                resultMessage.append(getString(R.string.check_email_result_authentication_failed)).append("\n");
            } else {
                resultMessage.append(getString(R.string.check_email_result_connection_failed)).append("\n");
            }
        }
    }

    private void runPop3ConnectionTest(MailConnector connector, ProgressDialog progressBar, int resultHeaderMessageId) {
        resultMessage.append(getString(resultHeaderMessageId)).append(": ");
        try {
            connector.checkPOP3Connection();
            resultMessage.append(getString(R.string.check_email_result_success)).append("\n");
        } catch (ConnectorException ex) {
            ActivityUtil.logException(TAG, ex);
            if (ex.getCause().getCause() instanceof AuthenticationFailedException) {
                resultMessage.append(getString(R.string.check_email_result_authentication_failed)).append("\n");
            } else {
                resultMessage.append(getString(R.string.check_email_result_connection_failed)).append("\n");
            }
        }
    }

    private void runImapConnectionTest(MailConnector connector, ProgressDialog progressBar, int resultHeaderMessageId) {
        resultMessage.append(getString(resultHeaderMessageId)).append(": ");
        try {
            connector.checkIMAPConnection();
            resultMessage.append(getString(R.string.check_email_result_success)).append("\n");
        } catch (ConnectorException ex) {
            ActivityUtil.logException(TAG, ex);
            if (ex.getCause().getCause() instanceof AuthenticationFailedException) {
                resultMessage.append(getString(R.string.check_email_result_authentication_failed)).append("\n");
            } else {
                resultMessage.append(getString(R.string.check_email_result_connection_failed)).append("\n");
            }
        }
    }
}
