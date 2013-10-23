package com.creek.whereareyou.android.activity.account;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import static android.view.View.INVISIBLE;
import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractEmailAccountActivityWithButtons extends AbstractEmailAccountActivity {
    private static final String TAG = AbstractEmailAccountActivityWithButtons.class.getSimpleName();

    @Override
    protected void onCreate(Bundle icicle) {
        Log.d(TAG, "onCreate() " + this.getClass().getCanonicalName());

        super.onCreate(icicle);
        backButton.setVisibility(INVISIBLE);
        
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "testButton clicked");
                getCheckResult(getCurrentActivity(), getCheckMode());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "nextButton clicked: " + getCurrentActivity().getClass().getCanonicalName() + " -> " + getNextActivityClass().getCanonicalName());
                step(getCurrentActivity(), getNextActivityClass());                        
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            Log.d(TAG, "BACK key pressed");
            step(getCurrentActivity(), getPreviousActivityClass());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    protected abstract <T extends AbstractEmailAccountActivityWithButtons> T getCurrentActivity();
    
    protected abstract Class<? extends Activity> getPreviousActivityClass();
    
    protected abstract Class<? extends Activity> getNextActivityClass();
    
    protected abstract CheckMode getCheckMode();

    protected boolean processBackButton() {
        step(getCurrentActivity(), getPreviousActivityClass());
        return true;
    }
}
