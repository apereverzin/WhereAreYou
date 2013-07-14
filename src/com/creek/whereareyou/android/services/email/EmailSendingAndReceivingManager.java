package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import android.accounts.Account;
import android.util.Log;

import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.message.TransformException;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.service.MessagesService;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequest;
import com.creek.whereareyoumodel.valueobject.OwnerResponse;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSendingAndReceivingManager {
    private static final String TAG = EmailSendingAndReceivingManager.class.getSimpleName();
    
    private final Account account;
    private final MessagesService messagesService;
    
    public EmailSendingAndReceivingManager(Account account) throws CryptoException, IOException {
        this.account = account;
        Properties props = PredefinedMailProperties.getPredefinedPropertiesForServer("gmail");
        props.put("mail.username", "andrey.pereverzin");
        props.put("mail.password", "bertoluCCi");
        MailAccountPropertiesProvider.getInstance().persistMailProperties(props);
        
        Properties mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
        messagesService = new MessagesService(mailProps);
    }

    public void sendRequest(ContactRequest contactRequest) throws ServiceException {
        Log.d(TAG, "sendRequest() " + Thread.currentThread().getId());
        contactRequest.setTimeSent(System.currentTimeMillis());
        OwnerRequest payload = new OwnerRequest(contactRequest);
        RequestMessage message = new RequestMessage(payload, account.name);
        Log.d(TAG, "--------------sendRequest: " + Thread.currentThread().getId() + " " + message.toJSON());
        Log.d(TAG, "--------------sendRequest: " + Thread.currentThread().getId() + " " + contactRequest.getContactCompoundId().getContactEmail());
        messagesService.sendMessage(message, contactRequest.getContactCompoundId().getContactEmail());
        Log.d(TAG, "--------------sendRequest: " + Thread.currentThread().getId() + " " + message.toJSON());
    }

    public void sendResponse(ContactResponse contactResponse) throws ServiceException {
        Log.d(TAG, "sendResponse() " + Thread.currentThread().getId());
        contactResponse.setTimeSent(System.currentTimeMillis());
        OwnerResponse payload = new OwnerResponse(contactResponse);
        ResponseMessage message = new ResponseMessage(payload, account.name);
        Log.d(TAG, "--------------sendResponse: " + Thread.currentThread().getId() + " " + message.toJSON());
        Log.d(TAG, "--------------sendResponse: " + Thread.currentThread().getId() + " " + contactResponse.getContactCompoundId().getContactEmail());
        messagesService.sendMessage(message, contactResponse.getContactCompoundId().getContactEmail());
        Log.d(TAG, "--------------sendResponse: " + Thread.currentThread().getId() + " " + message.toJSON());
    }
    
    public Set<GenericMessage> receiveMessages() throws ServiceException, TransformException {
        Log.d(TAG, "receiveMessages() " + Thread.currentThread().getId());
        Set<GenericMessage> messages = messagesService.receiveMessages();
        Log.d(TAG, "--------------receiveMessages: " + Thread.currentThread().getId() + " " + messages.size());
        return messages;
    }
}
