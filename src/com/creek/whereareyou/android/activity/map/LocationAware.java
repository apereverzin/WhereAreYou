package com.creek.whereareyou.android.activity.map;

import android.location.Location;

public interface LocationAware {

    public abstract void updateWithNewLocation(Location location);

}