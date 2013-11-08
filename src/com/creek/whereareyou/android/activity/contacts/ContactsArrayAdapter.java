package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.android.util.Util.isStringNotEmpty;

import java.util.List;

import com.creek.whereareyou.R;
import com.creek.whereareyoumodel.domain.RequestAllowance;

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
public class ContactsArrayAdapter extends ArrayAdapter<CombinedContactData> {
    private static final String TAG = ContactsArrayAdapter.class.getSimpleName();

    private final Context ctx;
    private final List<CombinedContactData> values;

    public ContactsArrayAdapter(Context _ctx, List<CombinedContactData> _values) {
        super(_ctx, R.layout.contact_row, _values);
        this.ctx = _ctx;
        this.values = _values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.contact_name);

        CombinedContactData contact = values.get(position);
        
        textView.setText(contact.getAndroidContact().getDisplayName());

        setRequestAllowanceImage(rowView, contact);
        setOutgoingRequestImage(rowView, contact);
        setIncomingRequestImage(rowView, contact);

        return rowView;
    }
    
    public void updateRequestAllowance(int position, RequestAllowance requestAllowance) {
        Log.d(TAG, "updateRequestAllowance " + position + ", " + requestAllowance.name());
        CombinedContactData contactData = values.get(position);
        contactData.setRequestAllowance(requestAllowance);
    }
    
    public void updateOutgoingState(int position, OutgoingState outgoingState) {
        Log.d(TAG, "updateOutgoingState " + position + ", " + outgoingState.name());
        CombinedContactData contactData = values.get(position);
        contactData.setOutgoingState(outgoingState);
    }

    public void updateIncomingState(int position, IncomingState incomingState) {
        Log.d(TAG, "updateIncomingState " + position + ", " + incomingState.name());
        CombinedContactData contactData = values.get(position);
        contactData.setIncomingState(incomingState);
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
