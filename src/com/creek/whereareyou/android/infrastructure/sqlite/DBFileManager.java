package com.creek.whereareyou.android.infrastructure.sqlite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyou.android.FileProvider;
import com.creek.whereareyou.android.util.DataConversionUtil;
import com.creek.whereareyoumodel.domain.ContactData;

/**
 * 
 * @author Andrey Pereverzin
 */
public class DBFileManager {
    private static final String WHEREAREYOU_DATA_FILE_PATH = "/Android/whereareyou.dat";
    
    private final FileProvider fp = new FileProvider();
    
    public List<ContactData> retrieveReservedContactData() {
        List<ContactData> contactDataList = new ArrayList<ContactData>();
        
        try {
            List<String> strings = fp.retrieveStringsFromFile(WHEREAREYOU_DATA_FILE_PATH);
            for (int i = 0; i < strings.size(); i++) {
                String csv = strings.get(i);
                ContactData contactData = DataConversionUtil.getContactDataFromCSV(csv);
                contactDataList.add(contactData);
            }
        } catch (IOException ex) {
            //
        }

        return contactDataList;
    }
    
    public void reserveContactData(List<ContactData> contactDataList) {
        try {
            List<String> strings = new ArrayList<String>();
            for (int i = 0; i < contactDataList.size(); i++) {
                ContactData contactData = contactDataList.get(i);
                String csv = DataConversionUtil.getCSVFromContactData(contactData);
                strings.add(csv);
            }
            fp.persistStringsToFile(WHEREAREYOU_DATA_FILE_PATH, strings);
        } catch (IOException ex) {
            //
        }
    }

}
