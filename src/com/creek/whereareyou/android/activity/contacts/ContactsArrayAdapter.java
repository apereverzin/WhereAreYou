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
import android.util.Log;
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
    private static final String TAG = ContactsArrayAdapter.class.getSimpleName();

    private final Context context;
    private final List<AndroidContact> values;
    private final List<String> outgoingUnsentLocationRequestsEmailAddresses;
    private final List<String> outgoingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> outgoingEmailsEverReceivedLocationResponses;
    private final List<String> incomingUnrespondedLocationRequestsEmailAddresses;
    private final List<String> incomingUnsentLocationResponsesEmailAddresses;
    private final List<String> incomingEmailsEverSentLocationResponses;

    public ContactsArrayAdapter(Context context, List<AndroidContact> values) {
        super(context, R.layout.contact_row, values);
        this.context = context;
        this.values = values;
        
        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();

        outgoingUnsentLocationRequestsEmailAddresses = buildListOfOutgoingUnsentLocationRequestsEmailAddresses(contactRequestRepository);
        outgoingUnrespondedLocationRequestsEmailAddresses = buildListOfOutgoingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        outgoingEmailsEverReceivedLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverReceived();
        incomingUnrespondedLocationRequestsEmailAddresses = buildListOfIncomingUnrespondedLocationRequestsEmailAddresses(contactRequestRepository);
        incomingUnsentLocationResponsesEmailAddresses = buildListOfIncomingUnsentLocationResponsesEmailAddresses(contactResponseRepository);
        incomingEmailsEverSentLocationResponses = contactResponseRepository.getEmailAddressesForResponsesEverSent();
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

    private List<String> buildListOfOutgoingUnrespondedLocationRequestsEmailAddresses(ContactRequestRepository contactRequestRepository) {
        List<String> l = new ArrayList<String>();
        
        List<ContactRequest> unsentRequests = contactRequestRepository.getOutgoingUnrespondedLocationRequests();
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
                Log.d(TAG, "-------allowance: " + contact.getDisplayName() + " ALWAYS");
                requestAllowanceImageView.setImageResource(R.drawable.request_allowed);
            } else if (contact.getContactData().getRequestAllowanceCode() == NEVER.getCode()) {
                Log.d(TAG, "-------allowance: " + contact.getDisplayName() + " NEVER");
                requestAllowanceImageView.setImageResource(R.drawable.request_not_allowed);
            }
        } else {
            Log.d(TAG, "-------allowance: " + contact.getDisplayName() + " dummy");
            //requestAllowanceImageView.setImageResource(R.drawable.outgoing_dummy);
        }
    }
    
    private void setOutgoingRequestImage(View rowView, String emailAddress) {
        ImageView outgoingRequestImageView = (ImageView) rowView.findViewById(R.id.outgoing_request_state);

        if (outgoingUnsentLocationRequestsEmailAddresses.contains(emailAddress)) {
            Log.d(TAG, "-------outgoing: " + emailAddress + " unsent");
            outgoingRequestImageView.setImageResource(R.drawable.outgoing_request_being_sent);
        } else if (outgoingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
            Log.d(TAG, "-------outgoing: " + emailAddress + " unresponded");
            outgoingRequestImageView.setImageResource(R.drawable.outgoing_request_sent);
        } else if (outgoingEmailsEverReceivedLocationResponses.contains(emailAddress)) {
            Log.d(TAG, "-------outgoing: " + emailAddress + " ever");
            outgoingRequestImageView.setImageResource(R.drawable.outgoing_location_received);
        } else {
            Log.d(TAG, "-------outgoing: " + emailAddress + " dummy");
            //outgoingRequestImageView.setImageResource(R.drawable.outgoing_dummy);
        }
    }
    
    private void setIncomingRequestImage(View rowView, String emailAddress) {
        ImageView incomingRequestImageView = (ImageView) rowView.findViewById(R.id.incoming_request_state);
        
        if (incomingUnrespondedLocationRequestsEmailAddresses.contains(emailAddress)) {
            Log.d(TAG, "-------incoming: " + emailAddress + " unresponded");
            incomingRequestImageView.setImageResource(R.drawable.incoming_request_received);
        } else if (incomingUnsentLocationResponsesEmailAddresses.contains(emailAddress)) {
            Log.d(TAG, "-------incoming: " + emailAddress + " unsent");
            incomingRequestImageView.setImageResource(R.drawable.incoming_location_being_sent);
        } else if (incomingEmailsEverSentLocationResponses.contains(emailAddress)) {
            Log.d(TAG, "-------incoming: " + emailAddress + " ever");
            incomingRequestImageView.setImageResource(R.drawable.incoming_location_sent);
        } else {
            Log.d(TAG, "-------incoming: " + emailAddress + " dummy");
            //incomingRequestImageView.setImageResource(R.drawable.incoming_dummy);
        }
    }
}
