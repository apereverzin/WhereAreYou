package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import android.accounts.Account;
import android.util.Log;

import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.TransformException;
import com.creek.whereareyoumodel.domain.sendable.GenericRequestResponse;
import com.creek.whereareyoumodel.service.MessagesService;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;

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

    public <T extends GenericRequestResponse> void sendMessage(T data, MessageFactory<OwnerRequestResponse> messageFactory) throws ServiceException {
        data.setTimeSent(System.currentTimeMillis());
        OwnerRequestResponse payload = new OwnerRequestResponse(data);
        AbstractMessage message = messageFactory.createMessage(payload, account.name);
        Log.d(TAG, "--------------sendMessage: " + message.toJSON());
        Log.d(TAG, "--------------sendMessage: " + data.getContactCompoundId().getContactEmail());
        messagesService.sendMessage(message, data.getContactCompoundId().getContactEmail());
        Log.d(TAG, "--------------sendMessage: " + message.toJSON());
    }
    
    public Set<GenericMessage> receiveMessages() throws ServiceException, TransformException {
        Set<GenericMessage> messages = messagesService.receiveMessages();
        return messages;
    }
}
