package com.creek.whereareyou.android.activity.contacts;

import java.io.Serializable;

import com.creek.whereareyou.android.contacts.AndroidContact;
import com.creek.whereareyoumodel.domain.RequestAllowance;

/**
 * 
 * @author Andrey Pereverzin
 */
@SuppressWarnings("serial")
public class CombinedContactData implements Serializable {
    private final AndroidContact androidContact;
    private RequestAllowance requestAllowance;
    private OutgoingState outgoingState;
    private IncomingState incomingState;

    public CombinedContactData(AndroidContact androidContact) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((androidContact == null) ? 0 : androidContact.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CombinedContactData other = (CombinedContactData) obj;
        if (androidContact == null) {
            if (other.androidContact != null)
                return false;
        } else if (!androidContact.equals(other.androidContact))
            return false;
        return true;
    }
}
