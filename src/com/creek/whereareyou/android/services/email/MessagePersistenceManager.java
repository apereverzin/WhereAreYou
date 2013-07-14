package com.creek.whereareyou.android.services.email;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.LOCATION_RESPONSE_TYPE;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.NORMAL_RESPONSE_TYPE;

import java.util.List;

import android.util.Log;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
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
public class MessagePersistenceManager {
    private static final String TAG = MessagePersistenceManager.class.getSimpleName();

    public void persistReceivedMessages(List<GenericMessage> messagesToPersist, List<ContactCompoundId> contactDataToPersist) {
        Log.d(TAG, "persistReceivedMessages() " + Thread.currentThread().getId());

        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
            ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();
            LocationRepository locationRepository = SQLiteRepositoryManager.getInstance().getLocationRepository();
            for (int i = 0; i < messagesToPersist.size(); i++) {
                ContactCompoundId contactCompoundId = contactDataToPersist.get(i);
                GenericMessage message = messagesToPersist.get(i);

                if (message instanceof RequestMessage) {
                    persistContactRequest(contactRequestRepository, contactCompoundId, (RequestMessage) message);
                } else if (message instanceof ResponseMessage) {
                    persistNormalContactResponse(contactResponseRepository, contactCompoundId, (ResponseMessage) message);
                } else if (message instanceof OwnerLocationDataMessage) {
                    persistLocationData(locationRepository, contactResponseRepository, contactCompoundId, (OwnerLocationDataMessage) message);
                }
                Log.d(TAG, "--------------message persisted " + Thread.currentThread().getId());
            }
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }
    }
    
    private void persistContactRequest(ContactRequestRepository contactRequestRepository, ContactCompoundId contactCompoundId, RequestMessage message) {
        Log.d(TAG, "persistContactRequest: " + Thread.currentThread().getId() + " " + contactCompoundId + ", " + message);
        OwnerRequest ownerRequest = message.getOwnerRequest();
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setContactCompoundId(contactCompoundId);
        contactRequest.setTimeSent(ownerRequest.getTimeSent());
        contactRequest.setTimeReceived(System.currentTimeMillis());
        contactRequest.setRequestCode(RequestCode.getRequestCode(ownerRequest.getRequestCode()));
        contactRequest.setMessage(ownerRequest.getMessage());
        contactRequest.setProcessed(false);
        contactRequestRepository.create(contactRequest);
    }
    
    private void persistNormalContactResponse(ContactResponseRepository<ContactResponseEntity> contactResponseRepository, ContactCompoundId contactCompoundId, ResponseMessage message) {
        Log.d(TAG, "persistNormalContactResponse: " + Thread.currentThread().getId() + " " + contactCompoundId + ", " + message);
        Log.d(TAG, "--------------persistNormalContactResponse: " + Thread.currentThread().getId() + " " + contactCompoundId + ", " + message);
        OwnerResponse ownerResponse = message.getOwnerResponse();
        persistContactResponseEntity(contactResponseRepository, contactCompoundId, NORMAL_RESPONSE_TYPE, -1, ownerResponse.getTimeSent(), ownerResponse.getResponseCode(), ownerResponse.getMessage());
    }

    private void persistLocationData(LocationRepository locationRepository, ContactResponseRepository<ContactResponseEntity> contactResponseRepository, ContactCompoundId contactCompoundId, OwnerLocationDataMessage message) {
        Log.d(TAG, "persistLocationData: " + Thread.currentThread().getId() + " " + contactCompoundId + ", " + message);
        Log.d(TAG, "--------------persistLocationData: " + Thread.currentThread().getId() + " " + contactCompoundId + ", " + message);
        SendableLocationData sendableLocationData = message.getOwnerLocationData();
        LocationData locationData = sendableLocationData.getLocationData();
        locationData.setContactCompoundId(contactCompoundId);
        locationData = (LocationData)locationRepository.create(locationData);
        persistContactResponseEntity(contactResponseRepository, contactCompoundId, LOCATION_RESPONSE_TYPE, locationData.getId(), System.currentTimeMillis(), ResponseCode.SUCCESS.getCode(), "");
    }
    
    private void persistContactResponseEntity(ContactResponseRepository<ContactResponseEntity> contactResponseRepository, ContactCompoundId contactCompoundId, int responseType, long locationId, long timeSent, int code, String message) {
        Log.d(TAG, "persistContactResponseEntity: " + Thread.currentThread().getId() + " " + contactCompoundId);
        Log.d(TAG, "--------------persistContactResponseEntity: " + Thread.currentThread().getId() + " " + contactCompoundId);
        ContactResponseEntity contactResponseEntity = new ContactResponseEntity();
        contactResponseEntity.setContactCompoundId(contactCompoundId);
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
