package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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

    public void sendRequestsAndResponses(Context ctx) {
        Log.d(TAG, "sendRequestsAndResponses()");
        List<ContactRequest> failedRequests = sendRequests(ctx);
        List<ContactResponse> failedResponses = sendResponses(ctx);
        // TODO do something with unsent data
    }

    private List<ContactRequest> sendRequests(Context ctx) {
        Log.d(TAG, "sendRequests()");
        List<ContactRequest> unsentDataList = new ArrayList<ContactRequest>();
        try {
            SQLiteRepositoryManager.getInstance().initialise(ctx);
            ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();

            List<ContactRequest> unsentRequests = contactRequestRepository.getUnsentContactRequests();
            Log.d(TAG, "--------------sendRequests: " + unsentRequests.size());
            for (int i = 0; i < unsentRequests.size(); i++) {
                ContactRequest data = unsentRequests.get(i);
                try {
                    Log.d(TAG, "--------------sending request: " + data);
                    emailSendingAndReceivingManager.sendRequest(data);
                    contactRequestRepository.update(data);
                    Log.d(TAG, "--------------request sent: " + data);
                } catch (ServiceException ex) {
                    unsentDataList.add(data);
                }
            }
            unsentRequests.removeAll(unsentDataList);
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
        return unsentDataList;
    }

    private List<ContactResponse> sendResponses(Context ctx) {
        Log.d(TAG, "sendResponses()");
        List<ContactResponse> unsentDataList = new ArrayList<ContactResponse>();

        try {
            SQLiteRepositoryManager.getInstance().initialise(ctx);
            ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();

            @SuppressWarnings({ "unchecked", "rawtypes" })
            List<ContactResponseEntity> unsentResponses = (List) contactResponseRepository.getUnsentContactResponses();
            Log.d(TAG, "--------------sendResponses: " + unsentResponses.size());
            for (int i = 0; i < unsentResponses.size(); i++) {
                ContactResponseEntity data = unsentResponses.get(i);
                try {
                    Log.d(TAG, "--------------sending response : " + data);
                    emailSendingAndReceivingManager.sendResponse(data);
                    contactResponseRepository.update(data);
                    Log.d(TAG, "--------------response sent: " + data);
                } catch (ServiceException ex) {
                    unsentDataList.add(data);
                }
            }
            unsentResponses.removeAll(unsentDataList);
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
        return unsentDataList;
    }
}
