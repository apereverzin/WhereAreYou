package com.creek.whereareyou.android.contacts;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.sendable.AbstractRequestResponse;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class RequestResponseFactory {
    private static final RequestResponseFactory instance = new RequestResponseFactory();
    
    private RequestResponseFactory() {
        //
    }
    
    public static RequestResponseFactory getInstance() {
        return instance;
    }
    
    public ContactRequest createContactLocationRequest(AndroidContact contact) {
        ContactRequest contactRequest = new ContactRequest();
        ContactCompoundId contactCompoundId = new ContactCompoundId(contact.getId(), contact.getEmail());
        contactRequest.setContactCompoundId(contactCompoundId);
        contactRequest.setCode(AbstractRequestResponse.LOCATION_REQUEST_CODE);
        contactRequest.setMessage("");
        contactRequest.setTimeCreated(System.currentTimeMillis());
        contactRequest.setTimeSent(0L);
        contactRequest.setTimeReceived(0L);
        return contactRequest;
    }
}
