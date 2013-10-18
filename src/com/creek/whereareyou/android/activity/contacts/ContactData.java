package com.creek.whereareyou.android.activity.contacts;

import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyoumodel.domain.RequestAllowance;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactData {
    private final AndroidContact androidContact;
    private RequestAllowance requestAllowance;
    private OutgoingState outgoingState;
    private IncomingState incomingState;

    public ContactData(AndroidContact androidContact) {
        this.androidContact = androidContact;
    }

    public AndroidContact getAndroidContact() {
        return androidContact;
    }    

    public RequestAllowance getRequestAllowance() {
        return requestAllowance;
    }

    public void setRequestAllowance(RequestAllowance requestAllowance) {
        this.requestAllowance = requestAllowance;
    }

    public OutgoingState getOutgoingState() {
        return outgoingState;
    }

    public void setOutgoingState(OutgoingState outgoingState) {
        this.outgoingState = outgoingState;
    }

    public IncomingState getIncomingState() {
        return incomingState;
    }

    public void setIncomingState(IncomingState incomingState) {
        this.incomingState = incomingState;
    }
}
