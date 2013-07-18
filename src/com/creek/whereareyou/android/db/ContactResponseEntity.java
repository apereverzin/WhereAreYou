package com.creek.whereareyou.android.db;

import com.creek.whereareyoumodel.domain.sendable.ContactResponse;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ContactResponseEntity extends ContactResponse {
    private int type;
    private long requestId;
    private long locationId;
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
    
    public long getLocationId() {
        return locationId;
    }
    
    public void setLocationDataId(long locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ContactResponseEntity [").append(super.toString()).append(", message=").append(getMessage()).append(", locationData=").append(getLocationData()).append(", type=")
                .append(getType()).append(", requestId=").append(getRequestId()).append(", locationId=").append(getLocationId()).append("]");
        return builder.toString();
    }
}
