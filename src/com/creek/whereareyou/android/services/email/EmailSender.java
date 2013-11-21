package com.creek.whereareyou.android.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.creek.whereareyou.WhereAreYouApplication;
import com.creek.whereareyou.android.activity.contacts.ContactsActivity;
import com.creek.whereareyou.android.activity.contacts.OutgoingState;
import com.creek.whereareyou.android.db.ContactResponseEntity;

import static com.creek.whereareyou.android.activity.contacts.OutgoingState.SENT;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.repository.LocationRepository;
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
        List<ContactRequest> unsentDataList = new ArrayList<ContactRequest>();
        ContactRequestRepository contactRequestRepository;
        List<ContactRequest> unsentRequests;

        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();

            unsentRequests = contactRequestRepository.getUnsentContactRequests();
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }

        Log.d(TAG, "--------------sendRequests: " + unsentRequests.size());
        for (int i = 0; i < unsentRequests.size(); i++) {
            final ContactRequest data = unsentRequests.get(i);
            try {
                Log.d(TAG, "--------------sending request: " + data);
                emailSendingAndReceivingManager.sendRequest(data);

                try {
                    SQLiteRepositoryManager.getInstance().openDatabase();
                    contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();

                    contactRequestRepository.update(data);
                    
                    final ContactsActivity contactsActivity = WhereAreYouApplication.getContactsActivity();
                    
                    Log.d(TAG, "--------------about to set outgoing request state");
                    if (contactsActivity != null) {
                        Log.d(TAG, "--------------outgoing request state will be set");
                        contactsActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                contactsActivity.setOutgoingRequestState(data.getContactCompoundId().getContactId(), SENT);
                                Log.d(TAG, "--------------outgoing request state is set");
                            }
                        });
                    }
                    
                    Log.d(TAG, "--------------request sent: " + data);
                } finally {
                    SQLiteRepositoryManager.getInstance().closeDatabase();
                }
            } catch (ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        unsentRequests.removeAll(unsentDataList);
        return unsentDataList;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<ContactResponse> sendResponses() {
        Log.d(TAG, "sendResponses()");
        List<ContactResponse> unsentDataList = new ArrayList<ContactResponse>();
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository;
        LocationRepository locationRepository;
        List<ContactResponseEntity> unsentResponses;

        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
            locationRepository = SQLiteRepositoryManager.getInstance().getLocationRepository();

            unsentResponses = (List) contactResponseRepository.getUnsentContactResponses();
            
            // TODO use JOIN in the repository instead of this
            for (int i = 0; i < unsentResponses.size(); i++) {
                ContactResponseEntity data = unsentResponses.get(i);
                if (data.getLocationId() != UNDEFINED_LONG) {
                    LocationData locationData = locationRepository.getLocationDataById(data.getLocationId());
                    data.setLocationData(locationData);
                }
            }
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }

        Log.d(TAG, "--------------sendResponses: " + unsentResponses.size());
        for (int i = 0; i < unsentResponses.size(); i++) {
            ContactResponseEntity data = unsentResponses.get(i);
            try {
                Log.d(TAG, "--------------sending response : " + data);
                emailSendingAndReceivingManager.sendResponse(data);

                try {
                    SQLiteRepositoryManager.getInstance().openDatabase();
                    contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
                    
                    contactResponseRepository.update(data);
                    Log.d(TAG, "--------------response sent: " + data);
                } finally {
                    SQLiteRepositoryManager.getInstance().closeDatabase();
                }
            } catch (ServiceException ex) {
                unsentDataList.add(data);
            }
        }
        unsentResponses.removeAll(unsentDataList);
        return unsentDataList;
    }
}
