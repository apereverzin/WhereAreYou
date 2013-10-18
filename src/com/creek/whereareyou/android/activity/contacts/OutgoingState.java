package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.R.drawable.*;

/**
 * 
 * @author Andrey Pereverzin
 */
public enum OutgoingState {
    NONE(-1, false),
    BEING_SENT(outgoing_request_being_sent, true),
    SENT(outgoing_request_sent, true),
    RECEIVED(outgoing_location_received, true);
    
    private final int imageId;
    private final boolean hasImage;

    private OutgoingState(int _imageId, boolean _hasImage) {
        this.imageId = _imageId;
        this.hasImage = _hasImage;
    }

    public int getImageId() {
        return imageId;
    }

    public boolean hasImage() {
        return hasImage;
    }
}
