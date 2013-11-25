package com.creek.whereareyou.android.activity.map;

import com.creek.whereareyou.android.contacts.AndroidContact;

import android.location.Location;

/**
 * 
 * @author Andrey Pereverzin
 */
public interface LocationAware {
    void updateWithNewLocation(Location location);
    
    void updateWithNewContactDataAndLocation(AndroidContact contactData, long locationTime, Location location);
}
