package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import android.util.Log;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;

import com.creek.accessemail.connector.mail.MailConnector;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.message.TransformException;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.service.MessagesService;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequest;
import com.creek.whereareyoumodel.valueobject.OwnerResponse;
import com.creek.whereareyoumodel.valueobject.SendableLocationData;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSendingAndReceivingManager {
    private static final String TAG = EmailSendingAndReceivingManager.class.getSimpleName();
    
    private final MessagesService messagesService;
    private final Properties mailProps;
    
    public EmailSendingAndReceivingManager() throws CryptoException, IOException {
        mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
        messagesService = new MessagesService(new MailConnector(mailProps));
    }

    public EmailSendingAndReceivingManager(Properties props) throws CryptoException, IOException {
        props.put(MAIL_USERNAME_PROPERTY, "andrey.pereverzin");
        props.put(MAIL_PASSWORD_PROPERTY, "bertoluCCi");
        MailAccountPropertiesProvider.getInstance().persistMailProperties(props);
        
        mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
        messagesService = new MessagesService(new MailConnector(mailProps));
    }

    public void sendRequest(ContactRequest contactRequest) throws ServiceException {
        Log.d(TAG, "sendRequest()");
        contactRequest.setTimeSent(System.currentTimeMillis());
        OwnerRequest payload = new OwnerRequest(contactRequest);
        RequestMessage message = new RequestMessage(payload, mailProps.getProperty(MAIL_USERNAME_PROPERTY));
        Log.d(TAG, "--------------sendRequest: " + message.toJSON());
        Log.d(TAG, "--------------sendRequest: " + contactRequest.getContactCompoundId().getContactEmail());
        messagesService.sendMessage(message, contactRequest.getContactCompoundId().getContactEmail());
        Log.d(TAG, "--------------sendRequest: " + message.toJSON());
    }

    public void sendResponse(ContactResponse contactResponse) throws ServiceException {
        Log.d(TAG, "sendResponse()");
        
        AbstractMessage message;
        if (contactResponse.getLocationData() != null) {
            SendableLocationData sendableLocationData = new SendableLocationData(contactResponse.getLocationData());
            message = new OwnerLocationDataMessage(sendableLocationData, mailProps.getProperty(MAIL_USERNAME_PROPERTY));
        } else {
            OwnerResponse payload = new OwnerResponse(contactResponse);
            message = new ResponseMessage(payload, mailProps.getProperty(MAIL_USERNAME_PROPERTY));
        }
        
        contactResponse.setTimeSent(System.currentTimeMillis());
        Log.d(TAG, "--------------sendResponse: " + message.toJSON());
        Log.d(TAG, "--------------sendResponse: " + contactResponse.getContactCompoundId().getContactEmail());
        messagesService.sendMessage(message, contactResponse.getContactCompoundId().getContactEmail());
        Log.d(TAG, "--------------sendResponse: " + message.toJSON());
    }
    
    public Set<GenericMessage> receiveMessages() throws ServiceException, TransformException {
        Log.d(TAG, "receiveMessages()");
        Set<GenericMessage> messages = messagesService.receiveMessages();
        Log.d(TAG, "--------------receiveMessages: " + messages.size());
        return messages;
    }
}
