package com.creek.whereareyou.android.services.email;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyoumodel.domain.RequestResponse;

import android.accounts.Account;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author andreypereverzin
 */
public class EmailSendingAndReceivingService extends Service {
    private static final String TAG = EmailSendingAndReceivingService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;
    protected ConnectivityManager cm;
    
    private TimerTask emailSendingReceivingTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work");
            Account account = GoogleAccountProvider.getInstance().getEmailAccount(EmailSendingAndReceivingService.this);
            Log.d(TAG, "===================: " + account.toString());
            
            try {
                //Set<GenericMessage> messages = locationMessagesService.receiveMessages();
                //Log.d(TAG, "===================messages.size(): " + messages.size());
                
                List<RequestResponse> unsentRequests = SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnsentContactRequests();
                List<RequestResponse> unsentResponses = SQLiteRepositoryManager.getInstance().getContactResponseRepository().getUnsentContactResponses();
                EmailSendingReceivingManager emailSendingReceivingManager = new EmailSendingReceivingManager(account);
                emailSendingReceivingManager.sendMessages(unsentRequests, new RequestMessageFactory());
                emailSendingReceivingManager.sendMessages(unsentResponses, new ResponseMessageFactory());
            } catch(Throwable ex) {
                ActivityUtil.showException(EmailSendingAndReceivingService.this, ex);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        contentResolver = getContentResolver();

        timer = new Timer("WhereAreYouEmailReceivingTimer");
        timer.schedule(emailSendingReceivingTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
