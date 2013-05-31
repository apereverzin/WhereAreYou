package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import android.accounts.Account;

import com.creek.accessemail.connector.mail.PredefinedMailProperties;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.ContactRequest;
import com.creek.whereareyoumodel.domain.ContactResponse;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.service.MessagesService;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequest;
import com.creek.whereareyoumodel.valueobject.OwnerResponse;

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

    public void sendContactRequests(List<ContactRequest> contactRequests) throws ServiceException {
        for(int i = 0; i < contactRequests.size(); i++) {
            ContactRequest contactRequest = contactRequests.get(i);
            contactRequest.setTimeSent(System.currentTimeMillis());
            OwnerRequest ownerRequest = new OwnerRequest(contactRequest.getTimeSent(), contactRequest.getCode(), contactRequest.getMessage());
            RequestMessage ownerRequestMessage = new RequestMessage(ownerRequest, account.name);
            messagesService.sendMessage(ownerRequestMessage, contactRequest.getContactCompoundId().getContactEmail());
        }
    }

    public void sendContactResponses(List<ContactResponse> contactResponses) throws ServiceException {
        for(int i = 0; i < contactResponses.size(); i++) {
            ContactResponse contactResponse = contactResponses.get(i);
            contactResponse.setTimeSent(System.currentTimeMillis());
            OwnerResponse ownerResponse = new OwnerResponse(contactResponse.getTimeSent(), contactResponse.getCode(), contactResponse.getMessage());
            ResponseMessage ownerResponseMessage = new ResponseMessage(ownerResponse, account.name);
            messagesService.sendMessage(ownerResponseMessage, contactResponse.getContactCompoundId().getContactEmail());
        }
    }
}
