package com.creek.whereareyou.android.activity.account;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import com.creek.whereareyou.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractEmailAccountActivity extends Activity {
    private static final String TAG = AbstractEmailAccountActivity.class.getSimpleName();

    protected static final String MAIL_PROPERTIES = "MAIL_PROPERTIES";
    protected static final String PREDEFINED_PROPERTIES = "PREDEFINED_PROPERTIES";
    protected static final String CHECK_MODE = "CHECK_MODE";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";
    
    protected HashMap<String, String> bundledProps;

    protected Button backButton;
    protected Button testButton;
    protected Button nextButton;
   
    @Override
    protected void onCreate(Bundle icicle) {
        Log.d(TAG, "-------onCreate() " + this.getClass().getCanonicalName());

        super.onCreate(icicle);
        setContentView(getLayoutId());
        
        backButton = (Button) findViewById(R.id.mail_properties_button_back);
        testButton = (Button) findViewById(R.id.mail_properties_button_test);
        nextButton = (Button) findViewById(R.id.mail_properties_button_next);
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        extractBundledProperties();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            bundledProps = (HashMap<String, String>) data.getExtras().get(MAIL_PROPERTIES);
        }
    }

    @SuppressWarnings("unchecked")
    protected void extractBundledProperties() {
        Bundle extras = getIntent().getExtras();
        Log.d(TAG, "-------extras");
        if (extras != null) {
            Log.d(TAG, "-------extras != null");
            bundledProps = (HashMap<String, String>) extras.get(MAIL_PROPERTIES);
            Log.d(TAG, "-------bundledProps " + bundledProps);
        }
    }

    protected void putBundledPropertiesIntoIntent(Intent intent) {
        gatherProperties();
        putExtrasIntoIntent(intent, bundledProps);
    }

    protected void putExtrasIntoIntent(Intent intent, HashMap<String, String> props) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(MAIL_PROPERTIES, props);
        intent.putExtras(bundle);
    }

    protected void gatherTextFieldValue(HashMap<String, String> props, String propName, EditText field) {
        if (field.getText() != null) {
            props.put(propName, field.getText().toString());
        }
    }
    
    protected void gatherBooleanValue(HashMap<String, String> props, String propName, CheckBox field) {
        if (field.isChecked()) {
            props.put(propName, TRUE);
        } else {
            props.put(propName, FALSE);
        }
    }
    
    protected HashMap<String, String> convertPropertiesToHashMap(Properties properties) {
        HashMap<String, String> props = new HashMap<String, String>();
        Set<Object> keys = properties.keySet();
        for (Object key: keys) {
            props.put((String)key, properties.getProperty((String)key));
        }
        return props;
    }
    
    protected Properties convertHashMapToProperties(HashMap<String, String> props) {
        Properties properties = new Properties();
        properties.putAll(props);
        return properties;
    }
    
    protected <T extends AbstractEmailAccountActivity> void step(T currentActivity, Class<? extends Activity> nextActivityClass) {
        Log.d(TAG, "-----step: " + currentActivity.getClass().getCanonicalName() + " -> " + nextActivityClass.getCanonicalName());
        Intent intent = new Intent(currentActivity, nextActivityClass);
        putBundledPropertiesIntoIntent(intent);
        startActivity(intent);
    }
    
    protected <T extends AbstractEmailAccountActivity> void getCheckResult(T currentActivity, CheckMode checkMode) {
        Intent intent = new Intent(currentActivity, CheckEmailResultActivity.class);
        gatherProperties();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(MAIL_PROPERTIES, bundledProps);
        bundle.putSerializable(CHECK_MODE, checkMode);
        intent.putExtras(bundle);
        startActivityForResult(intent, checkMode.getRequestCode());
    }
    
    protected void gatherProperties() {
        //
    }
    
    protected abstract int getLayoutId();
}
