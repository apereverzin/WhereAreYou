package com.creek.whereareyou.android.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

import android.text.format.DateFormat;
import com.google.android.maps.GeoPoint;

/**
 * 
 * @author Andrey Pereverzin
 */
public class Util {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
    private static final MathContext DOUBLES_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

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

    public static String formatDate(long date) {
        return (String) DateFormat.format(DATE_FORMAT, new Date(date));
    }

    public static String formatDateTime(long date) {
        return (String) DateFormat.format(DATE_TIME_FORMAT, new Date(date));
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
}
