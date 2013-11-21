package com.creek.whereareyou.android.util;

import java.util.Enumeration;
import java.util.Properties;

import com.creek.whereareyou.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 *
 */
public final class ActivityUtil {
    private static final String TITLE_ENTRIES_SEPARATOR = ": ";
    
    private ActivityUtil() {
        //
    }
    
    public static void showException(Context parent, Throwable ex) {
        if(ex != null) {
            Log.e(parent.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
            new AlertDialog.Builder(parent).setTitle("Error").setMessage(ex.getLocalizedMessage()).setNeutralButton("Close", null).show();
        }
    }
    
    public static void showAlert(Context parent, String alert) {
        new AlertDialog.Builder(parent).setTitle("Error").setMessage(alert).setNeutralButton("Close", null).show();
    }
    
//    public static void processCheckConnectionFailure(Activity parent, Throwable ex) {
//        if(ex.getCause() != null && ex.getCause() instanceof ConnectorException) {
//            Throwable cause = ex.getCause();
//            if(cause.getCause() != null && cause.getCause() instanceof AuthenticationFailedException) {
//                showAlert(parent, parent.getString(R.string.error_message_authentication_failed));
//            } else if(cause.getCause() != null && cause.getCause() instanceof MessagingException) {
//                ActivityUtil.showAlert(parent, parent.getString(R.string.error_message_connection_failed));
//            } else {
//                showException(parent, ex);
//            }
//        } else {
//            ActivityUtil.showException(parent, ex);
//        }
//    }
//    
    public static void logException(String tag, Throwable ex) {
        Log.e(tag, ex.getLocalizedMessage(), ex);
    }
    
    public static void printStackTrace(String TAG, Throwable ex) {
        StackTraceElement[] sta = ex.getStackTrace();
        if (ex.getMessage() != null) {
            Log.e(TAG, ex.getMessage());
        }
        Log.e(TAG, ex.getClass().getName());
        for(StackTraceElement st: sta) {
            Log.e(TAG, "   " + st.getFileName() + "." + st.getMethodName() + ": " + st.getLineNumber());
        }
    }
    
    public static void printProperties(String TAG, Properties props) {
        @SuppressWarnings("unchecked")
        Enumeration<String> propNames = (Enumeration<String>)props.propertyNames();
        while(propNames.hasMoreElements()) {
            String propName = propNames.nextElement();
            Log.d(TAG, propName + "=" + props.getProperty(propName));
        }
    }
    
    public static final void setActivityTitle(Activity activity, int... ids) {
        StringBuilder title = new StringBuilder();
        for(int i = 0; i < ids.length; i++) {
            title.append(activity.getString(ids[i]));
            if (i < ids.length - 1) {
                title.append(TITLE_ENTRIES_SEPARATOR);
            }
        }
        
        activity.setTitle(title);
    }
}
