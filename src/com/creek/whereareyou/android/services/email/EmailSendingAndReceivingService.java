package com.creek.whereareyou.android.services.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.repository.IdentifiableRepository;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.domain.sendable.GenericRequestResponse;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;

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
 * @author Andrey Pereverzin
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
                
                ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
                ContactResponseRepository contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
                
                List<ContactRequest> unsentRequests = SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnsentContactRequests();
                List<ContactResponseEntity> unsentResponses = SQLiteRepositoryManager.getInstance().getContactResponseRepository().getUnsentContactResponses();
                EmailSendingAndReceivingManager emailSendingAndReceivingManager = new EmailSendingAndReceivingManager(account);
                List<ContactRequest> failedRequests = sendGenericRequestsResponses(contactRequestRepository, emailSendingAndReceivingManager, unsentRequests, new RequestMessageFactory());
                List<ContactResponse> failedResponses = sendGenericRequestsResponses(contactResponseRepository, emailSendingAndReceivingManager, unsentResponses, new ResponseMessageFactory());
                
                Set<GenericMessage> receivedMessages = emailSendingAndReceivingManager.receiveMessages();
                for (GenericMessage message: receivedMessages) {
                    
                }
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
    
    private <T extends GenericRequestResponse> List<T> sendGenericRequestsResponses(IdentifiableRepository<T> repository, EmailSendingAndReceivingManager emailSendingAndReceivingManager, List<T> dataList, MessageFactory<OwnerRequestResponse> messageFactory) {
        List<T> unsentDataList = new ArrayList<T>();
        for (int i = 0; i < dataList.size(); i++) {
            T data = dataList.get(i);
            try {
                emailSendingAndReceivingManager.sendMessage(data, messageFactory);
                repository.update(data);
            } catch(ServiceException ex) {
                // TODO
                unsentDataList.add(data);
            }
        }
        dataList.removeAll(unsentDataList);
        return unsentDataList;
    }
}
