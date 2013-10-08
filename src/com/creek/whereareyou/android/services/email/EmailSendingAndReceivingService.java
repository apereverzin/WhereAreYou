package com.creek.whereareyou.android.services.email;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;
import static com.creek.whereareyou.android.util.ActivityUtil.showException;

import com.creek.whereareyou.android.notifier.ContactLocationNotifier;
import com.creek.whereareyou.android.notifier.ReceivedMessages;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSendingAndReceivingService extends Service {
    private static final String TAG = EmailSendingAndReceivingService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;
    
    private static final int MILLISECONDS_IN_MINUTE = 60 * 1000;
    // TODO make configurable
    private int timeoutInMinutes = 1;
    private int timeoutMs = timeoutInMinutes * MILLISECONDS_IN_MINUTE;

    private static final String TIMER_NAME = "WhereAreYouEmailReceivingTimer";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contentResolver = getContentResolver();

        timer = new Timer(TIMER_NAME);
        timer.schedule(emailSendingAndReceivingTask, 1000L, timeoutMs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
    
    private TimerTask emailSendingAndReceivingTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work");
            try {
                Properties props = PredefinedMailProperties.getPredefinedPropertiesForServer("gmail");
                props.put(MAIL_USERNAME_PROPERTY, "andrey.pereverzin");
                props.put(MAIL_PASSWORD_PROPERTY, "bertoluCCi");
                EmailSendingAndReceivingManager emailSendingAndReceivingManager = new EmailSendingAndReceivingManager(props);
                
                Log.d(TAG, "===================EmailSendingAndReceivingService sending");
                EmailSender emailSender = new EmailSender(emailSendingAndReceivingManager);
                emailSender.sendRequestsAndResponses();
                
                Log.d(TAG, "===================EmailSendingAndReceivingService receiving");
                EmailReceiver emailReceiver = new EmailReceiver(emailSendingAndReceivingManager);
                ReceivedMessages messageCounts = emailReceiver.receiveRequestsAndResponses();
                if (messageCounts.hasMessages()) {
                    ContactLocationNotifier notifier = new ContactLocationNotifier(EmailSendingAndReceivingService.this);
                    notifier.notifyUser(messageCounts);
                }
            } catch(Throwable ex) {
                showException(EmailSendingAndReceivingService.this, ex);
            }
        }
    };
}
