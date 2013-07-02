package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.service.ServiceException;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSender {
    private static final String TAG = EmailSender.class.getSimpleName();
    private final EmailSendingAndReceivingManager emailSendingAndReceivingManager;
    
    public EmailSender(EmailSendingAndReceivingManager emailSendingAndReceivingManager) throws IOException, CryptoException {
        this.emailSendingAndReceivingManager = emailSendingAndReceivingManager;
    }
    
    public void sendRequestsAndResponses() {
        Log.d(TAG, "sendRequestsAndResponses()");
        List<ContactRequest> failedRequests = sendRequests();
        List<ContactResponse> failedResponses = sendResponses();
        // TODO do something with unsent data
    }

    private List<ContactRequest> sendRequests() {
        Log.d(TAG, "sendRequests()");
        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        
        List<ContactRequest> unsentRequests = contactRequestRepository.getUnsentContactRequests();
        List<ContactRequest> unsentDataList = new ArrayList<ContactRequest>();
        System.out.println("--------------sendRequests: " + unsentRequests.size());
        for (int i = 0; i < unsentRequests.size(); i++) {
            ContactRequest data = unsentRequests.get(i);
            try {
                System.out.println("--------------sending request: " + data);
                emailSendingAndReceivingManager.sendRequest(data);
                contactRequestRepository.update(data);
                System.out.println("--------------request sent: " + data);
            } catch(ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        unsentRequests.removeAll(unsentDataList);
        return unsentDataList;
    }

    private List<ContactResponse> sendResponses() {
        Log.d(TAG, "sendResponses()");
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        List<ContactResponseEntity> unsentResponses = (List)contactResponseRepository.getUnsentContactResponses();
        List<ContactResponse> unsentDataList = new ArrayList<ContactResponse>();
        System.out.println("--------------sendResponses: " + unsentResponses.size());
        for (int i = 0; i < unsentResponses.size(); i++) {
            ContactResponseEntity data = unsentResponses.get(i);
            try {
                System.out.println("--------------sending response : " + data);
                emailSendingAndReceivingManager.sendResponse(data);
                contactResponseRepository.update(data);
                System.out.println("--------------response sent: " + data);
            } catch(ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        unsentResponses.removeAll(unsentDataList);
        return unsentDataList;
    }
}
