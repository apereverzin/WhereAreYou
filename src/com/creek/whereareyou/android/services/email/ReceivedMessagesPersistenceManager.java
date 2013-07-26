package com.creek.whereareyou.android.services.email;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.LOCATION_RESPONSE_TYPE;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.NORMAL_RESPONSE_TYPE;

import java.util.List;

import android.util.Log;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.notifier.ReceivedMessages;
import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.RequestCode;
import com.creek.whereareyoumodel.domain.sendable.ResponseCode;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;
import com.creek.whereareyoumodel.repository.LocationRepository;
import com.creek.whereareyoumodel.valueobject.OwnerRequest;
import com.creek.whereareyoumodel.valueobject.OwnerResponse;
import com.creek.whereareyoumodel.valueobject.SendableLocationData;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ReceivedMessagesPersistenceManager {
    private static final String TAG = ReceivedMessagesPersistenceManager.class.getSimpleName();

    public ReceivedMessages persistReceivedMessages(List<GenericMessage> messagesToPersist, List<ContactCompoundId> contactDataToPersist, ReceivedMessages receivedMessages) {
        Log.d(TAG, "persistReceivedMessages()");
        Log.d(TAG, "--------------persistReceivedMessages()");

        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
            ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
            LocationRepository locationRepository = SQLiteRepositoryManager.getInstance().getLocationRepository();
            
            Log.d(TAG, "--------------messagesToPersist.size(): " + messagesToPersist.size());
            for (int i = 0; i < messagesToPersist.size(); i++) {
                ContactCompoundId contactCompoundId = contactDataToPersist.get(i);
                GenericMessage message = messagesToPersist.get(i);

                Log.d(TAG, "--------------message: " + message);
                if (message instanceof RequestMessage) {
                    Log.d(TAG, "--------------RequestMessage: " + message);
                    persistReceivedContactRequest(contactRequestRepository, contactCompoundId, (RequestMessage) message);
                    receivedMessages.addRequest((RequestMessage) message);
                } else if (message instanceof ResponseMessage) {
                    Log.d(TAG, "--------------ResponseMessage: " + message);
                    persistReceivedNormalContactResponse(contactResponseRepository, contactCompoundId, (ResponseMessage) message);
                    receivedMessages.addNormalResponse((ResponseMessage) message);
                } else if (message instanceof OwnerLocationDataMessage) {
                    Log.d(TAG, "--------------OwnerLocationDataMessage: " + message);
                    persistReceivedLocationData(locationRepository, contactResponseRepository, contactCompoundId, (OwnerLocationDataMessage) message);
                    receivedMessages.addLocationResponse((OwnerLocationDataMessage) message);
                }
                Log.d(TAG, "--------------message persisted");
            }
            
            return receivedMessages;
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }
    
    private void persistReceivedContactRequest(ContactRequestRepository contactRequestRepository, ContactCompoundId contactCompoundId, RequestMessage message) {
        Log.d(TAG, "persistContactRequest: " + contactCompoundId + ", " + message);
        OwnerRequest ownerRequest = message.getOwnerRequest();
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setContactCompoundId(contactCompoundId);
        contactRequest.setTimeCreated(UNDEFINED_LONG);
        contactRequest.setTimeSent(ownerRequest.getTimeSent());
        contactRequest.setTimeReceived(System.currentTimeMillis());
        contactRequest.setRequestCode(RequestCode.getRequestCode(ownerRequest.getRequestCode()));
        contactRequest.setMessage(ownerRequest.getMessage());
        contactRequest.setProcessed(false);
        contactRequestRepository.create(contactRequest);
    }
    
    private void persistReceivedNormalContactResponse(ContactResponseRepository<ContactResponseEntity> contactResponseRepository, ContactCompoundId contactCompoundId, ResponseMessage message) {
        Log.d(TAG, "persistNormalContactResponse: " + contactCompoundId + ", " + message);
        Log.d(TAG, "--------------persistNormalContactResponse: " + contactCompoundId + ", " + message);
        OwnerResponse ownerResponse = message.getOwnerResponse();
        persistReceivedContactResponseEntity(contactResponseRepository, contactCompoundId, NORMAL_RESPONSE_TYPE, -1, ownerResponse.getTimeSent(), ownerResponse.getResponseCode(), ownerResponse.getMessage());
    }

    private void persistReceivedLocationData(LocationRepository locationRepository, ContactResponseRepository<ContactResponseEntity> contactResponseRepository, ContactCompoundId contactCompoundId, OwnerLocationDataMessage message) {
        Log.d(TAG, "persistLocationData: " + contactCompoundId + ", " + message);
        Log.d(TAG, "--------------persistLocationData: " + contactCompoundId + ", " + message);
        SendableLocationData sendableLocationData = message.getOwnerLocationData();
        LocationData locationData = sendableLocationData.getLocationData();
        locationData.setContactCompoundId(contactCompoundId);
        locationData = (LocationData)locationRepository.create(locationData);
        persistReceivedContactResponseEntity(contactResponseRepository, contactCompoundId, LOCATION_RESPONSE_TYPE, locationData.getId(), System.currentTimeMillis(), ResponseCode.SUCCESS.getCode(), "");
    }
    
    private void persistReceivedContactResponseEntity(ContactResponseRepository<ContactResponseEntity> contactResponseRepository, ContactCompoundId contactCompoundId, int responseType, long locationId, long timeSent, int code, String message) {
        Log.d(TAG, "persistContactResponseEntity: " + contactCompoundId);
        Log.d(TAG, "--------------persistContactResponseEntity: " + contactCompoundId);
        ContactResponseEntity contactResponseEntity = new ContactResponseEntity();
        contactResponseEntity.setContactCompoundId(contactCompoundId);
        contactResponseEntity.setTimeCreated(UNDEFINED_LONG);
        contactResponseEntity.setTimeSent(timeSent);
        contactResponseEntity.setTimeReceived(System.currentTimeMillis());
        contactResponseEntity.setResponseCode(ResponseCode.getResponseCode(code));
        contactResponseEntity.setMessage(message);
        contactResponseEntity.setProcessed(false);
        contactResponseEntity.setType(responseType);
        contactResponseEntity.setLocationDataId(locationId);
        contactResponseEntity.setRequestId(UNDEFINED_INT);
        contactResponseRepository.create(contactResponseEntity);
    }
}
