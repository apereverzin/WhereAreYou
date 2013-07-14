package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
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
    
    public void receiveRequestsAndResponses() throws TransformException, ServiceException {
        Log.d(TAG, "receiveRequestsAndResponses() " + Thread.currentThread().getId());
        Set<GenericMessage> receivedMessages = emailSendingAndReceivingManager.receiveMessages();
        Log.d(TAG, "--------------receiveRequestsAndResponses: " + Thread.currentThread().getId() + " " + receivedMessages.size());
        if (receivedMessages.size() > 0) {
            List<GenericMessage> messagesToPersist = new ArrayList<GenericMessage>();
            List<ContactCompoundId> contactCompoundIdsToPersist = new ArrayList<ContactCompoundId>();
            retrieveContactDataForMessages(receivedMessages, messagesToPersist, contactCompoundIdsToPersist);
            
            if (messagesToPersist.size() > 0) {
                MessagePersistenceManager messagePersistenceManager = new MessagePersistenceManager();
                messagePersistenceManager.persistReceivedMessages(messagesToPersist, contactCompoundIdsToPersist);
            }
        }
    }

    private void retrieveContactDataForMessages(Set<GenericMessage> receivedMessages, List<GenericMessage> messagesToPersist, List<ContactCompoundId> contactCompoundIdsToPersist) {
        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            for (GenericMessage message : receivedMessages) {
                String contactEmail = message.getSenderEmail();
                ContactData contactData = SQLiteRepositoryManager.getInstance().getContactDataRepository().getContactDataByEmail(contactEmail);
                Log.d(TAG, "--------------contactData: " + Thread.currentThread().getId() + " " + contactData);
                if (contactData != null && contactData.isRequestAllowed()) {
                    messagesToPersist.add(message);
                    contactCompoundIdsToPersist.add(contactData.getContactCompoundId());
                }
            }
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }
}
