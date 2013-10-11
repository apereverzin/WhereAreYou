package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.android.util.Util.isStringNotEmpty;

import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.R;
import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyou.android.db.ContactResponseEntity;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
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
public class ContactsArrayAdapter extends ArrayAdapter<AndroidContact> {
    private final Context context;
    private final List<AndroidContact> values;
    private final List<String> outgoingUnsentLocationRequestsEmailAddresses;
    private final List<String> incomingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> incomingUnsentLocationResponsesEmailAddresses;
    private final List<String> incomingEmailsEverSentLocationResponses;
    private final List<String> outgoingEmailsEverReceivedLocationResponses;

    public ContactsArrayAdapter(Context context, List<AndroidContact> values) {
        super(context, R.layout.contact_row, values);
        this.context = context;
        this.values = values;
        
        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();

        outgoingUnsentLocationRequestsEmailAddresses = buildListOfOutgoingUnsentLocationRequestsEmailAddresses(contactRequestRepository);
        incomingUnrespondedLocationRequestsEmailAddresses = buildListOfIncomingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        incomingUnsentLocationResponsesEmailAddresses = buildListOfIncomingUnsentLocationResponsesEmailAddresses(contactResponseRepository);
        incomingEmailsEverSentLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverSent();
        outgoingEmailsEverReceivedLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverReceived();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.contact_name);

        AndroidContact contact = values.get(position);
        
        textView.setText(contact.getDisplayName());

        String emailAddress = contact.getContactData().getContactEmail();
        setRequestAllowanceImage(rowView, contact);
        setOutgoingRequestImage(rowView, emailAddress);
        setIncomingRequestImage(rowView, emailAddress);

        return rowView;
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

    private void setRequestAllowanceImage(View rowView, AndroidContact contact) {
        ImageView requestAllowanceImageView = (ImageView) rowView.findViewById(R.id.request_allowance);

        if (isStringNotEmpty(contact.getContactData().getContactEmail())) {
            if (contact.getContactData().getRequestAllowanceCode() == ALWAYS.getCode()) {
                requestAllowanceImageView.setImageResource(R.drawable.request_allowed);
            } else if (contact.getContactData().getRequestAllowanceCode() == NEVER.getCode()) {
                requestAllowanceImageView.setImageResource(R.drawable.request_not_allowed);
            }
        }
    }
    
    private void setOutgoingRequestImage(View rowView, String emailAddress) {
        ImageView outgoingRequestImageView = (ImageView) rowView.findViewById(R.id.outgoing_request_state);

        if (outgoingUnsentLocationRequestsEmailAddresses.contains(emailAddress)) {
            outgoingRequestImageView.setImageResource(R.drawable.request_being_sent);
        } else if (incomingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
            outgoingRequestImageView.setImageResource(R.drawable.request_sent);
        } else if (outgoingEmailsEverReceivedLocationResponses.contains(emailAddress)) {
            outgoingRequestImageView.setImageResource(R.drawable.location_received);
        }
    }
    
    private void setIncomingRequestImage(View rowView, String emailAddress) {
        ImageView incomingRequestImageView = (ImageView) rowView.findViewById(R.id.incoming_request_state);
        
        if (outgoingUnsentLocationRequestsEmailAddresses.contains(emailAddress)) {
            incomingRequestImageView.setImageResource(R.drawable.request_received);
        } else if (incomingUnsentLocationResponsesEmailAddresses.contains(emailAddress)) {
            incomingRequestImageView.setImageResource(R.drawable.location_being_sent);
        } else if (incomingEmailsEverSentLocationResponses.contains(emailAddress)) {
            incomingRequestImageView.setImageResource(R.drawable.location_sent);
        }
    }
}
