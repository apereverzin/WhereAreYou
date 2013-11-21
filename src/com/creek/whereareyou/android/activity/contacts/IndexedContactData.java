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
    private final int firstVisiblePosition;

    public IndexedContactData(CombinedContactData _contactData, int _contactInd, int _firstVisiblePosition) {
        this.contactData = _contactData;
        this.contactInd = _contactInd;
        this.firstVisiblePosition = _firstVisiblePosition;
    }

    public CombinedContactData getContactData() {
        return contactData;
    }

    public int getContactInd() {
        return contactInd;
    }

    public int getFirstVisiblePosition() {
        return firstVisiblePosition;
    }
}
