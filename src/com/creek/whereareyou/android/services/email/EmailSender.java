package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.domain.sendable.GenericRequestResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.repository.IdentifiableRepository;
import com.creek.whereareyoumodel.service.ServiceException;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSender {
    private final EmailSendingAndReceivingManager emailSendingAndReceivingManager;
    
    public EmailSender(EmailSendingAndReceivingManager emailSendingAndReceivingManager) throws IOException, CryptoException {
        this.emailSendingAndReceivingManager = emailSendingAndReceivingManager;
    }
    
    public void sendRequestsAndResponses() {
        List<ContactRequest> failedRequests = sendRequests();
        List<ContactResponse> failedResponses = sendResponses();
        // TODO do something with unsent data
    }

    private List<ContactRequest> sendRequests() {
        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        
        List<ContactRequest> unsentRequests = SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnsentContactRequests();
        return sendGenericRequestsResponses(contactRequestRepository, emailSendingAndReceivingManager, unsentRequests, new RequestMessageFactory());
    }

    private List<ContactResponse> sendResponses() {
        ContactResponseRepository contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
        
        List<ContactResponseEntity> unsentResponses = SQLiteRepositoryManager.getInstance().getContactResponseRepository().getUnsentContactResponses();
        return sendGenericRequestsResponses(contactResponseRepository, emailSendingAndReceivingManager, unsentResponses, new ResponseMessageFactory());
    }
    
    private <T extends GenericRequestResponse> List<T> sendGenericRequestsResponses(IdentifiableRepository<T> repository, EmailSendingAndReceivingManager emailSendingAndReceivingManager, List<T> dataList, MessageFactory<OwnerRequestResponse> messageFactory) {
        List<T> unsentDataList = new ArrayList<T>();
        for (int i = 0; i < dataList.size(); i++) {
            T data = dataList.get(i);
            try {
                emailSendingAndReceivingManager.sendMessage(data, messageFactory);
                repository.update(data);
            } catch(ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        dataList.removeAll(unsentDataList);
        return unsentDataList;
    }
}
