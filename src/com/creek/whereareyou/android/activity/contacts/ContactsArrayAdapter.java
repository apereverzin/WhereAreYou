package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.android.util.Util.isStringNotEmpty;

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
    private List<ContactRequest> unsentRequests;
    private List<ContactRequest> unrespondedRequests;
    private List<ContactResponse> unsentResponses;

    public ContactsArrayAdapter(Context context, List<AndroidContact> values) {
        super(context, R.layout.contact_row, values);
        this.context = context;
        this.values = values;
        
        ContactRequestRepository contactRequestRepository = SQLiteRepositoryManager.getInstance().getContactRequestRepository();
        ContactResponseRepository<ContactResponseEntity> contactResponseRepository = SQLiteRepositoryManager.getInstance().getContactResponseRepository();

        unsentRequests = contactRequestRepository.getUnsentContactRequests();
        unrespondedRequests = contactRequestRepository.getUnrespondedLocationRequests();
        unsentResponses = (List<ContactResponse>) contactResponseRepository.getUnsentContactResponses();
        for (int i = 0; i < unsentRequests.size(); i++) {
            ContactRequest unsentRequest = unsentRequests.get(i);
            unsentRequest.getContactCompoundId().getContactEmail();
        }
        for (int i = 0; i < unrespondedRequests.size(); i++) {
            ContactRequest unrespondedRequest = unrespondedRequests.get(i);
            unrespondedRequest.getContactCompoundId().getContactEmail();
        }
        for (int i = 0; i < unsentResponses.size(); i++) {
            ContactResponse unsentResponse = unsentResponses.get(i);
            unsentResponse.getContactCompoundId().getContactEmail();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.contact_name);
        ImageView requestAllowanceImageView = (ImageView) rowView.findViewById(R.id.request_allowance);
        ImageView outgoingRequestImageView = (ImageView) rowView.findViewById(R.id.outgoing_request_state);
        ImageView incomingRequestImageView = (ImageView) rowView.findViewById(R.id.incoming_request_state);

        AndroidContact contact = values.get(position);
        
        textView.setText(contact.getDisplayName());

        if (isStringNotEmpty(contact.getContactData().getContactEmail())) {
            if (contact.getContactData().getRequestAllowanceCode() == ALWAYS.getCode()) {
                requestAllowanceImageView.setImageResource(R.drawable.request_allowed);
            } else if (contact.getContactData().getRequestAllowanceCode() == NEVER.getCode()) {
                requestAllowanceImageView.setImageResource(R.drawable.request_not_allowed);
            } else {
                //
            }
        }

        return rowView;
    }
}
