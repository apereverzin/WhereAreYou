package com.creek.whereareyou.android.activity.map;

import android.location.Location;

/**
 * 
 * @author Andrey Pereverzin
 */
public interface LocationAware {
    void updateWithNewLocation(Location location);
}
