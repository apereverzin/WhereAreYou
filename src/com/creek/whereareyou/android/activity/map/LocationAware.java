package com.creek.whereareyou.android.activity.map;

import com.creek.whereareyoumodel.domain.ContactData;

import android.location.Location;

/**
 * 
 * @author Andrey Pereverzin
 */
public interface LocationAware {
    void updateWithNewLocation(Location location);
    void updateWithNewLocation(ContactData contactDaat, Location location);
}
