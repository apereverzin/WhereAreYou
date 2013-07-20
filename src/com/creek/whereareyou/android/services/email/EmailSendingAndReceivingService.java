package com.creek.whereareyou.android.services.email;

import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.notifier.ContactLocationNotifier;
import com.creek.whereareyou.android.notifier.ReceivedMessages;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.accounts.Account;
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
            Account account = GoogleAccountProvider.getInstance().getEmailAccount(EmailSendingAndReceivingService.this);
            Log.d(TAG, "===================: " + account.toString());
            
            try {
                EmailSendingAndReceivingManager emailSendingAndReceivingManager = new EmailSendingAndReceivingManager(account);
                
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
                ActivityUtil.showException(EmailSendingAndReceivingService.this, ex);
            }
        }
    };
}
