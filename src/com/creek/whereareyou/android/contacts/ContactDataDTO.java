package com.creek.whereareyou.android.contacts;

import java.io.Serializable;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.RequestAllowance;
import static com.creek.whereareyoumodel.domain.RequestAllowance.NEVER;

/**
 * 
 * @author Andrey Pereverzin
 */
@SuppressWarnings("serial")
public class ContactDataDTO implements Serializable {
    private long id;
    private final String contactId;
    private String contactEmail;
    private int requestAllowanceCode;
    private long allowanceDate;

    public ContactDataDTO(String _contactId) {
        this.contactId = _contactId;
    }

    public ContactDataDTO(String _contactId, ContactData contactData) {
        this.contactId = _contactId;
        if (contactData == null) {
            id = -1L;
            contactEmail = "";
            requestAllowanceCode = NEVER.getCode();
            allowanceDate = 0L;
        } else {
            id = contactData.getId();
            contactEmail = contactData.getContactCompoundId().getContactEmail();
            requestAllowanceCode = contactData.getRequestAllowance().getCode();
            allowanceDate = contactData.getAllowanceDate();
        }
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getRequestAllowanceCode() {
        return requestAllowanceCode;
    }

    public void setRequestAllowanceCode(int requestAllowanceCode) {
        this.requestAllowanceCode = requestAllowanceCode;
    }

    public long getAllowanceDate() {
        return allowanceDate;
    }

    public void setAllowanceDate(long allowanceDate) {
        this.allowanceDate = allowanceDate;
    }
    
    public ContactData toContactData() {
        ContactData contactData = new ContactData();
        
        contactData.setId(id);
        ContactCompoundId contactCompoundId = new ContactCompoundId(contactId, contactEmail);
        contactData.setContactCompoundId(contactCompoundId);
        contactData.setRequestAllowance(RequestAllowance.getRequestAllowance(requestAllowanceCode));
        contactData.setAllowanceDate(allowanceDate);
        
        return contactData;
    }

    @Override
    public String toString() {
        return "ContactDataDTO [id=" + id + ", contactId=" + contactId + ", contactEmail=" + contactEmail + ", requestAllowanceCode=" + requestAllowanceCode + ", allowanceDate=" + allowanceDate + "]";
    }
}
