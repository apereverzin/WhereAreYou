package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.android.util.ActivityUtil.showException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.contacts.ContactsPersistenceManager;
import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.RequestAllowance;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;

/**
 * 
 * @author andreypereverzin
 */
public class CombinedContactDataBuilder {
    private final Context ctx;

    private final List<String> outgoingUnsentLocationRequestsEmailAddresses;
    private final List<String> outgoingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> outgoingEmailsEverReceivedLocationResponses;
    private final List<String> incomingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> incomingUnsentLocationResponsesEmailAddresses;
    private final List<String> incomingEmailsEverSentLocationResponses;

    public CombinedContactDataBuilder(Context _ctx) {
        this.ctx = _ctx;

        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();

        outgoingUnsentLocationRequestsEmailAddresses = buildListOfOutgoingUnsentLocationRequestsEmailAddresses(contactRequestRepository);
        outgoingUnrespondedLocationRequestsEmailAddresses = buildListOfOutgoingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        outgoingEmailsEverReceivedLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverReceived();
        incomingUnrespondedLocationRequestsEmailAddresses = buildListOfIncomingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        incomingUnsentLocationResponsesEmailAddresses = buildListOfIncomingUnsentLocationResponsesEmailAddresses(contactResponseRepository);
        incomingEmailsEverSentLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverSent();
    }

    public ArrayList<CombinedContactData> buildCombinedContactDataList() {
        try {
            List<AndroidContact> androidContacts = ContactsPersistenceManager.getInstance().retrieveCombinedContacts(ctx);
            ArrayList<CombinedContactData> combinedContacts = new ArrayList<CombinedContactData>();
            
            for (int i = 0; i < androidContacts.size(); i++) {
                CombinedContactData combinedContact = new CombinedContactData(androidContacts.get(i));
                
                setRequestAllowanceCode(combinedContact);
                setOutgoingRequestStatus(combinedContact);
                setIncomingRequestStatus(combinedContact);
                
                combinedContacts.add(combinedContact);
            }
            
            return combinedContacts;
        } catch (IOException ex) {
            showException(ctx, ex);
            return new ArrayList<CombinedContactData>();
        }

    }

    private void setIncomingRequestStatus(CombinedContactData combinedContact) {
        String emailAddress = combinedContact.getAndroidContact().getContactData().getContactEmail();
        if (incomingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
            combinedContact.setIncomingState(IncomingState.RECEIVED);
        } else if (incomingUnsentLocationResponsesEmailAddresses.contains(emailAddress)) {
            combinedContact.setIncomingState(IncomingState.BEING_SENT);
        } else if (incomingEmailsEverSentLocationResponses.contains(emailAddress)) {
            combinedContact.setIncomingState(IncomingState.SENT);
        } else {
            combinedContact.setIncomingState(IncomingState.NONE);
        }
    }

    private void setOutgoingRequestStatus(CombinedContactData combinedContact) {
        String emailAddress = combinedContact.getAndroidContact().getContactData().getContactEmail();
        if (outgoingUnsentLocationRequestsEmailAddresses.contains(emailAddress)) {
            combinedContact.setOutgoingState(OutgoingState.BEING_SENT);
        } else if (outgoingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
            combinedContact.setOutgoingState(OutgoingState.SENT);
        } else if (outgoingEmailsEverReceivedLocationResponses.contains(emailAddress)) {
            combinedContact.setOutgoingState(OutgoingState.RECEIVED);
        } else {
            combinedContact.setOutgoingState(OutgoingState.NONE);
        }
    }

    private void setRequestAllowanceCode(CombinedContactData combinedContact) {
        int requestAllowanceCode = combinedContact.getAndroidContact().getContactData().getRequestAllowanceCode();
        combinedContact.setRequestAllowance(RequestAllowance.getRequestAllowance(requestAllowanceCode));
    }
    
    private List<String> buildListOfIncomingUnsentLocationResponsesEmailAddresses(ContactResponseRepository<ContactResponseEntity> contactResponseRepository) {
        List<String> l = new ArrayList<String>();
        
        List<ContactResponse> unsentResponses = (List<ContactResponse>) contactResponseRepository.getUnsentContactResponses();
        for (int i = 0; i < unsentResponses.size(); i++) {
            ContactResponse unsentResponse = unsentResponses.get(i);
            l.add(unsentResponse.getContactCompoundId().getContactEmail());
        }
        
        return l;
    }

    private List<String> buildListOfIncomingUnrespondedLocationRequestsEmailAddresses(ContactRequestRepository contactRequestRepository) {
        List<String> l = new ArrayList<String>();
        
        List<ContactRequest> unrespondedRequests = contactRequestRepository.getIncomingUnrespondedLocationRequests();
        for (int i = 0; i < unrespondedRequests.size(); i++) {
            ContactRequest unrespondedRequest = unrespondedRequests.get(i);
            l.add(unrespondedRequest.getContactCompoundId().getContactEmail());
        }
        
        return l;
    }

    private List<String> buildListOfOutgoingUnsentLocationRequestsEmailAddresses(ContactRequestRepository contactRequestRepository) {
        List<String> l = new ArrayList<String>();
        
        List<ContactRequest> unsentRequests = contactRequestRepository.getUnsentContactRequests();
        for (int i = 0; i < unsentRequests.size(); i++) {
            ContactRequest unsentRequest = unsentRequests.get(i);
            l.add(unsentRequest.getContactCompoundId().getContactEmail());
        }
        
        return l;
    }

    private List<String> buildListOfOutgoingUnrespondedLocationRequestsEmailAddresses(ContactRequestRepository contactRequestRepository) {
        List<String> l = new ArrayList<String>();
        
        List<ContactRequest> unsentRequests = contactRequestRepository.getOutgoingUnrespondedLocationRequests();
        for (int i = 0; i < unsentRequests.size(); i++) {
            ContactRequest unsentRequest = unsentRequests.get(i);
            l.add(unsentRequest.getContactCompoundId().getContactEmail());
        }
        
        return l;
    }
}
