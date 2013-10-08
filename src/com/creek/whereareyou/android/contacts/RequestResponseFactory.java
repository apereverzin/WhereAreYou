package com.creek.whereareyou.android.contacts;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.RequestCode;

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
        ContactCompoundId contactCompoundId = new ContactCompoundId(contact.getContactId(), contact.getContactData().getContactEmail());
        contactRequest.setContactCompoundId(contactCompoundId);
        contactRequest.setRequestCode(RequestCode.LOCATION);
        contactRequest.setMessage("");
        contactRequest.setTimeCreated(System.currentTimeMillis());
        contactRequest.setTimeSent(UNDEFINED_LONG);
        contactRequest.setTimeReceived(UNDEFINED_LONG);
        return contactRequest;
    }
}
