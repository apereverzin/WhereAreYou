package com.creek.whereareyou.android.util;

import java.util.StringTokenizer;

import android.location.Location;

import com.creek.whereareyoumodel.domain.ContactCompoundId;
import com.creek.whereareyoumodel.domain.ContactData;
import com.creek.whereareyoumodel.domain.LocationData;
import com.creek.whereareyoumodel.domain.RequestAllowance;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class DataConversionUtil {
    private static final String CONTACT_DATA_FORMAT = "%s,%s,%s,%s";
    private DataConversionUtil() {
        //
    }
    
    public static Location getLocationFromLocationResponse(LocationData locationData) {
        Location location = new Location("gps");
        location.setLatitude(locationData.getLatitude());
        location.setLongitude(locationData.getLongitude());
        location.setAccuracy(locationData.getAccuracy());
        location.setTime(locationData.getLocationTime());
        location.setSpeed(locationData.getSpeed());
        return location;
    }
    
    public static String getCSVFromContactData(ContactData contactData) {
        return String.format(CONTACT_DATA_FORMAT, 
                contactData.getContactCompoundId().getContactId(), 
                contactData.getContactCompoundId().getContactEmail(), 
                contactData.getRequestAllowance().getCode(), 
                contactData.getAllowanceDate());
    }
    
    public static ContactData getContactDataFromCSV(String csv) {
        ContactData contactData = new ContactData();
        StringTokenizer st = new StringTokenizer(csv, ",");
        String contactId = getNextToken(st, true);
        String contactEmail = getNextToken(st, true);
        int requestAllowanceCode = Integer.parseInt(getNextToken(st, true));
        String allowanceDateStr = getNextToken(st, false);
        contactData.setContactCompoundId(new ContactCompoundId(contactId, contactEmail));
        contactData.setRequestAllowance(RequestAllowance.getRequestAllowance(requestAllowanceCode));
        contactData.setAllowanceDate(getLongFromString(allowanceDateStr));

        return contactData;
    }
    
    private static String getNextToken(StringTokenizer st, boolean isMandatory) {
        if (st.hasMoreTokens()) {
            return st.nextToken();
        }
        
        if (isMandatory) {
            throw new IllegalStateException();
        }
        
        return null;
    }
    
    private static long getLongFromString(String str) {
        if (str == null || "".equals(str)) {
            return -1L;
        }
        return Long.parseLong(str);
    }
}
