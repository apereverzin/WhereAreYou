package com.creek.whereareyou.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.creek.whereareyoumodel.message.Transformable;
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
    
    public String getStringFromFile(String fileName) throws IOException {
        File f = getFile(fileName);
        if(!f.exists()) {
            return "";
        }
        
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            return br.readLine();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
    
    public void persistStringToFile(String fileName, String value) throws IOException {
        File f = getFile(fileName);
        if(f.exists()) {
            f.delete();
        }
        
        f.createNewFile();
        
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(f);
            pw.write(value);
        } finally {
            if(pw != null) {
                pw.close();
            }
        }
    }
}
