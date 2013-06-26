package com.creek.whereareyou.android.services.email;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.LOCATION_RESPONSE_TYPE;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.NORMAL_RESPONSE_TYPE;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.RequestCode;
import com.creek.whereareyoumodel.domain.sendable.ResponseCode;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.valueobject.OwnerRequest;
import com.creek.whereareyoumodel.valueobject.OwnerResponse;
import com.creek.whereareyoumodel.valueobject.SendableLocationData;

/**
 * 
 * @author Andrey Pereverzin
 */
public class MessagePersistenceManager {
    public void persistReceivedMessage(ContactData contactData, GenericMessage message) {
        if (contactData != null) {
            if (message instanceof RequestMessage) {
                persistContactRequest(contactData, (RequestMessage) message);
            } else if (message instanceof ResponseMessage) {
                persistNormalContactResponse(contactData, (ResponseMessage) message);
            } else if (message instanceof OwnerLocationDataMessage) {
                persistLocationData(contactData, (OwnerLocationDataMessage) message);
            }
        }
    }
    
    private void persistContactRequest(ContactData contactData, RequestMessage message) {
        OwnerRequest ownerRequest = message.getOwnerRequest();
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setContactCompoundId(contactData.getContactCompoundId());
        contactRequest.setTimeSent(ownerRequest.getTimeSent());
        contactRequest.setTimeReceived(System.currentTimeMillis());
        contactRequest.setRequestCode(RequestCode.getRequestCode(ownerRequest.getRequestCode()));
        contactRequest.setMessage(ownerRequest.getMessage());
        contactRequest.setProcessed(false);
        SQLiteRepositoryManager.getInstance().getContactRequestRepository().create(contactRequest);
    }
    
    private void persistNormalContactResponse(ContactData contactData, ResponseMessage message) {
        persistContactResponse(contactData, message, NORMAL_RESPONSE_TYPE);
    }

    private void persistLocationData(ContactData contactData, OwnerLocationDataMessage message) {
        SendableLocationData sendableLocationData = message.getOwnerLocationData();
        LocationData locationData = sendableLocationData.getLocationData();
        locationData.setContactCompoundId(contactData.getContactCompoundId());
        locationData = (LocationData)SQLiteRepositoryManager.getInstance().getLocationRepository().create(locationData);
        persistContactResponseEntity(contactData, LOCATION_RESPONSE_TYPE, locationData.getId(), System.currentTimeMillis(), ResponseCode.SUCCESS.getCode(), "");
    }
    
    private void persistContactResponse(ContactData contactData, ResponseMessage message, int responseType) {
        persistContactResponse(contactData, message, responseType, -1);
    }
    
    private void persistContactResponse(ContactData contactData, ResponseMessage message, int responseType, int locationId) {
        OwnerResponse ownerResponse = message.getOwnerResponse();
        persistContactResponseEntity(contactData, responseType, locationId, ownerResponse.getTimeSent(), ownerResponse.getResponseCode(), ownerResponse.getMessage());
    }
    
    private void persistContactResponseEntity(ContactData contactData, int responseType, int locationId, long timeSent, int code, String message) {
        ContactResponseEntity contactResponseEntity = new ContactResponseEntity();
        contactResponseEntity.setContactCompoundId(contactData.getContactCompoundId());
        contactResponseEntity.setTimeSent(timeSent);
        contactResponseEntity.setTimeReceived(System.currentTimeMillis());
        contactResponseEntity.setResponseCode(ResponseCode.getResponseCode(code));
        contactResponseEntity.setMessage(message);
        contactResponseEntity.setProcessed(false);
        contactResponseEntity.setType(responseType);
        contactResponseEntity.setLocationId(locationId);
        contactResponseEntity.setRequestId(UNDEFINED_INT);
        SQLiteRepositoryManager.getInstance().getContactResponseRepository().create(contactResponseEntity);
    }
}
