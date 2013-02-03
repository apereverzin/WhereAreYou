package com.creek.whereareyou.android;

import java.io.File;

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
}
