package com.creek.whereareyou.android.activity.contacts;

import java.io.Serializable;


/**
 * 
 * @author andreypereverzin
 */
@SuppressWarnings("serial")
public class IndexedContactData implements Serializable {
    private final CombinedContactData contactData;
    private final int contactInd;

    public IndexedContactData(CombinedContactData _contactData, int _contactInd) {
        this.contactData = _contactData;
        this.contactInd = _contactInd;
    }

    public CombinedContactData getContactData() {
        return contactData;
    }

    public int getContactInd() {
        return contactInd;
    }
}
