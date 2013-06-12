package com.creek.whereareyou.android.db;

import com.creek.whereareyoumodel.domain.sendable.ContactResponse;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactResponseEntity extends ContactResponse {
    private int type;
    private int requestId;
    private int locationId;
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getRequestId() {
        return requestId;
    }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    public int getLocationId() {
        return locationId;
    }
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
