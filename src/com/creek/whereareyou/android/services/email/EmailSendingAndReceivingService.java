package com.creek.whereareyou.android.services.email;

import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.service.LocationMessagesService;

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
    
    private TimerTask emailReceivingTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work");
            Account account = GoogleAccountProvider.getInstance().getEmailAccount(EmailSendingAndReceivingService.this);
            Log.d(TAG, "===================: " + account.toString());
            
            try {
                Properties props = PredefinedMailProperties.getPredefinedPropertiesForServer("gmail");
                props.put("mail.username", "andrey.pereverzin");
                props.put("mail.password", "xxxxxxxx");
                MailAccountPropertiesProvider.getInstance().persistMailProperties(props);
                
                Properties mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
                LocationMessagesService locationMessagesService = new LocationMessagesService(mailProps);
                Set<GenericMessage> messages = locationMessagesService.receiveMessages();
                Log.d(TAG, "===================messages.size(): " + messages.size());
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
        timer.schedule(emailReceivingTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
