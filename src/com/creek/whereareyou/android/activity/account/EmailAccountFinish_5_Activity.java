package com.creek.whereareyou.android.activity.account;

import com.creek.whereareyou.R;

import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.ActivityUtil;

import static com.creek.whereareyou.android.activity.account.CheckMode.SMTP_AND_POP3;

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
        descr.setText("Account");
        
        backButton.setVisibility(INVISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----backButton clicked");
                goBack();
           }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                getCheckResult(EmailAccountFinish_5_Activity.this, SMTP_AND_POP3);
            }
        });

        nextButton.setText(R.string.save_and_finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----saveButton clicked");
                try {
                    MailAccountPropertiesProvider.getInstance().persistMailProperties(convertHashMapToProperties(bundledProps));
                    setResult(RESULT_OK);
                    //finish();
                } catch (Exception ex) {
                    ActivityUtil.showException(EmailAccountFinish_5_Activity.this, ex);
                }
            }
        });

        StringBuilder title = new StringBuilder(getString(R.string.app_name)).append(": ").append(getString(R.string.mail_properties_activity_name));
        setTitle(title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.email_account_finish_5;
    }
    
    private void goBack() {
        if (TRUE.equals(bundledProps.get(PREDEFINED_PROPERTIES))) {
            step(EmailAccountFinish_5_Activity.this, EmailAccountAddress_1_Activity.class); 
        } else {
            step(EmailAccountFinish_5_Activity.this, EmailAccountPop3_4_Activity.class); 
        }
        finish();
    }
}
