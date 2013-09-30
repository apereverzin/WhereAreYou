package com.creek.whereareyou.android.activity.account;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static android.view.View.INVISIBLE;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractEmailAccountActivityWithButtons extends AbstractEmailAccountActivity {
    private static final String TAG = AbstractEmailAccountActivityWithButtons.class.getSimpleName();

    @Override
    protected void onCreate(Bundle icicle) {
        Log.d(TAG, "-------onCreate() " + this.getClass().getCanonicalName());

        super.onCreate(icicle);
        backButton.setVisibility(INVISIBLE);
        
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----backButton clicked: " + getCurrentActivity().getClass().getCanonicalName() + " -> " + getPreviousActivityClass().getCanonicalName());
                step(getCurrentActivity(), getPreviousActivityClass());                        
                finish();
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----testButton clicked");
                getCheckResult(getCurrentActivity(), getCheckMode());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "-----nextButton clicked: " + getCurrentActivity().getClass().getCanonicalName() + " -> " + getNextActivityClass().getCanonicalName());
                step(getCurrentActivity(), getNextActivityClass());                        
                //finish();
            }
        });
    }
    
    protected abstract <T extends AbstractEmailAccountActivityWithButtons> T getCurrentActivity();
    
    protected abstract Class<? extends Activity> getPreviousActivityClass();
    
    protected abstract Class<? extends Activity> getNextActivityClass();
    
    protected abstract CheckMode getCheckMode();
}
