package com.creek.whereareyou.android.activity.account;

import static android.view.View.INVISIBLE;

import java.util.Properties;
import java.util.Set;

import javax.mail.AuthenticationFailedException;

import com.creek.accessemail.connector.mail.ConnectorException;
import com.creek.accessemail.connector.mail.MailConnector;
import com.creek.whereareyou.R;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class CheckEmailResultActivity extends AbstractEmailAccountActivity {
    private static final String TAG = CheckEmailResultActivity.class.getSimpleName();

    private StringBuilder resultMessage = new StringBuilder();
    private TextView checkEmailResultText;
    private Properties mailProps;
    
    @Override
    protected void onCreate(Bundle icicle) {
        Log.i(TAG, "onCreate() called");
        super.onCreate(icicle);
        
        checkEmailResultText = (TextView) findViewById(R.id.check_email_result);

        testButton.setVisibility(INVISIBLE);
        nextButton.setVisibility(INVISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----backButton clicked");
                finishActivity();
           }
        });

        Bundle extras = getIntent().getExtras();
        final CheckMode checkMode = (CheckMode)extras.get(CHECK_MODE);
        
        mailProps = convertHashMapToProperties(bundledProps);
        Log.d(TAG, "------------------------------");
        Set<Object> keys = mailProps.keySet();
        for (Object key: keys) {
            Log.d(TAG, key + " " + mailProps.get(key));
        }
        Log.d(TAG, "------------------------------");
        final MailConnector connector = new MailConnector(mailProps);

        checkEmailResultText.setText(R.string.check_email_result_pending);

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

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.check_email_result_title));
        setTitle(title);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
        if (hasFocus) {
            checkEmailResultText = (TextView) findViewById(R.id.check_email_result);
            checkEmailResultText.setText(resultMessage);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Log.d(TAG, "BACK key pressed");
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    
    private void finishActivity() {
        Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(MAIL_PROPERTIES, convertPropertiesToHashMap(mailProps));
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
