package com.creek.whereareyou.android.services.email;

import java.util.List;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.activity.contacts.ContactsActivity;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import static android.content.Context.NOTIFICATION_SERVICE;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactLocationNotifier {
    private final Context ctx;
    
    public static final int CONTACT_LOCATION_NOTIFICATION_ID = 239;

    private static final String TAG = ContactLocationNotifier.class.getSimpleName();
    
    public ContactLocationNotifier(Context _ctx) {
        this.ctx = _ctx;
    }

    public void notifyUser(List<OwnerLocationDataMessage> messages) {
        Log.d(TAG, "notifyUser: " + messages.size());
        Log.d(TAG, "------notifyUser: " + messages.size());

        if(messages.size() > 0) {
            displayNotification(messages);
        }
    }

    private void displayNotification(List<OwnerLocationDataMessage> messages) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        
        Notification notification = getNotification();
        
        CharSequence contentTitle = ctx.getString(R.string.notifications_contact_locations);  // message title
        CharSequence contentText = ctx.getString(R.string.notifications_contact_locations_received) + ": " + messages.size();
                                                                                                                                                          // text
        Intent notificationIntent = new Intent(ctx, ContactsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);
        
        notification.setLatestEventInfo(ctx, contentTitle, contentText, contentIntent);
        notificationManager.notify(CONTACT_LOCATION_NOTIFICATION_ID, notification);
    }

    private Notification getNotification() {
        int icon = R.drawable.contact_location_notification;        // icon from resources
        CharSequence tickerText = ctx.getString(R.string.notifications_contact_locations); // ticker-text
        long when = System.currentTimeMillis();         // notification time

        return new Notification(icon, tickerText, when);
    }
}
