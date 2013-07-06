package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.TransformException;
import com.creek.whereareyoumodel.service.ServiceException;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailReceiver {
    private static final String TAG = EmailReceiver.class.getSimpleName();
    private final EmailSendingAndReceivingManager emailSendingAndReceivingManager;
    
    public EmailReceiver(EmailSendingAndReceivingManager emailSendingAndReceivingManager) throws IOException, CryptoException {
        this.emailSendingAndReceivingManager = emailSendingAndReceivingManager;
    }
    
    public void receiveRequestsAndResponses(Context ctx) throws TransformException, ServiceException {
        Log.d(TAG, "receiveRequestsAndResponses()");
        Set<GenericMessage> receivedMessages = emailSendingAndReceivingManager.receiveMessages();
        Log.d(TAG, "--------------receiveRequestsAndResponses: " + receivedMessages.size());
        if (receivedMessages.size() > 0) {
            try {
                SQLiteRepositoryManager.getInstance().initialise(ctx);
                MessagePersistenceManager messagePersistenceManager = new MessagePersistenceManager();
                for (GenericMessage message : receivedMessages) {
                    String contactEmail = message.getSenderEmail();
                    ContactData contactData = SQLiteRepositoryManager.getInstance().getContactDataRepository().getContactDataByEmail(contactEmail);
                    Log.d(TAG, "--------------contactData: " + contactData);
                    if (contactData != null && contactData.isRequestAllowed()) {
                        messagePersistenceManager.persistReceivedMessage(contactData, message);
                    }
                }
            } finally {
                SQLiteRepositoryManager.getInstance().closeDatabase();
            }
        }
    }
}
