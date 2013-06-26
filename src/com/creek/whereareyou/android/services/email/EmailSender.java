package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.service.ServiceException;

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
        List<ContactRequest> unsentRequests = SQLiteRepositoryManager.getInstance().getContactRequestRepository().getUnsentContactRequests();
        List<ContactRequest> unsentDataList = new ArrayList<ContactRequest>();
        for (int i = 0; i < unsentRequests.size(); i++) {
            ContactRequest data = unsentRequests.get(i);
            try {
                emailSendingAndReceivingManager.sendRequest(data);
                SQLiteRepositoryManager.getInstance().getContactRequestRepository().update(data);
            } catch(ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        unsentRequests.removeAll(unsentDataList);
        return unsentDataList;
    }

    private List<ContactResponse> sendResponses() {
        ContactResponseRepository contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
        
        List<ContactResponseEntity> unsentResponses = SQLiteRepositoryManager.getInstance().getContactResponseRepository().getUnsentContactResponses();
        List<ContactResponse> unsentDataList = new ArrayList<ContactResponse>();
        for (int i = 0; i < unsentResponses.size(); i++) {
            ContactResponse data = unsentResponses.get(i);
            try {
                emailSendingAndReceivingManager.sendResponse(data);
                SQLiteRepositoryManager.getInstance().getContactResponseRepository().update(data);
            } catch(ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        unsentResponses.removeAll(unsentDataList);
        return unsentDataList;
    }
}
