package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import android.accounts.Account;

import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.RequestResponse;
import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.service.MessagesService;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;

/**
 * 
 * @author andreypereverzin
 */
public class EmailSendingReceivingManager {
    private final Account account;
    private final MessagesService messagesService;
    
    public EmailSendingReceivingManager(Account account) throws CryptoException, IOException {
        this.account = account;
        Properties props = PredefinedMailProperties.getPredefinedPropertiesForServer("gmail");
        props.put("mail.username", "andrey.pereverzin");
        props.put("mail.password", "xxxxxxxx");
        MailAccountPropertiesProvider.getInstance().persistMailProperties(props);
        
        Properties mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
        messagesService = new MessagesService(mailProps);
    }

    public void sendMessages(List<RequestResponse> contactRequests, MessageFactory messageFactory) throws ServiceException {
        for(int i = 0; i < contactRequests.size(); i++) {
            RequestResponse contactRequest = contactRequests.get(i);
            contactRequest.setTimeSent(System.currentTimeMillis());
            OwnerRequestResponse ownerRequest = new OwnerRequestResponse(contactRequest);
            AbstractMessage message = messageFactory.createMessage(ownerRequest, account.name);
            messagesService.sendMessage(message, contactRequest.getContactCompoundId().getContactEmail());
        }
    }
}
