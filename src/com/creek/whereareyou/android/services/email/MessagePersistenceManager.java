package com.creek.whereareyou.android.services.email;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_INT;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.LOCATION_RESPONSE_TYPE;
import static com.creek.whereareyou.android.infrastructure.sqlite.SQLiteContactResponseRepository.NORMAL_RESPONSE_TYPE;
import static com.creek.whereareyoumodel.domain.sendable.AbstractSendable.SUCCESS;

import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.message.GenericMessage;
import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;
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
        OwnerRequestResponse ownerRequestResponse = message.getOwnerRequestResponse();
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setContactCompoundId(contactData.getContactCompoundId());
        contactRequest.setTimeSent(ownerRequestResponse.getTimeSent());
        contactRequest.setTimeReceived(System.currentTimeMillis());
        contactRequest.setCode(ownerRequestResponse.getCode());
        contactRequest.setMessage(ownerRequestResponse.getMessage());
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
        persistContactResponseEntity(contactData, LOCATION_RESPONSE_TYPE, locationData.getId(), System.currentTimeMillis(), SUCCESS, "");
    }
    
    private void persistContactResponse(ContactData contactData, ResponseMessage message, int responseType) {
        persistContactResponse(contactData, message, responseType, -1);
    }
    
    private void persistContactResponse(ContactData contactData, ResponseMessage message, int responseType, int locationId) {
        OwnerRequestResponse ownerRequestResponse = message.getOwnerRequestResponse();
        persistContactResponseEntity(contactData, responseType, locationId, ownerRequestResponse.getTimeSent(), ownerRequestResponse.getCode(), ownerRequestResponse.getMessage());
    }
    
    private void persistContactResponseEntity(ContactData contactData, int responseType, int locationId, long timeSent, int code, String message) {
        ContactResponseEntity contactResponseEntity = new ContactResponseEntity();
        contactResponseEntity.setContactCompoundId(contactData.getContactCompoundId());
        contactResponseEntity.setTimeSent(timeSent);
        contactResponseEntity.setTimeReceived(System.currentTimeMillis());
        contactResponseEntity.setCode(code);
        contactResponseEntity.setMessage(message);
        contactResponseEntity.setProcessed(false);
        contactResponseEntity.setType(responseType);
        contactResponseEntity.setLocationId(locationId);
        contactResponseEntity.setRequestId(UNDEFINED_INT);
        SQLiteRepositoryManager.getInstance().getContactResponseRepository().create(contactResponseEntity);
    }
}
