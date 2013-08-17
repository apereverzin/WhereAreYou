package com.creek.whereareyou.android.activity.account;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractEmailAccountActivity extends Activity {
    protected static final String MAIL_PROPERTIES = "MAIL_PROPERTIES";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";
    protected HashMap<String, String> bundledProps;
    
    @SuppressWarnings("unchecked")
    protected void extractBundledProperties() {
        Bundle extras = getIntent().getExtras();
        bundledProps = (HashMap<String, String>) extras.get(MAIL_PROPERTIES);
    }

    protected void putExtrasIntoIntentAndStartActivity(Intent intent) {
        putExtrasIntoIntent(intent);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    protected void putExtrasIntoIntent(Intent intent) {
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
        for (String key: props.keySet()) {
            properties.put(key, props.get(key));
        }
        return properties;
    }
    
    protected void gatherProperties() {
        //
    }
}
