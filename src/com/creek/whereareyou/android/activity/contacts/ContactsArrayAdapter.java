package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.android.util.Util.isStringNotEmpty;

import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyoumodel.domain.RequestAllowance;
import com.creek.whereareyoumodel.domain.sendable.ContactRequest;
import com.creek.whereareyoumodel.domain.sendable.ContactResponse;
import com.creek.whereareyoumodel.repository.ContactRequestRepository;
import com.creek.whereareyoumodel.repository.ContactResponseRepository;

import static com.creek.whereareyoumodel.domain.RequestAllowance.ALWAYS;
import static com.creek.whereareyoumodel.domain.RequestAllowance.NEVER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactsArrayAdapter extends ArrayAdapter<CombinedContactData> {
    private final Context context;
    private final List<CombinedContactData> values;
    private final List<String> outgoingUnsentLocationRequestsEmailAddresses;
    private final List<String> outgoingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> outgoingEmailsEverReceivedLocationResponses;
    private final List<String> incomingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> incomingUnsentLocationResponsesEmailAddresses;
    private final List<String> incomingEmailsEverSentLocationResponses;

    public ContactsArrayAdapter(Context _context, List<CombinedContactData> _values) {
        super(_context, R.layout.contact_row, _values);
        this.context = _context;
        this.values = _values;
        
        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();

        outgoingUnsentLocationRequestsEmailAddresses = buildListOfOutgoingUnsentLocationRequestsEmailAddresses(contactRequestRepository);
        outgoingUnrespondedLocationRequestsEmailAddresses = buildListOfOutgoingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        outgoingEmailsEverReceivedLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverReceived();
        incomingUnrespondedLocationRequestsEmailAddresses = buildListOfIncomingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        incomingUnsentLocationResponsesEmailAddresses = buildListOfIncomingUnsentLocationResponsesEmailAddresses(contactResponseRepository);
        incomingEmailsEverSentLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverSent();
        
        setRequestAllowances();
        setOutgoingRequestsStatus();
        setIncomingRequestsStatus();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.contact_name);

        CombinedContactData contact = values.get(position);
        
        textView.setText(contact.getAndroidContact().getDisplayName());

        setRequestAllowanceImage(rowView, contact);
        setOutgoingRequestImage(rowView, contact);
        setIncomingRequestImage(rowView, contact);

        return rowView;
    }
    
    public void updateData(AndroidContact androidContact) {
        
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
    
    private void setRequestAllowances() {
        for (int i = 0; i < values.size(); i++) {
            CombinedContactData c = values.get(i);
            int requestAllowanceCode = c.getAndroidContact().getContactData().getRequestAllowanceCode();
            c.setRequestAllowance(RequestAllowance.getRequestAllowance(requestAllowanceCode));
        }
    }
    
    private void setOutgoingRequestsStatus() {
        for (int i = 0; i < values.size(); i++) {
            CombinedContactData c = values.get(i);
            String emailAddress = c.getAndroidContact().getContactData().getContactEmail();
            if (outgoingUnsentLocationRequestsEmailAddresses.contains(emailAddress)) {
                c.setOutgoingState(OutgoingState.BEING_SENT);
            } else if (outgoingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
                c.setOutgoingState(OutgoingState.SENT);
            } else if (outgoingEmailsEverReceivedLocationResponses.contains(emailAddress)) {
                c.setOutgoingState(OutgoingState.RECEIVED);
            } else {
                c.setOutgoingState(OutgoingState.NONE);
            }
        }
    }
    
    private void setIncomingRequestsStatus() {
        for (int i = 0; i < values.size(); i++) {
            CombinedContactData c = values.get(i);
            String emailAddress = c.getAndroidContact().getContactData().getContactEmail();
            if (incomingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
                c.setIncomingState(IncomingState.RECEIVED);
            } else if (incomingUnsentLocationResponsesEmailAddresses.contains(emailAddress)) {
                c.setIncomingState(IncomingState.BEING_SENT);
            } else if (incomingEmailsEverSentLocationResponses.contains(emailAddress)) {
                c.setIncomingState(IncomingState.SENT);
            } else {
                c.setIncomingState(IncomingState.NONE);
            }
        }
    }

    private void setRequestAllowanceImage(View rowView, CombinedContactData contact) {
        ImageView requestAllowanceImageView = (ImageView) rowView.findViewById(R.id.request_allowance);

        if (isStringNotEmpty(contact.getAndroidContact().getContactData().getContactEmail())) {
            if (contact.getAndroidContact().getContactData().getRequestAllowanceCode() == ALWAYS.getCode()) {
                requestAllowanceImageView.setImageResource(R.drawable.request_allowed);
            } else if (contact.getAndroidContact().getContactData().getRequestAllowanceCode() == NEVER.getCode()) {
                requestAllowanceImageView.setImageResource(R.drawable.request_not_allowed);
            }
        }
    }
    
    private void setOutgoingRequestImage(View rowView, CombinedContactData contact) {
        ImageView outgoingRequestImageView = (ImageView) rowView.findViewById(R.id.outgoing_request_state);

        if (contact.getOutgoingState().hasImage()) {
            outgoingRequestImageView.setImageResource(contact.getOutgoingState().getImageId());
        }
    }
    
    private void setIncomingRequestImage(View rowView, CombinedContactData contact) {
        ImageView incomingRequestImageView = (ImageView) rowView.findViewById(R.id.incoming_request_state);
        
        if (contact.getIncomingState().hasImage()) {
            incomingRequestImageView.setImageResource(contact.getIncomingState().getImageId());
        }
    }
}
