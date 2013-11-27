package com.creek.whereareyou.android.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import com.creek.whereareyou.R;
import com.google.android.maps.GeoPoint;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class Util {
    private static final String LOCATION_DATE_TIME_FORMAT_DEF = "HH:mm dd/MM/yyyy";
    private static final java.text.DateFormat LOCATION_DATE_TIME_FORMAT = new SimpleDateFormat(LOCATION_DATE_TIME_FORMAT_DEF);
    private static final MathContext DOUBLES_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

    private Util() {
        //
    }
    
    /** Radius of the Earth in meters */
    private static final double EARTH_RADIUS = 6371000;

    public static double getDistance(GeoPoint geoPoint1, GeoPoint geoPoint2) {
        double lat1 = geoPoint1.getLatitudeE6() / 1E6;
        double lat2 = geoPoint2.getLatitudeE6() / 1E6;
        double lon1 = geoPoint1.getLongitudeE6() / 1E6;
        double lon2 = geoPoint2.getLongitudeE6() / 1E6;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public static String formatDouble(double val) {
        try {
            return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (NumberFormatException ex) {
            return Double.toString(val);
        }
    }
    
    public static String[] concatArrays(String[] array1, String[] array2) {
        String[] newArray = new String[array1.length + array2.length];
        for (int i = 0; i < array1.length; i++) {
            newArray[i] = array1[i];
        }
        int n = array1.length;
        for (int i = n; i < n + array2.length; i++) {
            newArray[i] = array2[i - n];
        }
        return newArray;
    }
    
    public static boolean isStringNotEmpty(String str) {
        return str != null && !"".equals(str);
    }
    
    public static final String buildGooglemailAddress(Context ctx, String username) {
        if (username != null && username.length() > 0) {
            return username + ctx.getString(R.string.googlemail_com);
        }

        return "";
    }
    
    public static String formatLocationTime(long locationTime) {
        return LOCATION_DATE_TIME_FORMAT.format(new Date(locationTime));
    }
}
