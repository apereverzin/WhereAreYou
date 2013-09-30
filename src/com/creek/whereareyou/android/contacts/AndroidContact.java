package com.creek.whereareyou.android.contacts;

import java.io.Serializable;

/**
 * 
 * @author Andrey Pereverzin
 */
@SuppressWarnings("serial")
public class AndroidContact implements Comparable<AndroidContact>, Serializable {
    private final String contactId;
    private String contactEmail;
    private String displayName;
    private ContactDataDTO contactData;

    public AndroidContact(String _contactId) {
        this.contactId = _contactId;
    }
    
    public String getContactId() {
        return contactId;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ContactDataDTO getContactData() {
        return contactData;
    }

    public void setContactData(ContactDataDTO contactData) {
        this.contactData = contactData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contactId == null) ? 0 : contactId.hashCode());
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
        if (contactId == null) {
            if (other.contactId != null)
                return false;
        } else if (!contactId.equals(other.contactId))
            return false;
        return true;
    }

    @Override
    public int compareTo(AndroidContact other) {
        return getDisplayName().compareTo(other.getDisplayName());
    }

    @Override
    public String toString() {
        return "AndroidContact [contactId=" + contactId + ", contactEmail=" + contactEmail + ", displayName=" + displayName + ", contactData=" + contactData + "]";
    }
}
