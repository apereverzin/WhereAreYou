package com.creek.whereareyou.android.activity.account;

import com.creek.whereareyou.R;

import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailAccountFinish_5_Activity extends AbstractEmailAccountActivity {
    private static final String TAG = EmailAccountFinish_5_Activity.class.getSimpleName();

    private Button backButton;
    private Button testButton;
    private Button finishAndSaveButton;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.email_account_finish_5);

        extractBundledProperties();

        backButton = (Button) findViewById(R.id.mail_properties_button_back);
        testButton = (Button) findViewById(R.id.mail_properties_button_test);
        finishAndSaveButton = (Button) findViewById(R.id.mail_properties_button_save_and_finish);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----backButton clicked");
                Intent intent = new Intent(EmailAccountFinish_5_Activity.this, EmailAccountPop3_4_Activity.class);                        
                putExtrasIntoIntentAndStartActivity(intent);
           }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                Intent intent = new Intent(EmailAccountFinish_5_Activity.this, CheckEmailResultActivity.class);
                putExtrasIntoIntent(intent, bundledProps);
                startActivity(intent);
            }
        });

        finishAndSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----saveButton clicked");
                try {
                    MailAccountPropertiesProvider.getInstance().persistMailProperties(convertHashMapToProperties(bundledProps));
                    setResult(RESULT_OK);
                    finish();
                } catch (Exception ex) {
                    ActivityUtil.showException(EmailAccountFinish_5_Activity.this, ex);
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
}
