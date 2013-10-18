package com.creek.whereareyou.android.activity.contacts;

import static com.creek.whereareyou.R.drawable.*;

/**
 * 
 * @author Andrey Pereverzin
 */
public enum IncomingState {
    NONE(-1, false),
    RECEIVED(incoming_request_received, true),
    BEING_SENT(incoming_location_being_sent, true),
    SENT(incoming_location_sent, true);
    
    private final int imageId;
    private final boolean hasImage;

    private IncomingState(int _imageId, boolean _hasImage) {
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
