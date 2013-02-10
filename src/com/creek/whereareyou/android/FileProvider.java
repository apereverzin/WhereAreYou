package com.creek.whereareyou.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.creek.whereareyoumodel.util.JSONTransformer;

import android.os.Environment;

/**
 * 
 * @author andreypereverzin
 */
public class FileProvider {

    public String getFullFilePath(String filePath) {
        return Environment.getExternalStorageDirectory().getPath() + filePath;
    }

    public File getFile(String filePath) {
        return new File(getFullFilePath(filePath));
    }
    
    public Properties getPropertiesFromFile(String fileName) throws IOException {
        File f = getFile(fileName);
        if (f.exists()) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(f));
            return properties;
        }
        
        return null;
    }
    
    public void persistPropertiesToFile(String fileName, Properties props) throws IOException {
        File f = getFile(fileName);
        f.createNewFile();
        props.store(new FileOutputStream(f), "");
    }
    
    public List<String[]> getPipeSeparatedStringsFromFile(String fileName) throws IOException {
        List<String[]> strings = new ArrayList<String[]>();
        
        return strings;
        
    }
    
    public void persistPipeSeparatedStringsToFile(String fileName, List<String[]> strings) throws IOException {
        File f = getFile(fileName);
        f.createNewFile();
        
    }
}
