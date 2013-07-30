package com.creek.whereareyou.android.infrastructure.sqlite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.creek.whereareyou.android.FileProvider;
import com.creek.whereareyou.android.util.DataConversionUtil;
import com.creek.whereareyoumodel.domain.ContactData;

/**
 * 
 * @author Andrey Pereverzin
 */
public class DBFileManager {
    private static final String TAG = DBFileManager.class.getSimpleName();

    private static final String WHEREAREYOU_DATA_FILE_PATH = "/Android/whereareyou.dat";
    
    private final FileProvider fp = new FileProvider();
    
    public List<ContactData> retrieveReservedContactData() {
        Log.d(TAG, "retrieveReservedContactData()");
        Log.d(TAG, "------------retrieveReservedContactData()");

        List<ContactData> contactDataList = new ArrayList<ContactData>();
        
        try {
            List<String> strings = fp.retrieveStringsFromFile(WHEREAREYOU_DATA_FILE_PATH);
            for (int i = 0; i < strings.size(); i++) {
                String csv = strings.get(i);
                ContactData contactData = DataConversionUtil.getContactDataFromCSV(csv);
                Log.d(TAG, "------------csv: " + csv);
                Log.d(TAG, "------------contactData: " + contactData);
                contactDataList.add(contactData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contactDataList;
    }
    
    public void reserveContactData(List<ContactData> contactDataList) {
        Log.d(TAG, "reserveContactData()");
        Log.d(TAG, "------------reserveContactData()");
        try {
            List<String> strings = new ArrayList<String>();
            for (int i = 0; i < contactDataList.size(); i++) {
                ContactData contactData = contactDataList.get(i);
                String csv = DataConversionUtil.getCSVFromContactData(contactData);
                Log.d(TAG, "------------contactData: " + contactData);
                Log.d(TAG, "------------csv: " + csv);
                strings.add(csv);
            }
            fp.persistStringsToFile(WHEREAREYOU_DATA_FILE_PATH, strings);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
