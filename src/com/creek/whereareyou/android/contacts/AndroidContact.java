package com.creek.whereareyou.android.contacts;

import java.io.Serializable;

import com.creek.whereareyoumodel.domain.ContactData;

/**
 * 
 * @author Andrey Pereverzin
 */
@SuppressWarnings("serial")
public class AndroidContact implements Comparable<AndroidContact>, Serializable {
    private final String id;
    private String displayName;
    private String email;
    private boolean requestAllowed;

    public AndroidContact(String id) {
        this.id = id;
    }

    public AndroidContact(ContactData contactData) {
        this.id = contactData.getContactCompoundId().getContactId();
        this.email = contactData.getContactCompoundId().getContactEmail();
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getId() {
        return id;
    }

    public boolean isRequestAllowed() {
        return requestAllowed;
    }

    public void setRequestAllowed(boolean requestAllowed) {
        this.requestAllowed = requestAllowed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        AndroidContact other = (AndroidContact) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(AndroidContact other) {
        return getDisplayName().compareTo(other.getDisplayName());
    }

    @Override
    public String toString() {
        return "AndroidContact [id=" + id + ", displayName=" + displayName + ", email=" + email + ", requestAllowed=" + requestAllowed + "]";
    }
}
