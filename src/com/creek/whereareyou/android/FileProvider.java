package com.creek.whereareyou.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.os.Environment;

/**
 * 
 * @author Andrey Pereverzin
 */
public class FileProvider {

    public String getFullFilePath(String filePath) {
        return Environment.getExternalStorageDirectory().getPath() + filePath;
    }

    public File getFile(String filePath) {
        return new File(getFullFilePath(filePath));
    }
    
    public Properties retrievePropertiesFromFile(String fileName) throws IOException {
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
    
    public String retrieveStringFromFile(String fileName) throws IOException {
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
    
    public List<String> retrieveStringsFromFile(String fileName) throws IOException {
        List<String> strings = new ArrayList<String>();
        File f = getFile(fileName);
        if(!f.exists()) {
            return strings;
        }
        
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String s;
            while((s = br.readLine()) != null) {
                strings.add(s);
            }
            return strings;
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
    
    public void persistStringToFile(String fileName, String value) throws IOException {
        File f = getNewFile(fileName);
        
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
    
    public void persistStringsToFile(String fileName, List<String> strings) throws IOException {
        File f = getNewFile(fileName);

        if (strings.size() == 0) {
            return;
        }
        
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(f);
            for (int i = 0; i < strings.size(); i++) {
                String s = strings.get(i);
                pw.write(s + "\n");
            }
        } finally {
            if(pw != null) {
                pw.close();
            }
        }
    }
    
    private File getNewFile(String fileName) throws IOException {
        File f = getFile(fileName);
        if(f.exists()) {
            f.delete();
        }
        
        f.createNewFile();
        
        return f;
    }
}
